package com.galeria.medicationstracker.ui.screens.auth.accountrecovery

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.galeria.medicationstracker.SnackbarController
import com.galeria.medicationstracker.SnackbarEvent
import com.galeria.medicationstracker.data.source.network.OLDAuthRepository
import com.galeria.medicationstracker.navigation.AuthScreen
import com.galeria.medicationstracker.utils.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResetPasswordScreenState(
    val email: String = "",
    val emailError: String? = null
)

@HiltViewModel
class ResetPasswordScreenViewModel
@Inject
constructor(
    private val OLDAuthRepository: OLDAuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ResetPasswordScreenState())
    val uiState = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            try {
                val args = savedStateHandle.toRoute<AuthScreen.Registration>()
                val inputEmail = args.email
                _uiState.update { it.copy(email = inputEmail) }
                
            } catch (e: Exception) {
            }
        }
    }
    
    
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
                viewModelScope.launch {
                    val result = OLDAuthRepository
                        .resetPassword(email)
                    when (result) {
                        is AuthResult.Success -> {
                            SnackbarController.sendEvent(
                                event = SnackbarEvent("Password reset email sent!")
                            )
                            
                        }
                        
                        is AuthResult.AuthError -> {
                            SnackbarController.sendEvent(
                                event =
                                    SnackbarEvent(
                                        "Error sending password reset email: ${result.message}"
                                    )
                            )
                        }
                        
                        is AuthResult.NetworkError -> {
                            SnackbarController.sendEvent(
                                event =
                                    SnackbarEvent(
                                        "Check your internet connection and try again."
                                    )
                            )
                        }
                        
                        is AuthResult.UnknownError -> {
                            SnackbarController.sendEvent(
                                event =
                                    SnackbarEvent(
                                        "Something went wrong. Please try again later."
                                    )
                            )
                        }
                        
                        is AuthResult.ValidationError -> {
                            SnackbarController.sendEvent(
                                event =
                                    SnackbarEvent(
                                        "Fill all the fields correctly."
                                    )
                            )
                        }
                    }
                }
                /*     .onSuccess {
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
                    } */
            }
        }
    }
}
