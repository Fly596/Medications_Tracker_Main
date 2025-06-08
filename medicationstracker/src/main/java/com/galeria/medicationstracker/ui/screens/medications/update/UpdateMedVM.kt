package com.galeria.medicationstracker.ui.screens.medications.update

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.MedicationForm
import com.galeria.medicationstracker.data.NewMedicationRepository
import com.galeria.medicationstracker.data.NewUserMedication
import com.galeria.medicationstracker.data.old.MedicationUnit
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UpdateMedUiState(
    val medName: String = "",
    val medForm: MedicationForm = MedicationForm.TABLET,
    val endDate: Timestamp = Timestamp.now(),
    val unit: MedicationUnit = MedicationUnit.MG,
    val startDate: Timestamp = Timestamp.now(),
    val intakeTime: String = "",
    val notes: String = "",
    val strength: Float = 0.0f,
    val strengthUnit: MedicationUnit = MedicationUnit.MG, // Add strength unit
    val selectedDays: List<String> = emptyList(),
    val newSelectedDays: List<String> = emptyList(),
    val medication: NewUserMedication? = null,
)

@HiltViewModel
class UpdateMedVM @Inject constructor(private val repository: NewMedicationRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UpdateMedUiState())
    val uiState = _uiState.asStateFlow()
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirestoreService.db
    private var _selectedMedication = MutableStateFlow<NewUserMedication?>(null)
    private var _selectedDocumentId = MutableStateFlow<String?>(null)
    
    fun deleteMedicationFromFirestore(medId: String) {
        viewModelScope.launch {
            repository.deleteMedication(
                currentUserId.toString(),
                medId
            )
        }
    }
    
    fun fetchSelectedMedication(medId: String) {
        viewModelScope.launch {
            val drug = repository.getMedication(currentUserId.toString(), medId)
            if (drug.isSuccess) {
                _uiState.value =
                    _uiState.value.copy(medication = drug.getOrNull())
            }
        }
    }

    // Обновление данных о лекарстве в Firestore.
    fun updateMedicationFromFirestore(context: Context) {
        /*        val newValues: Map<String, Any?> =
                   mapOf(
                       "endDate" to _uiState.value.endDate,
                       "form" to _uiState.value.medForm.toString(),
                       "daysOfWeek" to _uiState.value.newSelectedDays,
                       "intakeTime" to _uiState.value.intakeTime,
                       "name" to _uiState.value.medName,
                       "notes" to _uiState.value.notes,
                       "strength" to _uiState.value.strength,
                       "unit" to _uiState.value.unit.toString(),
                       "startDate" to _uiState.value.startDate,
                       "uid" to currentUserId,
                   )
               val medicationRef =
                   db.collection(
                       "UserMedication"
                   )  *//* .document("${userEmail}_${uiState.value.medication?.name}_${uiState.value.medication?.strength}") *//*
        medicationRef
            .whereEqualTo("uid", currentUserId)
            .whereEqualTo("name", _uiState.value.medication?.name)
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.toObjects(UserMedication::class.java)[0]
                val documentId = querySnapshot.documents[0].id
                medicationRef.document(documentId).update(newValues)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error updating medication", Toast.LENGTH_SHORT).show()
            } */
    }

    fun updateSelectedDays(input: List<String>) {
        _uiState.value =
            _uiState.value.copy(newSelectedDays = _uiState.value.newSelectedDays + input)
    }

    fun updateMedName(input: String) {
        _uiState.value = _uiState.value.copy(medName = input)
    }

    fun updateMedForm(input: MedicationForm) {
        _uiState.value = _uiState.value.copy(medForm = input)
    }

    fun updateEndDate(date: Timestamp?) {
        _uiState.value = _uiState.value.copy(endDate = date ?: Timestamp.now())
    }

    fun updateStartDate(date: Timestamp?) {
        _uiState.value = _uiState.value.copy(startDate = date ?: Timestamp.now())
    }

    fun updateIntakeTime(time: String) {
        _uiState.value = _uiState.value.copy(intakeTime = time)
    }

    fun updateNotes(input: String) {
        _uiState.value = _uiState.value.copy(notes = input)
    }

    fun updateStrength(input: Float) {
        _uiState.value = _uiState.value.copy(strength = input)
    }

    fun updateStrengthUnit(input: MedicationUnit) {
        _uiState.value = uiState.value.copy(strengthUnit = input)
    }
}
