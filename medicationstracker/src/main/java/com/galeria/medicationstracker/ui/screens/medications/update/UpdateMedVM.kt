package com.galeria.medicationstracker.ui.screens.medications.update

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.galeria.medicationstracker.data.MedicationForm
import com.galeria.medicationstracker.data.MedicationUnit
import com.galeria.medicationstracker.data.UserMedication
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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

class UpdateMedVM : ViewModel() {

    var uiState by mutableStateOf(UpdateMedUiState())
        private set

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
                        uiState =
                            uiState.copy(
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
                "endDate" to uiState.endDate,
                "form" to uiState.medForm.toString(),
                "daysOfWeek" to uiState.newSelectedDays,
                "intakeTime" to uiState.intakeTime,
                "name" to uiState.medName,
                "notes" to uiState.notes,
                "strength" to uiState.strength,
                "unit" to uiState.unit.toString(),
                "startDate" to uiState.startDate,
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
        uiState = uiState.copy(newSelectedDays = uiState.newSelectedDays + input)
    }

    fun updateMedName(input: String) {
        uiState = uiState.copy(medName = input)
    }

    fun updateMedForm(input: MedicationForm) {
        uiState = uiState.copy(medForm = input)
    }

    fun updateEndDate(date: Timestamp?) {
        uiState = uiState.copy(endDate = date ?: Timestamp.now())
    }

    fun updateStartDate(date: Timestamp?) {
        uiState = uiState.copy(startDate = date ?: Timestamp.now())
    }

    fun updateIntakeTime(time: String) {
        uiState = uiState.copy(intakeTime = time)
    }

    fun updateNotes(input: String) {
        uiState = uiState.copy(notes = input)
    }

    fun updateStrength(input: Float) {
        uiState = uiState.copy(strength = input)
    }

    fun updateStrengthUnit(input: MedicationUnit) {
        uiState = uiState.copy(strengthUnit = input)
    }
}
