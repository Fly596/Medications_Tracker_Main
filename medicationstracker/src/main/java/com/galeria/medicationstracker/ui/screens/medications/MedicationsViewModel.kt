package com.galeria.medicationstracker.ui.screens.medications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.MedicationsRepository
import com.galeria.medicationstracker.data.UserMedication
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MedicationsUiState(
  val userMedications: List<UserMedication> = emptyList(),
)

@HiltViewModel
class MedicationsViewModel @Inject constructor(
  private val repository: MedicationsRepository
) : ViewModel() {
  
  private val _uiState = MutableStateFlow(MedicationsUiState())
  val uiState = _uiState.asStateFlow()
  private val userId = FirebaseAuth.getInstance().currentUser?.uid
  
  init {
    // Fetch user medications and collect the flow
    userId?.let { uid ->
      viewModelScope.launch {
        repository.getDrugsStream(uid)
          .collect { medications ->
            _uiState.value = _uiState.value.copy(userMedications = medications)
          }
      }
    }
  }
  
  // Удаление лекарства из Firestore.
  fun deleteMedicationFromFirestore(medName: String) {
    viewModelScope.launch {
      repository.deleteDrug(medName)
    }
  }
}