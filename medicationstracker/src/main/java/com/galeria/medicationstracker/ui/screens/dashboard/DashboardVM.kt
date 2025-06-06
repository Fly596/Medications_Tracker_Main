package com.galeria.medicationstracker.ui.screens.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.UserIntake
import com.galeria.medicationstracker.data.UserMedication
import com.galeria.medicationstracker.data.imp.IntakeStatus
import com.galeria.medicationstracker.data.imp.NewIntakeRepository
import com.galeria.medicationstracker.data.imp.NewMedicationRepository
import com.galeria.medicationstracker.data.imp.NewUserIntake
import com.galeria.medicationstracker.data.imp.NewUserMedication
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService
import com.galeria.medicationstracker.utils.toTimestamp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

data class DashboardUiState(
    val oldCurrentTakenMedications: List<UserMedication> = emptyList(),
    val currentTakenMedications: List<NewUserMedication> = emptyList(),
)

@HiltViewModel
class DashboardVM
@Inject
constructor(
    private val intakeRepository: NewIntakeRepository,
    private val medicationRepository: NewMedicationRepository,
) : ViewModel() {

    val db = FirestoreService.db
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    // Лекарства, которые нужно принимать.
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val currentUserId = firebaseAuth.currentUser?.uid

    init {
        // Получение списка активных лекарств пациента.

        getCurrentMedications()
    }

    private var showToastCallback: ((String) -> Unit)? = null

    // Фильтрация лекарств, прием которых окончен для использования при выводе на главный экран.
    private fun getCurrentMedications() {
        viewModelScope.launch {
            if (currentUserId != null) {
                val intakes =
                    medicationRepository.getTodaysIntakes(currentUserId).collect { intakesList ->
                        if (intakesList.isEmpty()) {
                            //
                        } else {
                            _uiState.update { it.copy(currentTakenMedications = intakesList) }
                        }
                    }
            }
        }
        // TODO:
        /* val todayEnd = LocalDate.now().plusDays(1).atStartOfDay().toTimestamp()
        val todayWeekDay = formatTimestampToWeekday(Timestamp.now()).uppercase()

        viewModelScope.launch {
            db.collection("UserMedication")
                .whereEqualTo("uid", currentUserId)
                .whereGreaterThanOrEqualTo("endDate", todayEnd)
                .whereArrayContains("daysOfWeek", todayWeekDay)
                .addSnapshotListener { medicationSnapshots, error ->
                    if (error != null) {
                        Log.e(
                            "DashboardVM",
                            "Error fetching current medications: ${error.message}",
                            error,
                        )
                        showToastCallback?.invoke("Error loading medications")
                        return@addSnapshotListener
                    }

                    medicationSnapshots?.let {
                        _uiState.value =
                            _uiState.value.copy(
                                oldCurrentTakenMedications =
                                    it.toObjects(UserMedication::class.java)
                            )

                        showToastCallback?.invoke("Medications loaded successfully")
                    }
                }
        } */
    }

    fun oldAddNewIntake(
        intakeTime: Timestamp = Timestamp.now(),
        medication: NewUserMedication = NewUserMedication(),
        status: IntakeStatus,
    ) {
        val intake: NewUserIntake =
            NewUserIntake(
                userId = currentUserId.toString(),
                medicationId = medication.id,
                status = status.name,
            )
        viewModelScope.launch {
            intakeRepository.addUserIntake(currentUserId.toString(), intake)
            /*   val intake =
                NewUserIntake(
                    userId = currentUserId.toString(),
                    medicationId = medication.id,
                    status = status.name,
                    timestamp = intakeTime.toDate(),
                )
            intakeRepository.addUserIntake(userId = currentUserId.toString(), intake) */
        }
    }

    // Проверка на то, был ли сегодня прием или нет.
    // -1: error; 0: noData, 1: skipped, 2: taken
    suspend fun fetchIntakeStatus(medication: UserMedication): Int {
        val todayStart = LocalDate.now().atStartOfDay().toTimestamp()
        val todayEnd = LocalDate.now().plusDays(1).atStartOfDay().toTimestamp()
        return try {
            val querySnapshot =
                db.collection("User")
                    .document("${FirebaseAuth.getInstance().currentUser?.email}")
                    .collection("intakes")
                    .whereEqualTo("medicationName", medication.name)
                    .whereGreaterThanOrEqualTo("dateTime", todayStart)
                    .whereLessThan("dateTime", todayEnd)
                    .limit(1)
                    .get(Source.SERVER)
                    .await()

            if (!querySnapshot.isEmpty) {
                if (querySnapshot.toObjects(UserIntake::class.java)[0].status.toString() == "TAKEN")
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

    fun localDateTimeToTimestamp(localDateTime: LocalDateTime): Timestamp {
        val secs = localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond()
        val nanos = localDateTime.nano

        return Timestamp(secs, nanos)
    }
}
