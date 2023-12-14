package com.example.myapplication

import android.app.usage.UsageEvents.Event
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import com.example.myapplication.Timetable
import javax.inject.Inject


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
    fun onSignup(email: String, pass: String) {
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
                        storeUserInFirestore(userId, email,pass)

                        // Call the function to store the timetable
                        storeTimetableInFirestore(userId)
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
    private suspend fun storeUserInFirestore(userId: String, email: String, pass: String) {
        try {
            val user = hashMapOf(
                "email" to email,
                "password" to pass
            )

            // Set user information in Firestore under the "users" collection
            fireStore.collection("users")
                .document(userId)
                .set(user)
                .await()

            Log.d(TAG, "User information (included password) stored in Firestore")
        } catch (e: Exception) {
            handleException(e, "Failed to store user information (included password) in Firestore")
        }
    }

    // Function to store timetable information in Firestore
    private suspend fun storeTimetableInFirestore(userId: String) {
        try {
            // Create a timetable instance (replace this with your actual timetable data)
            val timetable = Timetable(
                monday = listOf("Class 1", "Class 2", "Class 3"),
                tuesday = listOf("Class 1", "Class 2"),
                wednesday = listOf("Class 1", "Class 2", "Class 3"),
                thursday = listOf("Class 1", "Class 2"),
                friday = listOf("Class 1", "Class 2", "Class 3")
            )

            // Store the timetable in Firestore
            fireStore.collection("users")
                .document(userId)
                .collection("timetables")
                .document("timetableDocument") // You can use a more specific document ID
                .set(timetable)
                .await()

            Log.d(TAG, "Timetable stored in Firestore")
        } catch (e: Exception) {
            handleException(e, "Failed to store timetable in Firestore")
        }
    }


    // Function to get timetable data from Firestore
    suspend fun getTimetable(userId: String): Map<String, List<String>>? {
        return try {
            val timetableDocumentSnapshot = fireStore.collection("users")
                .document(userId)
                .collection("timetables")
                .document("timetableDocument")
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

            Log.d(TAG, "Contact information stored in Firestore")
        } catch (e: Exception) {
            handleException(e, "Failed to store contact information in Firestore")
        }
    }


    companion object {
        private const val TAG = "IgViewModel"
    }




}