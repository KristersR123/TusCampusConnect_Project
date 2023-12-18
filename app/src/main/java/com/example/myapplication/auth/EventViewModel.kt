package com.example.myapplication.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val userId: String?
        get() = auth.currentUser?.uid

    private val eventsCollection: CollectionReference?
        get() = userId?.let {
            FirebaseFirestore.getInstance().collection("users").document(it).collection("events")
        }

    private val _events = MutableStateFlow(emptyList<Pair<String, String>>())
    val events: StateFlow<List<Pair<String, String>>> = _events

    init {
        // Listen for changes in Firestore and update the state
        eventsCollection?.addSnapshotListener { snapshot, error ->
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
    fun addEvent(eventName: String, eventDateTime: String) {
        val newEvent = Pair(eventName, eventDateTime)

        // Add the event to Firestore
        eventsCollection?.add(mapOf(
            "eventName" to eventName,
            "eventDateTime" to eventDateTime
        ))?.addOnSuccessListener {
            // Successfully added to Firestore
        }?.addOnFailureListener {
            // Handle failure
        }
    }
}
