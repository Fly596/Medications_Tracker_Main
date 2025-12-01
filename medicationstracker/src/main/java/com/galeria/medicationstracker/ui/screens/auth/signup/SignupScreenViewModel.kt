package com.galeria.medicationstracker.ui.screens.auth.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.galeria.medicationstracker.data.repository.NewUserRepository
import com.galeria.medicationstracker.data.source.network.AuthRepository
import com.galeria.medicationstracker.data.source.network.NetworkUser
import com.galeria.medicationstracker.navigation.AuthScreen
import com.galeria.medicationstracker.utils.toTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
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
    val selectedBirthDate: LocalDate = LocalDate.now(),
    val showDatePicker: Boolean = false,
)

@HiltViewModel
class SignupScreenViewModel
@Inject
constructor(
    private val repository: AuthRepository,
    private val userRepo: NewUserRepository,
    savedStateHandle: SavedStateHandle,
) :
    ViewModel() {
    
    // val auth = FirebaseAuth.getInstance()
    private val _uiState = MutableStateFlow(SignupScreenState())
    val uiState = _uiState.asStateFlow()
    private val _signupSuccessEvent = MutableSharedFlow<Unit>()
    
    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            
            try {
                val args = savedStateHandle.toRoute<AuthScreen.Registration>()
                val inputEmail = args.email
                _uiState.update { it.copy(email = inputEmail) }
                
            } catch (e: Exception) {
            }
            _uiState.update { it.copy(isLoading = false, generalError = null) }
        }
    }
    
    fun onRegisterClick() {
        _uiState.update { it.copy(isLoading = true, generalError = null) }
        viewModelScope.launch {
            repository.signUp(_uiState.value.email, _uiState.value.password)
            
            val newUserId = repository.getUserId()
            val birthDateTimestamp =
                _uiState.value.selectedBirthDate.toTimestamp()
            val networkUser: NetworkUser =
                NetworkUser(
                    id = newUserId.getOrNull().toString(),
                    name = _uiState.value.name,
                    email = _uiState.value.email,
                    dateOfBirth = birthDateTimestamp,
                )
            userRepo.addUser(networkUser)
            _uiState.update { it.copy(isLoading = false, generalError = null) }
            
            
            // TODO: fold AUTHRESULT.
            /*  result.fold(
                 onSuccess = {
                     val newUserId = repository.getUserId()
                     val birthDateTimestamp =
                         _uiState.value.selectedBirthDate.toTimestamp()
                     
                     _uiState.update { it.copy(isLoading = false) }
                     val networkUser: NetworkUser =
                         NetworkUser(
                             id = newUserId.getOrNull().toString(),
                             name = _uiState.value.name,
                             email = _uiState.value.email,
                             dateOfBirth = birthDateTimestamp,
                         )
                     userRepo.addUser(networkUser)
                 },
                 onFailure = { exception ->
                     _uiState.update {
                         it.copy(
                             isLoading = false,
                             generalError = exception.message
                                 ?: "Signup failed.",
                         )
                     }
                 },
             ) */
        }
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
    
    fun showDatePicker() {
        _uiState.update { it.copy(showDatePicker = true) }
    }
    
    fun dismissDatePicker() {
        _uiState.update { it.copy(showDatePicker = false) }
    }
}
