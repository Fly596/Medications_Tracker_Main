package com.galeria.medicationstracker.ui.screens.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.IntakeStatus
import com.galeria.medicationstracker.data.NewIntakeRepository
import com.galeria.medicationstracker.data.NewMedicationRepository
import com.galeria.medicationstracker.data.NewUserIntake
import com.galeria.medicationstracker.data.NewUserMedication
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService.db
import com.galeria.medicationstracker.utils.toTimestamp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = true, // Добавим состояние загрузки
    val errorMessage: String? = null, // Для отображения ошибок
    val currentTakenMedications: List<NewUserMedication> = emptyList(),
)

data class AddIntakeUiState(
    val selectedMedication: NewUserMedication? = null,
    val selectedMedicationId: String = "",
    val status: IntakeStatus = IntakeStatus.PENDING,
    val selectedIntakeTime: LocalTime = LocalTime.now(),
)

@HiltViewModel
class DashboardVM
@Inject
constructor(
    private val intakeRepository: NewIntakeRepository,
    private val medicationRepository: NewMedicationRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    private val _intakeInputState = MutableStateFlow(AddIntakeUiState())
    val intakeInputState: StateFlow<AddIntakeUiState> =
        _intakeInputState.asStateFlow()
    // private val _currentUserId = MutableStateFlow<String?>(null)
    // val currentUserId = _currentUserId.asStateFlow()
    // private lateinit var currentUserId: String

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // get user id
                val uid = authRepository.getUserId().getOrThrow()
                
                getCurrentMedications(uid.toString())
                
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "ERROR: ${e.message}",
                    )
                }
            }
            _uiState.update { it.copy(isLoading = false) }
            
        }
    }
    
    // Фильтрация лекарств, прием которых окончен для использования при выводе
    // на главный экран.
    private fun getCurrentMedications(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val intakes =
                medicationRepository.getTodaysIntakes(userId)
                    .collect { intakesList ->
                        if (intakesList.isEmpty()) {
                            //
                        } else {
                            _uiState.update {
                                it.copy(currentTakenMedications = intakesList)
                            }
                        }
                    }
        }
    }

    fun addNewIntake(
        intakeTime: Timestamp = Timestamp.now(),
        medication: NewUserMedication = NewUserMedication(),
        status: IntakeStatus,
    ) {
        viewModelScope.launch {
            val uid = authRepository.getUserId().getOrThrow()
            val intake: NewUserIntake =
                NewUserIntake(
                    userId = uid.toString(),
                    medicationId = medication.id,
                    status = status.name,
                    presetTime = medication.intakeTime,
                    timestamp = intakeTime
                )
            intakeRepository.addUserIntake(uid.toString(), intake)
        }
    }

    // Проверка на то, был ли сегодня прием или нет.
    // -1: error; 0: noData, 1: skipped, 2: taken
    // ! перегести в репо.
    suspend fun fetchIntakeStatus(medication: NewUserMedication): Int {
        val uid = authRepository.getUserId().getOrThrow()
        
        val todayStart = LocalDate.now().atStartOfDay().toTimestamp()
        val todayEnd = LocalDate.now().plusDays(1).atStartOfDay().toTimestamp()
        return try {
            val querySnapshot =
                db.collection("User")
                    .document(uid.toString())
                    .collection("intakes")
                    .whereEqualTo("medicationId", medication.id)
                    .whereGreaterThanOrEqualTo("timestamp", todayStart)
                    .whereLessThan("timestamp", todayEnd)
                    .limit(1)
                    .get(Source.SERVER)
                    .await()
            
            if (!querySnapshot.isEmpty) {
                if (
                    querySnapshot
                        .toObjects(NewUserIntake::class.java)[0]
                        .status == "TAKEN"
                )
                    2
                else 1
            } else {
                0
            }
        } catch (e: Exception) {
            Log.e("checkIntake", "Error fetching intake data", e)
            -1
        }
    }
    
    fun setSelectedMedication(value: NewUserMedication) {
        _intakeInputState.update { it.copy(selectedMedication = value) }
    }
    
    fun onTimeSelected(time: LocalTime) {
        _intakeInputState.update { it.copy(selectedIntakeTime = time) }
    }
}
