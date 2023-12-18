package com.example.myapplication.auth

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.IgViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(navController: NavController, vm: EventViewModel) {
    var eventName by remember { mutableStateOf("") }
    var eventDateTime by remember { mutableStateOf<Date?>(null) }
    var events by remember { mutableStateOf(emptyList<Pair<String, String>>()) } // List to store events

    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid
    val eventsCollection = userId?.let {
        FirebaseFirestore.getInstance().collection("users").document(it).collection("events")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7E6E44)) // Reverted to the original background color
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Event Name
            TextField(
                value = eventName,
                onValueChange = { eventName = it },
                label = { Text("Event Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(bottom = 16.dp)
            )

            // Event Date and Time
            DatePicker(
                selectedDateTime = eventDateTime,
                onDateTimeSelected = { eventDateTime = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp)
            )

            // Add Event Button
            Button(
                onClick = {
                    if (!eventName.isBlank() && eventDateTime != null) {
                        val formattedDateTime = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(eventDateTime!!)
                        vm.addEvent(eventName, formattedDateTime)
                        eventName = ""
                        eventDateTime = null
                    } else {
                        // Show a message or handle the case when the input is not valid
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF6A5B3B)) // Darker than the background
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = contentColorFor(Color(0xFF6A5B3B)) // Text color darker than the button background
                )
            ) {
                Text(
                    text = "Add Event",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Display saved events
            val events by vm.events.collectAsState()
            if (events.isNotEmpty()) {
                Column {
                    events.forEach { (name, date) ->
                        EventListItem(name = name, date = date)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                Text("No events added yet.")
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Back Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF6A5B3B), Color(0xFF6A5B3B))
                        )
                    )
            ) {
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    Text(
                        text = "Back",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
@Composable
fun DatePicker(
    selectedDateTime: Date?,
    onDateTimeSelected: (Date) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color(
                    red = 0.02916666679084301f,
                    green = 0.028315972536802292f,
                    blue = 0.028315972536802292f,
                    alpha = 1f
                )
            )
        ) {
            Text(
                text = selectedDateTime?.let {
                    SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(it)
                } ?: "Select Date and Time",
                fontSize = 16.sp
            )
        }

        if (showDialog) {
            val context = LocalContext.current
            val currentDate = selectedDateTime ?: Calendar.getInstance().time
            var dateTimePicker by remember { mutableStateOf(currentDate) }

            Column {
                // DatePicker
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        dateTimePicker = Calendar.getInstance().apply {
                            set(Calendar.YEAR, year)
                            set(Calendar.MONTH, month)
                            set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        }.time
                    },
                    dateTimePicker.year + 1900,
                    dateTimePicker.month,
                    dateTimePicker.date
                ).show()

                // TimePicker
                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        dateTimePicker = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                        }.time
                        onDateTimeSelected(dateTimePicker)
                        showDialog = false
                    },
                    dateTimePicker.hours,
                    dateTimePicker.minutes,
                    false
                ).show()
            }
        }
    }
}

@Composable
fun EventListItem(name: String, date: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Event: $name", fontWeight = FontWeight.Bold)
        Text(text = "Date: $date", color = Color.White)
    }
}