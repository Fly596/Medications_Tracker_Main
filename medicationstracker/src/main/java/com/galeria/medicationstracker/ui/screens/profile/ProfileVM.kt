package com.galeria.medicationstracker.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.repository.NewIntakeRepository
import com.galeria.medicationstracker.data.repository.NewUserRepository
import com.galeria.medicationstracker.data.source.network.OLDAuthRepository
import com.galeria.medicationstracker.data.source.network.NetworkIntake
import com.galeria.medicationstracker.data.source.network.NetworkMedication
import com.galeria.medicationstracker.data.source.network.NetworkUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ? TODO: decide what to show on screens.
data class ProfileScreenUiState(
    val networkUser: NetworkUser? = null,
    val age: Int = 0,
    val weight: Float = 0.0f,
    val height: Float = 0.0f,
    val name: String = "",
    val intakes: List<NetworkIntake> = emptyList(),
    val medications: List<NetworkMedication> = emptyList(),
    val errorMessage: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)

@HiltViewModel
class ProfileVM
@Inject
constructor(
    private val userRepository: NewUserRepository,
    private val OLDAuthRepository: OLDAuthRepository,
    private val intakeRepository: NewIntakeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // fetchUserIntakes()

        viewModelScope.launch {
            try {
                val uid = OLDAuthRepository.getUserId().getOrThrow()
                fetchUserIntakes(uid.toString())
                // val resultUserData = userRepository.getUserData(uid.toString()).getOrThrow()
                /*  intakeRepository.observeUserIntakes(uid.toString()).collect() {
                    _uiState.update { it.copy(intakes = it.intakes) }
                } */
                // _uiState.update { it.copy(networkUser = resultUserData) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "ERROR: ${e.message}") }
            }
        }
    }
    
    fun fetchUserIntakes(uid: String) {
        viewModelScope.launch {
            intakeRepository.observeUserIntakes(uid.toString())
                .collect { intks ->
                    Log.d("ProfileVM", "Got intakes: $intks")
                    _uiState.update { it.copy(intakes = intks) }
                }
            /*  try {
                val uid = authRepository.getUserId().getOrNull()


                // _uiState.update { it.copy(intakes = resultIntakes) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "ERROR: ${e.message}") }
            } */
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
