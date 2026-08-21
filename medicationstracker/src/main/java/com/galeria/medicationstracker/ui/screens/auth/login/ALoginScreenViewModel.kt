package com.galeria.medicationstracker.ui.screens.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.core.domain.repository._AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ALoginUiEffect {
  data object NavigateToRegistration : ALoginUiEffect
  data object NavigateToResetPassword : ALoginUiEffect
  data object NavigateToHome : ALoginUiEffect

  data class ShowSnackbar(val message: String) : ALoginUiEffect
}

data class ALoginScreenState(
  val email: String = "fly.yt.77@gmail.com",
  val emailError: String? = null,
  val password: String = "666666",
  val passwordError: String? = null,
  val showPassword: Boolean = false,
  val isLoading: Boolean = false,
  val generalError: String? = null,
)

@HiltViewModel
class ALoginScreenViewModel
@Inject constructor(
  private val repository: _AuthRepository
) : ViewModel() {

  // 1. Создаем приватный буферизированный канал.
  private val _effectChannel = Channel<ALoginUiEffect>(Channel.BUFFERED)

  // 2. Превращаем в Flow для безопасного сбора на стороне UI.
  val effectFlow = _effectChannel.receiveAsFlow()
  private val _uiState = MutableStateFlow(ALoginScreenState())
  val uiState = _uiState.asStateFlow()

  fun onLoginClick() {

    val email = _uiState.value.email
    val password = _uiState.value.password

    viewModelScope.launch {
      if (!validateInputs()) {
        // Отправляем эффект показа ошибки.
        _effectChannel.send(ALoginUiEffect.ShowSnackbar("Fill all fields"))
      }

      repository.signIn(email, password)
        .onSuccess {
          _effectChannel.send(ALoginUiEffect.NavigateToHome)
          _uiState.update { it.copy(isLoading = false) }

        }
        .onFailure { exception ->
          _effectChannel.send(
            ALoginUiEffect.ShowSnackbar(
              exception.message ?: "Login failed. Please try again."
            )
          )
          _uiState.update {
            it.copy(
              isLoading = false,
              generalError = exception.message ?: "Login failed. Please try again.",
            )
          }
        }
    }
  }

  private fun validateInputs(): Boolean {
    val email = _uiState.value.email
    val password = _uiState.value.password
    var isValid = true

    if (email.isBlank()) {
      _uiState.update { it.copy(emailError = "Email cannot be empty") }
      isValid = false
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      _uiState.update { it.copy(emailError = "Invalid email format") }
      isValid = false
    }

    if (password.isBlank()) {
      _uiState.update { it.copy(passwordError = "Password cannot be empty") }
      isValid = false
    } else if (password.length < 6) {
      _uiState.update { it.copy(passwordError = "Password must be at least 6 characters") }
      isValid = false
    }

    return isValid
  }
}