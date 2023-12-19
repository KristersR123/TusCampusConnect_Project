package com.example.myapplication.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor() : ViewModel() {
    // Firebase authentication instance
    val auth = FirebaseAuth.getInstance()

    // User ID of the current authenticated user
    private var userId: String? = null

    // Reference to the user's document in Firestore
    private val userDocument: DocumentReference?
        get() = userId?.let {
            FirebaseFirestore.getInstance().collection("users").document(it)
        }

    // Reference to the collection of events for the current user in Firestore
    private val eventsCollection: CollectionReference?
        get() = userDocument?.collection("events")

    // StateFlow to hold the list of events for the current user
    private val _events = MutableStateFlow(emptyList<Pair<String, String>>())
    val events: StateFlow<List<Pair<String, String>>> = _events

    init {
        // Initialize the view model by getting the user ID and starting to listen for events
        getUserId()
        startListeningForEvents()
    }

    // Get the user ID of the currently authenticated user
    private fun getUserId() {
        userId = FirebaseAuth.getInstance().currentUser?.uid
    }

    // Start listening for changes in the "events" collection specific to the current user
    private fun startListeningForEvents() {
        userId?.let { uid ->
            eventsCollection?.addSnapshotListener { snapshot, error ->
                // Snapshot listener callback for changes in events collection
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                // Extract and map updated events from the snapshot
                val updatedEvents = snapshot?.documents?.mapNotNull {
                    val eventName = it["eventName"] as? String
                    val eventDateTime = it["eventDateTime"] as? String
                    if (eventName != null && eventDateTime != null) {
                        Pair(eventName, eventDateTime)
                    } else {
                        null
                    }
                } ?: emptyList()

                // Update the state with the new events
                _events.value = updatedEvents
            }
        }
    }

    // Add a new event to the user's collection in Firestore
    fun addEvent(eventName: String, eventDateTime: String) {
        // Create a new event pair
        val newEvent = Pair(eventName, eventDateTime)

        // Add the event directly to the user's document
        userId?.let { uid ->
            userDocument?.collection("events")?.add(
                mapOf(
                    "eventName" to eventName,
                    "eventDateTime" to eventDateTime,
                    "userId" to uid
                )
            )?.addOnSuccessListener {
                // Successfully added to Firestore
                // No need to fetch events here, as the listener will update the state
            }?.addOnFailureListener {
                // Handle failure
            }
        }
    }
}