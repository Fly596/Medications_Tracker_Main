package com.galeria.medicationstracker.ui.screens.profile.profiledetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.BloodType
import com.galeria.medicationstracker.data.UserProfile
import com.galeria.medicationstracker.data.UserRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileDetailsUiState(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val dateOfBirth: Timestamp? = null,
    val sex: String? = null,
    val bloodType: BloodType? = null,
    val weight: Float? = null,
    val height: Float? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileDetailsUiState())
    val state: StateFlow<ProfileDetailsUiState> = _state.asStateFlow()

    fun updateUser() {
        viewModelScope.launch {
            val user = state.value
            val newUser = UserProfile(
                firstName = user.firstName ?: "",
                lastName = user.lastName ?: "",
                email = user.email ?: "",
                weight = user.weight ?: 0f,
                height = user.height ?: 0f,
                dateOfBirth = user.dateOfBirth ?: Timestamp.now(),
                bloodType = user.bloodType ?: BloodType.UNKNOWN,
                sex = user.sex ?: "Unknown"
            )
            repository.updateUserData(newUser)
        }
    }

    fun getUserData() {
        viewModelScope.launch {
            repository.getUserData()
        }
    }

    fun updateFirstName(firstName: String) {
        _state.value = _state.value.copy(firstName = firstName)
    }

    fun updateLastName(lastName: String) {
        _state.value = _state.value.copy(lastName = lastName)
    }

    fun updateEmail(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun updateDateOfBirth(dateOfBirth: Timestamp?) {
        _state.value = _state.value.copy(dateOfBirth = dateOfBirth)

    }

    fun updateSex(sex: String) {
        _state.value = _state.value.copy(sex = sex)
    }

    fun updateBloodType(bloodType: BloodType) {
        _state.value = _state.value.copy(bloodType = bloodType)
    }

    fun updateWeight(weight: Float) {
        _state.value = _state.value.copy(weight = weight)
    }

    fun updateHeight(height: Float) {
        _state.value = _state.value.copy(height = height)
    }

    private fun updateLoading(isLoading: Boolean) {
        _state.value = _state.value.copy(isLoading = isLoading)
    }

    private fun updateError(isError: Boolean) {
        _state.value = _state.value.copy(isError = isError)
    }

}
