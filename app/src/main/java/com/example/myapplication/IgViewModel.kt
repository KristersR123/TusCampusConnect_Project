package com.example.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


// got help from -> about login/register. W L PROJECT - FIREBASE LOGIN/REGISTER: https://www.youtube.com/watch?v=ti6Ci0s4SD8&ab_channel=WLPROJECT



@HiltViewModel
class IgViewModel @Inject constructor(
    val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : ViewModel() {

    // Mutable state variables using Jetpack Compose's mutableStateOf
    val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val popupNotification = mutableStateOf<com.example.myapplication.Event<String>?>(null)


    // Function to handle user signup
    fun onSignup(email: String, pass: String, course: String) {
        viewModelScope.launch {
            inProgress.value = true

            try {
                // Create a new user using Firebase authentication
                val result = auth.createUserWithEmailAndPassword(email, pass).await()
                if (result.user != null) {
                    // Signup successful, set signedIn to true
                    signedIn.value = true
                    handleException(null, "signup successful")

                    // Store user information in Firestore
                    val userId = result.user?.uid
                    if (userId != null) {
                        storeUserInFirestore(userId, email, pass, course)

                        // Call the function to store the timetable
                        storeTimetableInFirestore(userId, course)
                    }
                } else {
                    // User creation failed
                    handleException(null, "signup failed")
                }
            } catch (e: Exception) {
                // Exception occurred during the signup process
                handleException(e, "signup failed")
            } finally {
                // Set inProgress to false after signup attempt
                inProgress.value = false
            }
        }
    }


    // Function to handle user login
    fun login(email: String, pass: String) {
        inProgress.value = true

        // Sign in using Firebase authentication
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Login successful, set signedIn to true
                    signedIn.value = true
                    handleException(it.exception, "login successful")
                } else {
                    // Login failed
                    handleException(it.exception, "login failed")
                }
                // Set inProgress to false after login attempt
                inProgress.value = false
            }
    }

    // Function to handle exceptions and display error messages
    fun handleException(exception: Exception? = null, customMessage: String = "") {
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMsg else "$customMessage: $errorMsg"
        popupNotification.value = Event(message)
    }

    // Function to handle user logout
    fun logout() {
        auth.signOut()
        signedIn.value = false
    }





    // Function to store user information in Firestore
    private suspend fun storeUserInFirestore(userId: String, email: String, pass: String, course: String) {
        try {
            val user = hashMapOf(
                "email" to email,
                "password" to pass,
                "course" to course // Add the course information
            )

            // Set user information in Firestore under the "users" collection
            fireStore.collection("users")
                .document(userId)
                .set(user)
                .await()

            // Call the function to store the timetable
            storeTimetableInFirestore(userId, course)

        } catch (e: Exception) {
            handleException(e, "Failed to store user information (included password) in Firestore")
        }
    }

    // Function to store timetable information in Firestore
    private suspend fun storeTimetableInFirestore(userId: String, course: String) {
        try {
            val mondayClasses = when (course) {
                "Internet Systems Development" -> listOf(
                    "Advanced Web Techniques 15B25, Carol Rainsford, 9:00AM - 11:00AM",
                    "Mobile Application Development 1A15, Ita Kavanagh, 11:00AM - 1:00PM",
                    "Advanced Web Techniques 15B25, Carol Rainsford, 2:00PM - 4:00PM")


                "Software Development" -> listOf(
                    "Applications Programming 3B04, Alan Ryan, 12:00PM - 1:00PM",
                    "Secure Web App Development 1A15, Sharon Byrne, 2:00PM - 4:00PM",
                    "Applications Programming 8A106, Alan Ryan, 4:00PM - 6:00PM")
                else -> emptyList()
            }

            val tuesdayClasses = when (course) {
                "Internet Systems Development" -> listOf(
                    "Mobile and Web Computing Project 15B26, Suzanne O'Gorman, 9:00AM - 11:00AM",
                    "Data Structures and Algorithms 15B25, Caroline McAlister, 11:00AM - 1:00PM",
                    "Mobile and Web Computing Project 15B25, Suzanne O'Gorman, 1:00PM - 2:00PM",
                    "Concurrent Programming 15B25, Alan Ryan, 2:00PM - 4:00PM")



                "Software Development" -> listOf(
                    "Computer Science 3B03, James Fennell, 9:00AM - 11:00AM",
                    "Computer Science 1A17, James Fennell, 11:00AM - 12:00AM",
                    "Object Modelling and Design 3B05, Brendan Watson, 1:00PM - 2:00PM",
                    "Sd3 - Software Development Group Project(1Hr) 8A106, Des O'Carroll, 2:00PM - 4:00PM")
                else -> emptyList()
            }

            val wednesdayClasses = when (course) {
                "Internet Systems Development" -> listOf(
                    "Mobile Application Development 1A15, Ita Kavanagh, 11:00AM - 1:00PM",
                    "Concurrent Programming 15B25, Alan Ryan, 4:00PM - 6:00PM")



                "Software Development" -> listOf(
                    "Secure Web App Development 1A16, Sharon Byrne, 9:00AM - 11:00AM",
                    "Application Programming 8A106, Alan Ryan, 11:00AM - 1:00PM",
                    "Object Modelling and Design 3B04, Brendan Watson, 2:00PM - 4:00PM")
                else -> emptyList()
            }

            val thursdayClasses = when (course) {
                "Internet Systems Development" -> listOf(
                    "Off")



                "Software Development" -> listOf(
                    "Object Modelling and Design 8A106, Brendan Watson, 9:00AM - 11:00AM",
                    "Sd3 - Software Development Group Project(1Hr) 8A106, William Ward, 1:00PM - 2:00PM",
                    "Computer Science 3B05, James Fennell, 4:00PM - 5:00PM")
                else -> emptyList()
            }

            val fridayClasses = when (course) {
                "Internet Systems Development" -> listOf(
                    "Advanced Web Techniques 15B25, Carol Rainsford, 9:00AM - 11:00AM",
                    "Data Structures and Algorithms 15B25, Caroline McAlister, 11:00AM - 1:00PM",
                    "Data Structures and Algorithms 15B25, Caroline McAlister, 1:00PM - 2:00PM")



                "Software Development" -> listOf("Off")
                else -> emptyList()
            }




            // Create a timetable instance
            val timetable = Timetable(
                monday = mondayClasses,
                tuesday = tuesdayClasses,
                wednesday = wednesdayClasses,
                thursday = thursdayClasses,
                friday = fridayClasses
                // Repeat for other days
            )

            // Store the timetable in Firestore
            fireStore.collection("users")
                .document(userId)
                .collection("timetables")
                .document(course)
                .set(timetable)
                .await()
        } catch (e: Exception) {
            handleException(e, "Failed to store timetable in Firestore")
        }
    }


    // Function to get the selected course from Firestore
    suspend fun getCourse(userId: String): String? {
        return try {
            val userDocumentSnapshot = fireStore.collection("users")
                .document(userId)
                .get()
                .await()

            if (userDocumentSnapshot.exists()) {
                val userData = userDocumentSnapshot.data
                userData?.get("course") as? String
            } else {
                null
            }
        } catch (e: Exception) {
            handleException(e, "Failed to fetch user course")
            null
        }
    }


    // Function to get timetable data from Firestore based on the selected course
    suspend fun getTimetable(userId: String, course: String): Map<String, List<String>>? {
        return try {
            val timetableDocumentSnapshot = fireStore.collection("users")
                .document(userId)
                .collection("timetables")
                .document(course)
                .get()
                .await()

            if (timetableDocumentSnapshot.exists()) {
                // Document exists, extract the timetable data
                val timetableData = timetableDocumentSnapshot.data
                val monday = timetableData?.get("monday") as? List<String>
                val tuesday = timetableData?.get("tuesday") as? List<String>
                val wednesday = timetableData?.get("wednesday") as? List<String>
                val thursday = timetableData?.get("thursday") as? List<String>
                val friday = timetableData?.get("friday") as? List<String>

                // Create a map with day names as keys and class lists as values
                mapOf(
                    "Monday" to monday.orEmpty(),
                    "Tuesday" to tuesday.orEmpty(),
                    "Wednesday" to wednesday.orEmpty(),
                    "Thursday" to thursday.orEmpty(),
                    "Friday" to friday.orEmpty()
                )
            } else {
                // Timetable data doesn't exist for the user
                null
            }
        } catch (e: Exception) {
            handleException(e, "Failed to fetch timetable")
            null
        }
    }





    // Mutable state variables for user contact information
    var userName by mutableStateOf("")
    var userEmail by mutableStateOf("")
    var userMessage by mutableStateOf("")


    // Function to store contact information in Firestore
    suspend fun storeContactInFirestore(name: String, email: String, message: String) {
        try {
            val contact = hashMapOf(
                "name" to name,
                "email" to email,
                "message" to message
            )

            // Store contact information in Firestore under the "contacts" collection
            fireStore.collection("contacts")
                .add(contact)
                .await()

        } catch (e: Exception) {
            handleException(e, "Failed to store contact information in Firestore")
        }
    }





}