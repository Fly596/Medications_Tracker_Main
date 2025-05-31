package com.galeria.medicationstracker.ui.screens.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.SnackbarController
import com.galeria.medicationstracker.SnackbarEvent
import com.galeria.medicationstracker.data.UserType
import com.galeria.medicationstracker.data.imp.AuthRepository
import com.galeria.medicationstracker.utils.FirestoreFunctions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginScreenState(
    val email: String = "tom@gmail.com",
    val emailError: String? = null,
    val password: String = "tomtom",
    val passwordError: String? = null,
    val showPassword: Boolean = false,
    val userType: UserType = UserType.PATIENT,
)

@HiltViewModel
class LoginScreenViewModel @Inject constructor(private val repository: AuthRepository) :
    ViewModel() {

    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val loginScreenState = _loginScreenState.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirestoreFunctions.FirestoreService.db
    private var currentUserType = MutableStateFlow<String?>(null)

    fun onSignInClick(email: String, password: String, onLoginClick: () -> Unit) {
        viewModelScope.launch {
            val isEmailValid = validateEmail()
            val isPasswordValid = validatePassword()

            if (isEmailValid && isPasswordValid) {
                if (
                    repository.signIn(email, password).isSuccess
                ) {
                    onLoginClick()
                }
                /*   firebaseAuth.signInWithEmailAndPassword(email, password)
                      .addOnCompleteListener { task ->
                          if (task.isSuccessful) {
                              onLoginClick()
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
                      } */
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
    
}
