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
    val auth = FirebaseAuth.getInstance()
    private var userId: String? = null

    private val userDocument: DocumentReference?
        get() = userId?.let {
            FirebaseFirestore.getInstance().collection("users").document(it)
        }

    private val eventsCollection: CollectionReference?
        get() = userDocument?.collection("events")

    private val _events = MutableStateFlow(emptyList<Pair<String, String>>())
    val events: StateFlow<List<Pair<String, String>>> = _events

    init {
        getUserId()
        startListeningForEvents()
    }

    private fun getUserId() {
        userId = FirebaseAuth.getInstance().currentUser?.uid
    }

    private fun startListeningForEvents() {
        userId?.let { uid ->
            eventsCollection?.whereEqualTo("userId", uid)?.addSnapshotListener { snapshot, error ->
                // ...
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val updatedEvents = snapshot?.documents?.mapNotNull {
                    val eventName = it["eventName"] as? String
                    val eventDateTime = it["eventDateTime"] as? String
                    if (eventName != null && eventDateTime != null) {
                        Pair(eventName, eventDateTime)
                    } else {
                        null
                    }
                } ?: emptyList()

                // Update the state
                _events.value = updatedEvents
            }
        }
    }

    fun addEvent(eventName: String, eventDateTime: String) {
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
