package com.example.myapplication

import android.app.usage.UsageEvents.Event
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class IgViewModel @Inject constructor(
    val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : ViewModel() {

    val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val popupNotification = mutableStateOf<com.example.myapplication.Event<String>?>(null)


    fun onSignup(email: String, pass: String) {
        viewModelScope.launch {
            inProgress.value = true

            try {
                val result = auth.createUserWithEmailAndPassword(email, pass).await()

                if (result.user != null) {
                    signedIn.value = true
                    handleException(null, "signup successful")

                    // Store user information in Firestore
                    val userId = result.user?.uid
                    if (userId != null) {
                        storeUserInFirestore(userId, email)
                    }
                } else {
                    // User creation failed
                    handleException(null, "signup failed")
                }
            } catch (e: Exception) {
                // Exception occurred during the signup process
                handleException(e, "signup failed")
            } finally {
                inProgress.value = false
            }
        }
    }
    private suspend fun storeUserInFirestore(userId: String, email: String) {
        try {
            val user = hashMapOf(
                "email" to email
            )

            fireStore.collection("users")
                .document(userId)
                .set(user)
                .await()

            Log.d(TAG, "User information stored in Firestore")
        } catch (e: Exception) {
            handleException(e, "Failed to store user information in Firestore")
        }
    }

    companion object {
        private const val TAG = "IgViewModel"
    }


    fun login(email: String, pass: String) {
        inProgress.value = true

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    signedIn.value = true
                    handleException(it.exception, "login successful")
                } else {
                    handleException(it.exception, "login failed")
                }
                inProgress.value = false
            }
    }


    fun handleException(exception: Exception? = null, customMessage: String = "") {
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMsg else "$customMessage: $errorMsg"
        popupNotification.value = Event(message)
    }


    fun logout() {
        auth.signOut()
        signedIn.value = false
    }


}