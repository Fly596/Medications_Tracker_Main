package com.galeria.medicationstracker.feature_auth.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.feature_auth.domain.UserDomain
import com.galeria.medicationstracker.feature_auth.domain.repository.AuthRepository
import com.galeria.medicationstracker.utils.ResourceRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Instant

data class RegistrationUiState(
    val name: String = "",
    val birthDate: Instant = Clock.System.now(),
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val showDatePicker: Boolean = false,
    val isLoading: Boolean = false,
)

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repository: AuthRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState = _uiState.asStateFlow()

    fun onRegisterClick() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            when (val registrationResult =
                repository.register(_uiState.value.email, _uiState.value.password)) {
                is ResourceRes.Error -> {
                    // TODO: show error.
                }
                is ResourceRes.Success -> {
                    val firebaseUser = registrationResult.data
                    val uid = firebaseUser.uid

                    val newUser =
                        UserDomain(
                            id = uid,
                            name = _uiState.value.name,
                            email = _uiState.value.email,
                            weightKg = 0.0,
                            heightCm = 0.0,
                            dateOfBirth = _uiState.value.birthDate)

                    val saveResult = repository.addUser(newUser)

                    if (saveResult is ResourceRes.Success) {
                        // TODO: navigate to main screen.
                    } else {
                        // error saving files.
                    }
                }

                else -> {}
            }
        }
    }
}
