package com.galeria.medtracker2.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import com.galeria.medtracker2.feature.auth.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class LoginScreenState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val generalError: String? = null, // Для общих ошибок от репозитория
)

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AuthRepository) :
    ViewModel() {
    
    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()
    
    fun onSignInClick() {
        // TODO:
    }
    
    fun updateEmail(input: String) {
        _state.value = _state.value.copy(email = input)
    }
    
    fun updatePassword(input: String) {
        _state.value = _state.value.copy(password = input)
    }
    
    fun isShowPasswordChecked(input: Boolean) {
        _state.value = _state.value.copy(showPassword = !input)
    }
}
