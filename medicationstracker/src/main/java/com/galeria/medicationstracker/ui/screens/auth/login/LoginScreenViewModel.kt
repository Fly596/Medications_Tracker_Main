package com.galeria.medicationstracker.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginScreenState(
  val email: String = "tom@gmail.com",
  val password: String = "tomtom",
  val emailError: String? = null,
  val passwordError: String? = null,
  val showPassword: Boolean = false,
  val userType: UserType? = null,
)

@HiltViewModel
class LoginScreenViewModel @Inject constructor(private val authRepository: AuthRepository) :
  ViewModel() {

  private val _loginScreenState = MutableStateFlow(LoginScreenState())
  val loginScreenState = _loginScreenState.asStateFlow()

  fun signIn() {
    val email = _loginScreenState.value.email
    val password = _loginScreenState.value.password
    viewModelScope.launch { authRepository.signIn(email, password) }
  }

  /*   private suspend fun signIn(email: String, password: String): AuthResult =
  withContext(Dispatchers.IO) {
      firebaseAuth.signInWithEmailAndPassword(email, password).await()
  } */

  /*   private suspend fun fetchUserType(email: String): UserType? =
  withContext(Dispatchers.IO) {
    val snapshot = db.collection("User").document(email).get().await()

    snapshot.getString("type")?.let { UserType.valueOf(it.uppercase()) }
  } */

  /*
    private fun validateInputs() {
      val state = _loginScreenState.value
      _loginScreenState.value =
        state.copy(
          emailError = ValidateUtils.validateEmail(state.email),
          passwordError = ValidateUtils.validatePassword(state.password),
        )
    }
  */

  /*   fun onSignInClick(email: String, password: String, onLoginClick: (userType: UserType) -> Unit) {
    viewModelScope.launch {
      validateInputs()
      val state = _loginScreenState.value

      if (state.emailError == null || state.passwordError == null) {
        try {
          signIn(email, password)
          val userType = fetchUserType(email) ?: throw Exception("User type not found")
          onLoginClick(userType)
          SnackbarController.sendEvent(SnackbarEvent("Login Successful!"))
        } catch (e: Exception) {
          SnackbarController.sendEvent(SnackbarEvent("Invalid email or password."))
        }
      }
    }
  } */

  fun updateEmail(input: String) {
    _loginScreenState.value = _loginScreenState.value.copy(email = input)
  }

  fun updatePassword(input: String) {
    _loginScreenState.value = _loginScreenState.value.copy(password = input)
  }

  fun updateShowPassword(show: Boolean) {
    _loginScreenState.value = _loginScreenState.value.copy(showPassword = show)
  }
}
