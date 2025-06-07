package com.galeria.medicationstracker.ui.screens.auth.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.NewUser
import com.galeria.medicationstracker.data.NewUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignupScreenState(
    val name: String = "",
    val email: String = "",
    val emailErrorMessage: String? = null,
    val password: String = "",
    val passwordErrorMessage: String? = null,
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val generalError: String? = null,
)

@HiltViewModel
class SignupScreenViewModel
@Inject
constructor(private val repository: AuthRepository, private val userRepo: NewUserRepository) :
    ViewModel() {

    // val auth = FirebaseAuth.getInstance()
    private val _uiState = MutableStateFlow(SignupScreenState())
    val uiState = _uiState.asStateFlow()
    private val _signupSuccessEvent = MutableSharedFlow<Unit>()
    val signupSuccessEvent: SharedFlow<Unit> = _signupSuccessEvent.asSharedFlow()

    // private val db = FirestoreFunctions.FirestoreService.db
    fun onRegisterClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            val result = repository.signUp(_uiState.value.email, _uiState.value.password)

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                    val user: NewUser =
                        NewUser(name = _uiState.value.name, email = _uiState.value.email)
                    userRepo.addUser(user)
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            generalError = exception.message ?: "Signup failed.",
                        )
                    }
                },
            )
        }
    }

    /*     fun onRegisterClick() {
        viewModelScope.launch {
            val isEmailValid = validateEmail()
            val isPasswordValid = validatePassword()

            if (isEmailValid && isPasswordValid) {
                repository.signUp(_uiState.value.email, _uiState.value.password)
            } else {
                viewModelScope.launch {
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(message = "Invalid email or password.")
                    )
                }
            }
        }

    } */

    fun addUserData() {
        viewModelScope.launch {
            val newUser = NewUser(name = _uiState.value.name, email = _uiState.value.email)
            userRepo.addUser(newUser)
        }
        /*         db.collection("User")
        .document(newUser.email.toString())
        .set(newUser)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                viewModelScope.launch {
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(message = "Account Created!")
                    )
                }
            } else {
                viewModelScope.launch {
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(message = "Something went wrong :(")
                    )
                }
            }
        } */
    }

    fun updateUserName(input: String) {
        _uiState.value = _uiState.value.copy(name = input)
    }

    fun updateEmail(input: String) {
        _uiState.value = _uiState.value.copy(email = input)
    }

    fun updatePassword(input: String) {
        _uiState.value = _uiState.value.copy(password = input)
    }

    fun isShowPasswordChecked(input: Boolean) {
        _uiState.value = _uiState.value.copy(showPassword = !input)
    }

    private fun validateEmail(): Boolean {
        val emailInput = _uiState.value.email.trim()
        var isValid = true
        var errorMessage = ""

        if (emailInput.isBlank() || emailInput.isEmpty()) {
            errorMessage = "Email cannot be empty"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            errorMessage = "Wrong email format"
            isValid = false
        }

        _uiState.value = _uiState.value.copy(emailErrorMessage = errorMessage)
        return isValid
    }

    private fun validatePassword(): Boolean {
        val passwordInput = _uiState.value.password
        var isValid = true
        var errorMessage = ""

        if (passwordInput.isBlank() || passwordInput.isEmpty()) {
            errorMessage = "Password cannot be empty"
            isValid = false
        } else if (passwordInput.length < 6) {
            errorMessage = "Password must be at least 6 characters"
            isValid = false
        }

        _uiState.value = _uiState.value.copy(passwordErrorMessage = errorMessage)
        return isValid
    }
}
