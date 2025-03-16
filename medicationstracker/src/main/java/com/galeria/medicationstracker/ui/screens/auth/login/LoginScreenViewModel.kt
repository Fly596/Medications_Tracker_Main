package com.galeria.medicationstracker.ui.screens.auth.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.SnackbarController
import com.galeria.medicationstracker.SnackbarEvent
import com.galeria.medicationstracker.data.UserType
import com.galeria.medicationstracker.utils.FirestoreFunctions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class LoginScreenState(
    val email: String = "tom@gmail.com",
    val emailError: String? = null,
    val password: String = "tomtom",
    val passwordError: String? = null,
    val showPassword: Boolean = false,
    val userType: UserType = UserType.PATIENT,
)

class LoginScreenViewModel : ViewModel() {

    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val loginScreenState = _loginScreenState.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirestoreFunctions.FirestoreService.db
    private var currentUserType = MutableStateFlow<String?>(null)
    var userType = currentUserType.asStateFlow()

    // запрашивает тип юзера.
    fun getUserType() {
        val userEmail = firebaseAuth.currentUser?.email

        viewModelScope.launch {
            if (userEmail == null) {
                currentUserType.value = null
                return@launch
            }

            try {
                val snapshot = db.collection("User").document(userEmail).get().await()

                if (snapshot.exists()) {
                    currentUserType.value = snapshot.data?.get("type") as? String
                } else {
                    currentUserType.value = null
                }
            } catch (exception: FirebaseFirestoreException) {
                Log.e("MainViewModel", "Error getting user type", exception)
            }
        }
    }

    private fun validateEmail(): Boolean {
        val emailInput = _loginScreenState.value.email.trim()
        var isValid = true
        var errorMessage = ""

        if (emailInput.isBlank() || emailInput.isEmpty()) {
            errorMessage = "Email cannot be empty"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            errorMessage = "Wrong email format"
            isValid = false
        }

        _loginScreenState.value = _loginScreenState.value.copy(emailError = errorMessage)
        return isValid
    }

    private fun validatePassword(): Boolean {
        val passwordInput = _loginScreenState.value.password
        var isValid = true
        var errorMessage = ""

        if (passwordInput.isBlank() || passwordInput.isEmpty()) {
            errorMessage = "Password cannot be empty"
            isValid = false
        } else if (passwordInput.length < 6) {
            errorMessage = "Password must be at least 6 characters"
            isValid = false
        }

        _loginScreenState.value = _loginScreenState.value.copy(passwordError = errorMessage)
        return isValid
    }

    fun onSignInClick(email: String, password: String, onLoginClick: (userType: UserType) -> Unit) {
        viewModelScope.launch {
            val isEmailValid = validateEmail()
            val isPasswordValid = validatePassword()

            if (isEmailValid && isPasswordValid) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    task ->
                    if (task.isSuccessful) {
                        val userId = task.result?.user?.uid

                        if (userId != null) {
                            val dataBase = FirestoreFunctions.FirestoreService.db

                            dataBase
                                .collection("User")
                                .document(email)
                                .get()
                                .addOnSuccessListener { snapshot ->
                                    if (snapshot.exists()) {
                                        val userTypeString = snapshot.getString("type")

                                        if (userTypeString != null) {
                                            val docUserType =
                                                UserType.valueOf(userTypeString.uppercase())
                                            _loginScreenState.value =
                                                _loginScreenState.value.copy(userType = docUserType)
                                            onLoginClick.invoke(docUserType)

                                            viewModelScope.launch {
                                                SnackbarController.sendEvent(
                                                    SnackbarEvent("Login Successful!")
                                                )
                                            }
                                        } else {
                                            viewModelScope.launch {
                                                SnackbarController.sendEvent(
                                                    event = SnackbarEvent("User data not found.")
                                                )
                                            }
                                        }
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    viewModelScope.launch {
                                        SnackbarController.sendEvent(
                                            event =
                                                SnackbarEvent(
                                                    "Error fetching user data: ${exception.message}"
                                                )
                                        )
                                    }
                                }
                        } else {
                            viewModelScope.launch {
                                SnackbarController.sendEvent(
                                    event = SnackbarEvent("User ID is null. Login failed.")
                                )
                            }
                        }
                    } else {
                        val errorMessage =
                            when (task.exception) {
                                is FirebaseAuthInvalidUserException -> "Invalid email or password."
                                is FirebaseAuthInvalidCredentialsException -> "Invalid password."
                                else -> "Authentication failed: ${task.exception?.message}"
                            }

                        viewModelScope.launch {
                            SnackbarController.sendEvent(
                                event = SnackbarEvent(message = errorMessage)
                            )
                        }
                    }
                }
            } else {
                SnackbarController.sendEvent(
                    event = SnackbarEvent(message = "Invalid email or password.")
                )
            }
        }
    }

    fun updateEmail(input: String) {
        _loginScreenState.value = _loginScreenState.value.copy(email = input)
    }

    fun updatePassword(input: String) {
        _loginScreenState.value = _loginScreenState.value.copy(password = input)
    }

    fun isShowPasswordChecked(input: Boolean) {
        _loginScreenState.value = _loginScreenState.value.copy(showPassword = !input)
    }
}
