package com.galeria.medicationstracker.ui.screens.profile.notes

import androidx.lifecycle.ViewModel
import com.galeria.medicationstracker.data.repository.NewUserRepository
import com.galeria.medicationstracker.data.source.network.NetworkMedication
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class NewNoteScreenUiState(
    val title: String = "",
    val content: String = "",
    val date: Timestamp = Timestamp.now(),
    val tags: List<String> = emptyList(),
    val medications: List<NetworkMedication> = emptyList(), // Available medications
    val selectedMedications: List<String> = emptyList() // Selected medications
)

@HiltViewModel
class NewNoteViewModel @Inject constructor(
    private val repository: NewUserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NewNoteScreenUiState())
    val uiState = _uiState.asStateFlow()
    
    init {
        // Load medications from repository (mocked for simplicity)
        /*         viewModelScope.launch {
                    _uiState.value = _uiState.value.copy(
                        /medications = repository.getUserDrugs() // Assume this returns a List<String>
                    )
        
                } */
    }
    
    fun saveNote() {
        /*         val note = NewUserNote(
                    title = _uiState.value.title,
                    content = _uiState.value.content,
                    date = _uiState.value.date,
                    tags = _uiState.value.tags,
                    medication = _uiState.value.selectedMedications
                )
                viewModelScope.launch {
                    repository.saveNote(note)
                } */
    }
    
    fun updateTitle(newTitle: String) {
        _uiState.value = _uiState.value.copy(title = newTitle)
    }
    
    fun updateContent(newContent: String) {
        _uiState.value = _uiState.value.copy(content = newContent)
    }
    
    fun updateDate(newDate: Timestamp) {
        _uiState.value = _uiState.value.copy(date = newDate)
    }
    
    fun updateTags(newTags: String) {
        _uiState.value =
            _uiState.value.copy(tags = uiState.value.tags + newTags)
    }
    
    fun toggleMedication(medication: String) {
        val currentSelected = _uiState.value.selectedMedications
        _uiState.value = if (medication in currentSelected) {
            _uiState.value.copy(selectedMedications = currentSelected - medication)
        } else {
            _uiState.value.copy(selectedMedications = currentSelected + medication)
        }
    }
    
}