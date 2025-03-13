package com.galeria.medicationstracker.ui.screens.medications.update

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.galeria.medicationstracker.data.MedicationForm
import com.galeria.medicationstracker.data.MedicationUnit
import com.galeria.medicationstracker.data.MedicationsRepository
import com.galeria.medicationstracker.data.UserMedication
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
)

@HiltViewModel
class UpdateMedVM @Inject constructor(private val repository: MedicationsRepository) :
    ViewModel() {
    
    private val _uiState = MutableStateFlow(UpdateMedUiState())
    val uiState = _uiState.asStateFlow()
    

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirestoreService.db

    private var _selectedMedication = MutableStateFlow<UserMedication?>(null)
    var selectedMedication = _selectedMedication.asStateFlow()
    private var _selectedDocumentId = MutableStateFlow<String?>(null)
    var selectedDocumentId = _selectedDocumentId.asStateFlow()

    // для получения выбранного лекарства.
    fun fetchSelectedMedication(medName: String) {
        val docRef = db.collection("UserMedication")

        docRef
            .whereEqualTo("uid", currentUserId)
            .whereEqualTo("name", medName)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (value != null && value.documents.isNotEmpty()) {
                    val document = value.documents[0]

                    _selectedMedication.value = document.toObject<UserMedication>()
                    _selectedDocumentId.value = document.id // Save the document ID
                    if (_selectedMedication.value != null) {
                        _uiState.value =
                            _uiState.value.copy(
                                medName = _selectedMedication.value!!.name.toString(),
                                medForm =
                                    MedicationForm.valueOf(
                                        _selectedMedication.value!!.form.toString()
                                    ),
                                endDate = _selectedMedication.value!!.endDate!!,
                                startDate = _selectedMedication.value!!.startDate!!,
                                intakeTime = _selectedMedication.value!!.intakeTime.toString(),
                                notes = _selectedMedication.value!!.notes.toString(),
                                strength = _selectedMedication.value!!.strength!!.toFloat(),
                                selectedDays = _selectedMedication.value!!.daysOfWeek,
                            )
                    }
                }
            }
    }

    // Обновление данных о лекарстве в Firestore.
    fun updateMedicationFromFirestore(context: Context) {
        val newValues: Map<String, Any?> =
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
        val documentId = _selectedDocumentId.value
        if (documentId != null) {
            db.collection("UserMedication")
                .document(documentId)
                .update(newValues)
                .addOnSuccessListener {
                    Toast.makeText(context, "Medication updated successfully!", Toast.LENGTH_SHORT)
                        .show()

                    Log.d("Firestore", "Medication updated successfully!")
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error updating medication", Toast.LENGTH_SHORT).show()

                    Log.e("Firestore", "Error updating medication", exception)
                }
        } else {
            Log.e("Firestore", "No document ID available. Unable to update medication.")
        }
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
        _uiState.value =
            _uiState.value.copy(startDate = date ?: Timestamp.now())
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
