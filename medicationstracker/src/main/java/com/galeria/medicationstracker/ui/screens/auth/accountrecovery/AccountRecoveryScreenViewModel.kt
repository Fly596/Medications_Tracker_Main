package com.galeria.medicationstracker.ui.screens.auth.accountrecovery

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.SnackbarController
import com.galeria.medicationstracker.SnackbarEvent
import com.galeria.medicationstracker.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountRecoveryScreenState(val email: String = "", val emailError: String? = null)

@HiltViewModel
class AccountRecoveryScreenViewModel
@Inject
constructor(private val authRepository: AuthRepository) : ViewModel() {

    // val auth = FirebaseAuth.getInstance()
    private val _uiState = MutableStateFlow(AccountRecoveryScreenState())
    val uiState = _uiState.asStateFlow()

    fun updateEmail(input: String) {
        _uiState.value = _uiState.value.copy(email = input)
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

        _uiState.value = _uiState.value.copy(emailError = errorMessage)
        return isValid
    }

    fun resetPassword(email: String) {
        val isEmailValid = validateEmail()

        viewModelScope.launch {
            if (isEmailValid) {
                authRepository
                    .resetPassword(email)
                    .onSuccess {
                        // Password reset email sent successfully
                        viewModelScope.launch {
                            SnackbarController.sendEvent(
                                event = SnackbarEvent("Password reset email sent!")
                            )
                        }
                    }
                    .onFailure { exception ->
                        // Error sending password reset email
                        viewModelScope.launch {
                            SnackbarController.sendEvent(
                                event =
                                    SnackbarEvent(
                                        "Error sending password reset email: ${exception.message}"
                                    )
                            )
                        }
                    }
            }
        }
    }
}
