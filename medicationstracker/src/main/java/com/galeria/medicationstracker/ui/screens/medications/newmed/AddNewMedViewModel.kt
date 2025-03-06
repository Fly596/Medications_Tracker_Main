package com.galeria.medicationstracker.ui.screens.medications.newmed

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.galeria.medicationstracker.data.MedicationForm
import com.galeria.medicationstracker.data.MedicationUnit
import com.galeria.medicationstracker.data.UserMedication
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService
import com.google.firebase.Timestamp
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

data class NewMedUiState(
  val uid: String = "",
  val medName: String = "",
  var medForm: String = MedicationForm.TABLET.toString(), // f
  val medStrength: Float = 0.0f,
  val chosenStrengths: List<Float> = emptyList(),
  val medUnit: MedicationUnit = MedicationUnit.MG, // f
  val medStartDate: Timestamp? = null, // f
  val medEndDate: Timestamp? = null, // f
  val medIntakeTime: String = "", // f
  val medNotes: String = "",
  val showDatePicker: Boolean = false,
  val showTimePicker: Boolean = false,
  val intakeDays: List<String> = emptyList(),
  val medicationForm: List<String> =
    MedicationForm.entries.map {
      it.name.lowercase().replaceFirstChar { it.uppercase() }
    }
)

class AddNewMedViewModel : ViewModel() {
  
  var uiState = MutableStateFlow(NewMedUiState())
    private set
  val db = FirestoreService.db
  val auth = FirebaseAuth.getInstance()
  val userId = auth.currentUser?.uid
  val userLogin = auth.currentUser?.email
  
  // Добавление нового лекарства в Firestore.
  fun addMedication(
    context: Context,
  ) {
    // Проверка на пустые значения текстовых полей и нулевое значение medStrength
    if (uiState.value.medName.isBlank() ||
      uiState.value.medForm.toString().isBlank() ||
      uiState.value.medUnit.toString().isBlank() ||
      uiState.value.medStrength <= 0 ||
      uiState.value.medStartDate.toString().isBlank() ||
      uiState.value.medEndDate.toString().isBlank() ||
      uiState.value.medIntakeTime.isBlank() ||
      uiState.value.intakeDays.isEmpty()
    ) {
      Toast.makeText(
        context,
        "Please fill in all required fields correctly!",
        Toast.LENGTH_SHORT
      ).show()
      Log.w(TAG, "Validation failed: Missing or incorrect input fields.")
      return
    }
    val medicationRef = db
      .collection("UserMedication")
    val documentId =
      "${userLogin}_${uiState.value.medName}_${uiState.value.medStrength}"
    // Проверка на дубликаты
    medicationRef.document(documentId).get()
      .addOnSuccessListener { documentSnapshot ->
        if (documentSnapshot.exists()) {
          // Документ уже существует
          Toast.makeText(
            context,
            "Medication already exists!",
            Toast.LENGTH_SHORT
          ).show()
          
          Log.d(TAG, "Medication already exists with ID: $documentId")
        } else {
          // Документ не существует, добавляем новый
          val newUserMedication = UserMedication(
            userId,
            uiState.value.medName,
            uiState.value.medForm.toString(),
            uiState.value.medStrength,
            uiState.value.medUnit.toString(),
            uiState.value.medStartDate,
            uiState.value.medEndDate,
            uiState.value.intakeDays,
            uiState.value.medIntakeTime,
            uiState.value.medNotes,
            uiState.value.chosenStrengths
          )
          
          medicationRef.document(documentId)
            .set(newUserMedication)
            .addOnSuccessListener {
              Toast.makeText(
                context,
                "DocumentSnapshot added successfully!",
                Toast.LENGTH_SHORT
              ).show()
              
              Log.d(TAG, "DocumentSnapshot added with ID: $documentId")
            }
            .addOnFailureListener { e ->
              Toast.makeText(
                context,
                "Error adding medication",
                Toast.LENGTH_SHORT
              )
                .show()
              Log.w(TAG, "Error adding document", e)
            }
        }
      }
  }
  
  fun updateStartDate(input: Timestamp?) {
    uiState.value = uiState.value.copy(medStartDate = input ?: Timestamp.now())
  }
  
  fun updateEndDate(input: Timestamp?) {
    uiState.value = uiState.value.copy(medEndDate = input ?: Timestamp.now())
  }
  
  fun updateMedName(newName: String) {
    uiState.value = uiState.value.copy(medName = newName)
  }
  
  fun updateMedForm(newForm: String) {
    uiState.value = uiState.value.copy(medForm = newForm)
  }
  
  fun updateMedStrength(newStrength: Float) {
    uiState.value =
      uiState.value.copy(medStrength = newStrength/* .toFloat() */)
  }
  
  fun addStrength(newStrength: Float) {
    uiState.value =
      uiState.value.copy(chosenStrengths = uiState.value.chosenStrengths + newStrength)
  }
  
  fun updateMedUnit(newUnit: MedicationUnit) {
    uiState.value = uiState.value.copy(medUnit = newUnit)
  }
  
  fun updateIntakeTime(newTime: String) {
    uiState.value = uiState.value.copy(medIntakeTime = newTime)
  }
  
  fun updateMedNotes(newNotes: String) {
    uiState.value = uiState.value.copy(medNotes = newNotes)
  }
  
  fun isShowDateChecked(input: Boolean) {
    uiState.value = uiState.value.copy(showDatePicker = input)
  }
  
  fun isShowTimeChecked(input: Boolean) {
    uiState.value = uiState.value.copy(showTimePicker = input)
  }
  
  fun updateSelectedDays(input: List<String>) {
    uiState.value =
      uiState.value.copy(intakeDays = uiState.value.intakeDays + input)
  }
  
}
