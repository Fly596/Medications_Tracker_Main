package com.galeria.medicationstracker.ui.screens.medications.update

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.galeria.medicationstracker.data.NewMedicationRepository
import com.galeria.medicationstracker.data.network.AuthRepository
import com.galeria.medicationstracker.data.network.MedicationForm
import com.galeria.medicationstracker.data.network.NetworkMedication
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService
import com.galeria.medicationstracker.utils.navigation.MedicationScreen
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UpdateMedUiState(
    val medId: String? = null,
    val medName: String = "",
    val medForm: String = MedicationForm.TABLET.name,
    val startDate: Timestamp = Timestamp.now(),
    val endDate: Timestamp = Timestamp.now(),
    val intakeTime: String = "",
    val dosage: Float = 0.0f,
    val selectedDays: List<String> = emptyList(),
    val medication: NetworkMedication? = null,
)

@HiltViewModel
class UpdateMedVM
@Inject
constructor(
    private val repository: NewMedicationRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    
    // TODO
    private val _uiState = MutableStateFlow(UpdateMedUiState())
    val uiState = _uiState.asStateFlow()
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirestoreService.db
    private var _selectedMedication = MutableStateFlow<NetworkMedication?>(null)
    private var _selectedDocumentId = MutableStateFlow<String?>(null)
    
    init {
        viewModelScope.launch {
            val args =
                savedStateHandle.toRoute<MedicationScreen.UpdateMedication>()
            val medId = args.medicationId
            _uiState.update { it.copy(medId = medId) }
            
            fetchSelectedMedication(medId)
        }
    }
    
    fun deleteMedicationFromFirestore(medId: String) {
        viewModelScope.launch {
            repository.deleteMedication(currentUserId.toString(), medId)
        }
    }
    
    private fun fetchSelectedMedication(medId: String) {
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
            )  */
        /* .document("${userEmail}_${uiState.value.medication?.name}_${uiState.value.medication?.strength}") */
        /*
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
            _uiState.value.copy(
                selectedDays = _uiState.value.selectedDays + input
            )
    }
    
    fun updateMedName(input: String) {
        _uiState.value = _uiState.value.copy(medName = input)
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
    
    fun updateStrength(input: Float) {
        _uiState.value = _uiState.value.copy(dosage = input)
    }
}
