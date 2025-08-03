package com.galeria.medicationstracker.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.network.AuthRepository
import com.galeria.medicationstracker.utils.AuthResult
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
    val loginScreenState: StateFlow<LoginScreenState> =
        _loginScreenState.asStateFlow()
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
            _loginScreenState.update {
                it.copy(
                    isLoading = true,
                    generalError = null
                )
            }
            val result = repository.signIn(email, password)
            if (result is AuthResult.Success) {
            }
            
            when (result) {
                is Result.Success -> {
                    // Успех!
                    _loginScreenState.update { it.copy(isLoading = false) }
                    _loginSuccessEvent.emit(Unit)
                }
                
                is AuthResult.AuthError -> TODO()
                AuthResult.NetworkError -> TODO()
                AuthResult.Success -> TODO()
                is AuthResult.UnknownError -> TODO()
                is AuthResult.ValidationError -> TODO()
            }
            
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
                            generalError = exception.message
                                ?: "Login failed. Please try again.",
                        )
                    }
                },
            )
        }
    }
    
    fun updateEmail(input: String) {
        _loginScreenState.update {
            it.copy(
                email = input,
                emailError = null,
                generalError = null
            )
        }
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
}
