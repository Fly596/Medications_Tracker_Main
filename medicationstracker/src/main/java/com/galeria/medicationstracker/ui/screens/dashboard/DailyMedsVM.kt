package com.galeria.medicationstracker.ui.screens.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.NewIntakeRepository
import com.galeria.medicationstracker.data.NewMedicationRepository
import com.galeria.medicationstracker.data.network.AuthRepository
import com.galeria.medicationstracker.data.network.IntakeStatus
import com.galeria.medicationstracker.data.network.NetworkIntake
import com.galeria.medicationstracker.data.network.NetworkMedication
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
    val isLoading: Boolean = false, // Добавим состояние загрузки
    val errorMessage: String? = null, // Для отображения ошибок
    val currentTakenMedications: List<NetworkMedication> = emptyList(),
    val todayIntakes: List<NetworkIntake> = emptyList(),
)

data class AddIntakeUiState(
    val selectedMedication: NetworkMedication? = null,
    val selectedMedicationId: String = "",
    val status: IntakeStatus = IntakeStatus.PENDING,
    val selectedIntakeTime: LocalTime = LocalTime.now(),
)

@HiltViewModel
class DailyMedsVM
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

    init {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                // get user id
                val uid = authRepository.getUserId().getOrThrow()
                // Получение списка сегодняшних приемов.
                getActiveMedications(uid.toString())
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "ERROR: ${e.message}"
                    )
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    // Фильтрация лекарств, прием которых окончен для использования при выводе
    // на главный экран.
    private fun getActiveMedications(userId: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            // Сегодняшние приемы.
            medicationRepository.getTodaysMedications(userId)
                .collect { todayMedicationsList ->
                    if (todayMedicationsList.isEmpty()) {
                        //
                    } else {
                        /* for (medication in todayMedicationsList) {
                            // Создание нового приема на сегодня.
                            val intake =
                                NetworkIntake(
                                    userId = userId,
                                    medicationId = medication.id,
                                    status = IntakeStatus.PENDING.name,
                                    presetTime = medication.intakeTime,
                                    factTimestamp = null,
                                    name = medication.name
                                )
                            // Добавление нового приема на сегодня.
                            intakeRepository.addUserIntake(userId, intake)
                            // Обновление данных на экране.
                            _uiState.update { it.copy(todayIntakes = it.todayIntakes + intake) }
                        } */
                        _uiState.update { it.copy(currentTakenMedications = todayMedicationsList) }
                    }
                }
            _uiState.update { it.copy(isLoading = false) }
            
        }
    }
    
    fun newAddNewIntake(
        intakeTime: Timestamp = Timestamp.now(),
        intake: NetworkIntake,
        status: IntakeStatus,
    ) {
        viewModelScope.launch {
            val intakeToSave =
                NetworkIntake(
                    userId = intake.userId,
                    medicationId = intake.medicationId,
                    status = status.name,
                    presetTime = intake.presetTime,
                    factTimestamp = intakeTime,
                    name = intake.name
                )
            intakeRepository.updateUserIntake(intake.userId, intakeToSave)
        }
    }
    
    fun addNewIntake(
        intakeTime: Timestamp = Timestamp.now(),
        medication: NetworkMedication = NetworkMedication(),
        status: IntakeStatus,
    ) {
        viewModelScope.launch {
            val uid = authRepository.getUserId().getOrThrow()
            val intake: NetworkIntake =
                NetworkIntake(
                    userId = uid.toString(),
                    medicationId = medication.id,
                    status = status.name,
                    presetTime = medication.intakeTime,
                    factTimestamp = intakeTime,
                    name = medication.name
                )
            intakeRepository.addUserIntake(uid.toString(), intake)
        }
    }
    
    // Проверка на то, был ли сегодня прием или нет.
    // -1: error; 0: noData, 1: skipped, 2: taken
    // ! перегести в репо.
    suspend fun fetchIntakeStatus(medication: NetworkMedication): Int {
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
                if (querySnapshot.toObjects(NetworkIntake::class.java)[0].status == "TAKEN") 2
                else 1
            } else {
                0
            }
        } catch (e: Exception) {
            Log.e("checkIntake", "Error fetching intake data", e)
            -1
        }
    }
    
    fun setSelectedMedication(value: NetworkMedication) {
        _intakeInputState.update { it.copy(selectedMedication = value) }
    }
    
    fun onTimeSelected(time: LocalTime) {
        _intakeInputState.update { it.copy(selectedIntakeTime = time) }
    }
    // ___
    
}
