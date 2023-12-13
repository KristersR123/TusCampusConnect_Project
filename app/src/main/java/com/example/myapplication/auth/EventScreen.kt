package com.example.myapplication.auth

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.IgViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(navController: NavController, vm: EventViewModel) {
    var eventName by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf<Date?>(null) }
    var events by remember { mutableStateOf(emptyList<Pair<String, String>>()) } // List to store events

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7E6E44))
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

            // Event Date
            DatePicker(
                selectedDate = eventDate,
                onDateSelected = { eventDate = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp)
            )

            // Add Event Button
            Button(
                onClick = {
                    // Handle adding event here
                    if (!eventName.isBlank() && eventDate != null) {
                        // Format the date if needed
                        val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(eventDate!!)
                        // Do something with the event details, e.g., pass them to ViewModel
                        vm.addEvent(eventName, formattedDate)
                        // Reset the fields
                        eventName = ""
                        eventDate = null
                    } else {
                        // Show a message or handle the case when the input is not valid
                    }
                },
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
                    text = "Add Event",
                    fontSize = 18.sp
                )
            }

            // Display saved events
            val events by vm.events.collectAsState()
            events.forEach { (name, date) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Event: $name")
                    Text(text = "Date: $date")
                }
            }
        }
    }
}

@Composable
fun DatePicker(
    selectedDate: Date?,
    onDateSelected: (Date) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier



    ) {
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
                text = selectedDate?.let {
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(it)
                } ?: "Select Date",
                fontSize = 16.sp
            )
        }

        if (showDialog) {
            val context = LocalContext.current
            val currentDate = selectedDate ?: Calendar.getInstance().time
            val datePicker = remember { mutableStateOf(currentDate) }

            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }.time
                    datePicker.value = selectedDate
                    onDateSelected(selectedDate)
                },
                datePicker.value.year + 1900,
                datePicker.value.month,
                datePicker.value.date
            ).show()
        }
    }
}