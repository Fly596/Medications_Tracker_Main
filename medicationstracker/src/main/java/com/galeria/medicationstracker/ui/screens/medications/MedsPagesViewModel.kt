package com.galeria.medicationstracker.ui.screens.medications

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.UserMedication
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource.SERVER
import com.google.firebase.firestore.Source
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MedsPagesUiState(
  val medications: List<UserMedication> = emptyList(),
  val selectedMed: UserMedication? = null,
  val intakesCount: Int = 0,
  val skipCount: Int = 0
)

class MedsPagesViewModel : ViewModel() {
  
  private var _uiState = MutableStateFlow(MedsPagesUiState())
  val uiState: StateFlow<MedsPagesUiState> = _uiState.asStateFlow()
  private val db = FirestoreService.db
  private val userId = FirebaseAuth.getInstance().currentUser?.uid
  
  fun getSelectedMed(medName: String) {
    viewModelScope.launch {
      // путь к коллекции с лекарствами
      val docRef = db.collection("UserMedication")
      val source = Source.DEFAULT
      
      docRef.whereEqualTo("uid", userId).whereEqualTo("name", medName)
        .get(source).addOnSuccessListener { docSnapshot ->
          if (docSnapshot.documents.isNotEmpty()) {
            val medications = docSnapshot.toObjects(UserMedication::class.java)
            _uiState.value = _uiState.value.copy(selectedMed = medications[0])
            getMedicationIntakes()
            getSkippedIntakes()
            Log.d(TAG, "Success getting document: ")
          } else {
            Log.d(TAG, "No such document")
          }
        }.addOnFailureListener { exception ->
          Log.d(TAG, "Error getting documents: ", exception)
          
          println("Error getting documents: $exception")
        }
    }
    
  }
  
  private fun getMedicationIntakes() {
    viewModelScope.launch {
      val docRef = db.collection("MedicationIntake")
        .whereEqualTo(
          "medicationName",
          uiState.value.selectedMed?.name.toString()
        )
        .whereEqualTo("status", true)
      val count = docRef.count()
      val source = Source.CACHE
      // Получение количества документов в коллекции.
      count.get(SERVER).addOnCompleteListener { task ->
        if (task.isSuccessful) {
          val snapshot = task.result
          // Обновление состояния UI с количеством документов.
          updateIntakesCount(snapshot.count.toInt())
          Log.d(TAG, "Count: ${snapshot.count}")
        } else {
          Log.d(TAG, "Error getting count", task.exception)
        }
      }
    }
    
  }
  
  fun getSkippedIntakes() {
    viewModelScope.launch {
      val docRef = db.collection("MedicationIntake")
        .whereEqualTo(
          "medicationName",
          uiState.value.selectedMed?.name.toString()
        )
        .whereEqualTo("status", false)
      val count = docRef.count()
      // Получение количества документов в коллекции.
      count.get(SERVER).addOnCompleteListener { task ->
        if (task.isSuccessful) {
          val snapshot = task.result
          // Обновление состояния UI с количеством документов.
          updateSkippedIntakesCount(snapshot.count.toInt())
          Log.d(TAG, "Count: ${snapshot.count}")
        } else {
          Log.d(TAG, "Error getting count", task.exception)
        }
      }
    }
    
  }
  
  private fun updateMedications(medications: List<UserMedication>) {
    _uiState.value = _uiState.value.copy(medications = medications)
  }
  
  private fun updateIntakesCount(count: Int) {
    _uiState.value = _uiState.value.copy(intakesCount = count)
  }
  
  private fun updateSkippedIntakesCount(count: Int) {
    _uiState.value = _uiState.value.copy(skipCount = count)
  }
  
  fun getAllUserMeds() {
    val docRef = db.collection("UserMedications")
    
    docRef.whereEqualTo("uid", userId).get()
      .addOnSuccessListener {
        val medications = it.toObjects(UserMedication::class.java)
        updateMedications(medications)
      }
  }
  
}