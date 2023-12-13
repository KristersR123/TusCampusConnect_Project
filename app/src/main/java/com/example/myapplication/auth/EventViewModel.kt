package com.example.myapplication.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val _events = MutableStateFlow(emptyList<Pair<String, String>>())
    val events: StateFlow<List<Pair<String, String>>> = _events

    fun addEvent(eventName: String, eventDate: String) {
        val newEvent = Pair(eventName, eventDate)
        _events.value = _events.value + newEvent
    }
}