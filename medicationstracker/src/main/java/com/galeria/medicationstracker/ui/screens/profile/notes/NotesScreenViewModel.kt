package com.galeria.medicationstracker.ui.screens.profile.notes

import androidx.lifecycle.ViewModel
import com.galeria.medicationstracker.data.NewNoteRepository
import com.galeria.medicationstracker.data.network.NetworkUserNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class NotesScreenUiState(
    val testData: String = "",
    val notes: List<NetworkUserNote> = emptyList()
)

@HiltViewModel
class NotesScreenViewModel @Inject constructor(
    private val repository: NewNoteRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NotesScreenUiState())
    val uiState = _uiState.asStateFlow()
    
    init {
        /*         viewModelScope.launch {
                    _uiState.value = _uiState.value.copy(
                        notes = repository.observeUserNotes()
                    )
        
                } */
    }
}