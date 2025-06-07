package com.galeria.medicationstracker.ui.screens.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginScreenState(
    val email: String = "tom@gmail.com",
    val emailError: String? = null,
    val password: String = "tomtom",
    val passwordError: String? = null,
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val generalError: String? = null, // Для общих ошибок от репозитория
)

@HiltViewModel
class LoginScreenViewModel @Inject constructor(private val repository: AuthRepository) :
    ViewModel() {

    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val loginScreenState: StateFlow<LoginScreenState> = _loginScreenState.asStateFlow()
    private val _loginSuccessEvent = MutableSharedFlow<Unit>()
    val loginSuccessEvent: SharedFlow<Unit> = _loginSuccessEvent.asSharedFlow()

    fun onSignInClick(onLoginClick: () -> Unit) {
        val email = _loginScreenState.value.email
        val password = _loginScreenState.value.password

        if (email.isBlank()) {
            _loginScreenState.update { it.copy(emailError = "Email cannot be empty") }
            return
        }
        if (password.isBlank()) {
            _loginScreenState.update { it.copy(passwordError = "Password cannot be empty") }
            return
        }

        viewModelScope.launch {
            _loginScreenState.update { it.copy(isLoading = true, generalError = null) }
            val result = repository.signIn(email, password)

            result.fold(
                onSuccess = {
                    // Успех!
                    _loginScreenState.update { it.copy(isLoading = false) }
                    _loginSuccessEvent.emit(Unit)
                },
                onFailure = { exception ->
                    _loginScreenState.update {
                        it.copy(
                            isLoading = false,
                            generalError = exception.message ?: "Login failed. Please try again.",
                        )
                    }
                },
            )
            /*             val isEmailValid = validateEmail()
            val isPasswordValid = validatePassword()

            if (isEmailValid && isPasswordValid) {
                if (repository.signIn(email, password).isSuccess) {
                    onLoginClick()
                }
                 */
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
            /*
            } else {
                SnackbarController.sendEvent(
                    event = SnackbarEvent(message = "Invalid email or password.")
                )
            } */
        }
    }

    fun updateEmail(input: String) {
        _loginScreenState.update { it.copy(email = input, emailError = null, generalError = null) }
        _loginScreenState.value = _loginScreenState.value.copy(email = input)
    }

    fun updatePassword(input: String) {
        _loginScreenState.update {
            it.copy(password = input, passwordError = null, generalError = null)
        }
    }

    fun isShowPasswordChecked(input: Boolean) {
        _loginScreenState.update { it.copy(showPassword = !input) }
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
