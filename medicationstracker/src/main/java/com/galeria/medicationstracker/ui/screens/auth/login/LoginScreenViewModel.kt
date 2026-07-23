package com.galeria.medicationstracker.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginScreenState(
  val email: String = "fly.yt.77@gmail.com",
  val emailError: String? = null,
  val password: String = "666666",
  val passwordError: String? = null,
  val showPassword: Boolean = false,
  val isLoading: Boolean = false,
  val generalError: String? = null, // Для общих ошибок от репозитория
)

@HiltViewModel
class LoginScreenViewModel
@Inject constructor(private val repository: AuthRepository) :
  ViewModel() {

  private val _uiState = MutableStateFlow(LoginScreenState())
  val uiState = _uiState.asStateFlow()
  private val _loginSuccessEvent = MutableSharedFlow<Unit>()
  val loginSuccessEvent: SharedFlow<Unit> = _loginSuccessEvent.asSharedFlow()

  fun onSignInClick(onLoginClick: () -> Unit) {
    val email = _uiState.value.email
    val password = _uiState.value.password

    if (email.isBlank()) {
      _uiState.update { it.copy(emailError = "Email cannot be empty") }
      return
    }
    if (password.isBlank()) {
      _uiState.update { it.copy(passwordError = "Password cannot be empty") }
      return
    }

    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true, generalError = null) }
      repository.signIn(email, password)
        .onSuccess {
          _uiState.update { it.copy(isLoading = false) }
          _loginSuccessEvent.emit(Unit)
        }
        .onFailure { exception ->
          _uiState.update {
            it.copy(
              isLoading = false,
              generalError = exception.message ?: "Login failed. Please try again.",
            )
          }
        }

      //val result = repository.signIn(email, password)
      /*      result.fold(
              onSuccess = {
                // Успех!
                _uiState.update { it.copy(isLoading = false) }
                _loginSuccessEvent.emit(Unit)
              },
              onFailure = { exception ->
                _uiState.update {
                  it.copy(
                    isLoading = false,
                    generalError = exception.message ?: "Login failed. Please try again.",
                  )
                }
              },
            )*/
    }
  }

  fun updateEmail(input: String) {
    _uiState.update { it.copy(email = input, emailError = null, generalError = null) }
  }

  fun updatePassword(input: String) {
    _uiState.update {
      it.copy(password = input, passwordError = null, generalError = null)
    }
  }

  fun isShowPasswordChecked(input: Boolean) {
    _uiState.update { it.copy(showPassword = !input) }
  }
}
