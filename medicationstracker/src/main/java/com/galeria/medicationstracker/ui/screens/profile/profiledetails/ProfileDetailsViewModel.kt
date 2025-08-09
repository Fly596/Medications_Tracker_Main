package com.galeria.medicationstracker.ui.screens.profile.profiledetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.NewUserRepository
import com.galeria.medicationstracker.data.network.AuthRepository
import com.galeria.medicationstracker.data.network.NetworkUser
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
    val weight: Float? = null,
    val height: Float? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    private val repository: NewUserRepository,
    private val authRepository: AuthRepository,
) :
    ViewModel() {
    
    private val _state = MutableStateFlow(ProfileDetailsUiState())
    val state: StateFlow<ProfileDetailsUiState> = _state.asStateFlow()
    
    init {
        getUserData()
    }
    
    fun updateUser() {
        // TODO: в репо.
        /*         viewModelScope.launch {
            val user = state.value
            val newUser = UserProfile(
                firstName = user.firstName ?: "",
                lastName = user.lastName ?: "",
                email = user.email ?: "",
                weight = user.weight ?: 0f,
                height = user.height ?: 0f,
                dateOfBirth = user.dateOfBirth ?: Timestamp.now(),
                bloodType = user.bloodType ?: BloodType.UNKNOWN,
                sex = user.sex ?: "Unknown",
                uid = repository.getUserData().uid ?: ""
            )
            repository.updateUserData(newUser)
        } */
    }
    
    private fun getUserData() {
        // var networkUser = NetworkUser()
        viewModelScope.launch {
            val uid = authRepository.getUserId().getOrNull()
            val user = repository.getUserData(uid ?: "")
            user.fold(
                onSuccess = {
                    _state.value =
                        _state.value.copy(
                            firstName = it.name,
                            email = it.email,
                            dateOfBirth = it.dateOfBirth,
                            weight = it.weightKg,
                            height = it.heightCm,
                        )
                },
                onFailure = {
                    _state.value = _state.value.copy(
                        isError = true,
                        errorMessage = it.message
                    )
                }
            )
        }
        // viewModelScope.launch {
        //     user = repository.getUserData()
        //     _state.value =
        //         _state.value.copy(
        //             firstName = user.firstName,
        //             lastName = user.lastName,
        //             email = user.email,
        //             dateOfBirth = user.dateOfBirth,
        //             sex = user.sex,
        //             bloodType = user.bloodType,
        //             weight = user.weight,
        //             height = user.height,
        //         )
        // }
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
    
    
    fun updateWeight(weight: Float) {
        viewModelScope.launch {
            _state.value = _state.value.copy(weight = weight)
            updateUserProfile()
        }
    }
    
    fun updateHeight(height: Float) {
        _state.value = _state.value.copy(height = height)
    }
    
    fun updateUserProfile() {
        viewModelScope.launch {
            val uid = authRepository.getUserId().getOrNull()
            val user = NetworkUser(
                name = state.value.firstName ?: "",
                email = state.value.email ?: "",
                weightKg = state.value.weight ?: 0f,
                heightCm = state.value.height ?: 0f,
                dateOfBirth = state.value.dateOfBirth ?: Timestamp.now(),
            )
            repository.updateUser(user, uid ?: "")
        }
    }
    
    private fun updateLoading(isLoading: Boolean) {
        _state.value = _state.value.copy(isLoading = isLoading)
    }
    
    private fun updateError(isError: Boolean) {
        _state.value = _state.value.copy(isError = isError)
    }
}
