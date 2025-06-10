package com.galeria.medicationstracker.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.NewUser
import com.galeria.medicationstracker.data.NewUserIntake
import com.galeria.medicationstracker.data.NewUserMedication
import com.galeria.medicationstracker.data.NewUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ? TODO: decide what to show on screens.
data class ProfileScreenUiState(
    val user: NewUser? = null,
    val age: Int = 0,
    val weight: Float = 0.0f,
    val height: Float = 0.0f,
    val name: String = "",
    val intakes: List<NewUserIntake> = emptyList(),
    val medications: List<NewUserMedication> = emptyList(),
    val errorMessage: String? = null,
)

@HiltViewModel
class ProfileVM
@Inject
constructor(
    private val userRepository: NewUserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val uid = authRepository.getUserId().getOrNull()
                val resultUserData =
                    userRepository.getUserData(uid.toString()).getOrThrow()
                
                _uiState.update { it.copy(user = resultUserData) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "ERROR: ${e.message}") }
            }
        }
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateAge(age: Int) {
        _uiState.value = _uiState.value.copy(age = age)
    }

    fun updateWeight(weight: Float) {
        _uiState.value = _uiState.value.copy(weight = weight)
    }

    fun updateHeight(height: Float) {
        _uiState.value = _uiState.value.copy(height = height)
    }
}
