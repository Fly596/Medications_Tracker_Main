package com.galeria.medicationstracker.ui.screens.medications.newmed

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.NewIntakeRepository
import com.galeria.medicationstracker.data.NewMedicationRepository
import com.galeria.medicationstracker.data.network.AuthRepository
import com.galeria.medicationstracker.data.network.MedicationForm
import com.galeria.medicationstracker.data.network.NetworkDosage
import com.galeria.medicationstracker.data.network.NetworkMedication
import com.galeria.medicationstracker.data.old.MedicationUnit
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NewMedUiState(
    val uid: String = "",
    val medName: String = "",
    var medForm: String = MedicationForm.TABLET.toString(), // f
    val medStrength: Float = 0.0f,
    val chosenStrengths: List<Float> = emptyList(),
    val medUnit: MedicationUnit = MedicationUnit.MG, // f
    val medStartDate: Timestamp? = Timestamp.now(), // f
    val medEndDate: Timestamp? = Timestamp.now(), // f
    val medIntakeTime: Int = 0, // f
    val medNotes: String = "",
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val intakeDays: List<String> = emptyList(),
    val medicationForm: List<String> =
        MedicationForm.entries.map {
            it.name.lowercase().replaceFirstChar { it.uppercase() }
        },
)

@HiltViewModel
class AddNewMedViewModel
@Inject
constructor(
    private val intakeRepository: NewIntakeRepository,
    private val medicationRepository: NewMedicationRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    var uiState = MutableStateFlow(NewMedUiState())
        private set

    val db = FirestoreService.db
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    val userLogin = auth.currentUser?.email

    // Добавление нового лекарства в Firestore.
    fun addMedication(context: Context) {
        viewModelScope.launch {
            // Проверка на пустые значения текстовых полей и нулевое значение medStrength
            // ? в репо
            if (
                uiState.value.medName.isBlank() ||
                uiState.value.medForm.toString().isBlank() ||
                uiState.value.medUnit.toString().isBlank() ||
                uiState.value.medStrength <= 0 ||
                uiState.value.medStartDate.toString().isBlank() ||
                uiState.value.medEndDate.toString().isBlank() ||
                uiState.value.intakeDays.isEmpty()
            ) {
                Toast.makeText(
                    context,
                    "Please fill in all required fields correctly!",
                    Toast.LENGTH_SHORT,
                )
                    .show()
                Log.w(
                    TAG,
                    "Validation failed: Missing or incorrect input fields."
                )
                // return
            }
            val networkMedication =
                NetworkMedication(
                    userId = userId.toString(),
                    name = uiState.value.medName,
                    dosage =
                        NetworkDosage(
                            uiState.value.medStrength.toDouble(),
                            uiState.value.medUnit.toString(),
                        ),
                    form = uiState.value.medForm,
                    startDate = uiState.value.medStartDate,
                    endDate = uiState.value.medEndDate,
                    daysOfWeek = uiState.value.intakeDays,
                    intakeTimeFromMidnight = uiState.value.medIntakeTime,
                )
            medicationRepository.addMedication(
                networkMedication
            )
        }
        /*         val medsCollectionRef =
            db.collection("User").document(userId.toString())
                .collection("medications")

        // Документ не существует, добавляем новый
        val networkMedication =
            NetworkMedication(
                userId = userId.toString(),
                name = uiState.value.medName,
                dosage = NetworkDosage(uiState.value.medStrength.toDouble(),uiState.value.medUnit.toString()),
                form = uiState.value.medForm,
                startDate = uiState.value.medStartDate,
                endDate = uiState.value.medEndDate,
                daysOfWeek = uiState.value.intakeDays,
                intakeTime = uiState.value.medIntakeTime
            )

        medsCollectionRef
            .add(networkMedication)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "DocumentSnapshot added successfully!",
                    Toast.LENGTH_SHORT,
                )
                    .show()

                Log.d(TAG, "DocumentSnapshot added")
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Error adding medication",
                    Toast.LENGTH_SHORT
                )
                    .show()
                Log.w(TAG, "Error adding document", e)
            } */

    }
    
    fun updateStartDate(input: Timestamp?) {
        uiState.value =
            uiState.value.copy(medStartDate = input ?: Timestamp.now())
    }
    
    fun updateEndDate(input: Timestamp?) {
        uiState.value =
            uiState.value.copy(medEndDate = input ?: Timestamp.now())
    }
    
    fun updateMedName(newName: String) {
        uiState.value = uiState.value.copy(medName = newName)
    }
    
    fun updateMedForm(newForm: String) {
        uiState.value = uiState.value.copy(medForm = newForm)
    }
    
    fun updateMedStrength(newStrength: Float) {
        uiState.value =
            uiState.value.copy(medStrength = newStrength /* .toFloat() */)
    }
    
    fun addStrength(newStrength: Float) {
        uiState.value =
            uiState.value.copy(chosenStrengths = uiState.value.chosenStrengths + newStrength)
    }
    
    fun updateMedUnit(newUnit: MedicationUnit) {
        uiState.value = uiState.value.copy(medUnit = newUnit)
    }
    
    fun updateIntakeTime(newTime: Int) {
        uiState.value = uiState.value.copy(medIntakeTime = newTime)
    }
    
    fun updateMedNotes(newNotes: String) {
        uiState.value = uiState.value.copy(medNotes = newNotes)
    }
    
    fun isShowDateChecked(input: Boolean) {
        uiState.value = uiState.value.copy(showDatePicker = !input)
    }
    
    fun isShowTimeChecked(input: Boolean) {
        uiState.value = uiState.value.copy(showTimePicker = !input)
    }
    
    fun updateSelectedDays(input: List<String>) {
        uiState.value =
            uiState.value.copy(intakeDays = uiState.value.intakeDays + input)
    }
}
