package com.galeria.medicationstracker.ui.screens.dashboard.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.NewIntakeRepository
import com.galeria.medicationstracker.data.network.NetworkIntake
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class IntakeRecordsUiState(val intakes: List<NetworkIntake> = emptyList())

@HiltViewModel
class IntakeRecordsVM
@Inject
constructor(
    private val intakesRepository: NewIntakeRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    
    private var _uiState = MutableStateFlow(IntakeRecordsUiState())
    val uiState = _uiState.asStateFlow()
    
    init {
        fetchUserIntakes()
    }
    
    private fun fetchUserIntakes() {
        viewModelScope.launch {
            val uid = authRepository.getUserId().getOrThrow()
            
            intakesRepository
                .observeUserIntakes(userId = uid.toString())
                .collect { intakesList ->
                    if (intakesList.isNotEmpty()) {
                        _uiState.update { it.copy(intakes = intakesList) }
                    }
                }
            /* db.collection("User")
            .document("${FirebaseAuth.getInstance().currentUser?.email}")
            .collection("intakes")
            .orderBy("dateTime", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val intakes = result.toObjects(NewUserIntake::class.java)

                _uiState.value = _uiState.value.copy(intakes = intakes)
            }
            .addOnFailureListener { exp ->
                println("Error fetching intakes: ${exp.message}")
            } */
        }
    }
}
