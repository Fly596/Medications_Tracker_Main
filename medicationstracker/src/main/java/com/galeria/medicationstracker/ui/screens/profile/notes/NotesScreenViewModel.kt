package com.galeria.medicationstracker.ui.screens.profile.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.Note
import com.galeria.medicationstracker.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotesScreenUiState(
    val testData: String = "",
    val notes: List<Note> = emptyList()
)

@HiltViewModel
class NotesScreenViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                notes = repository.getNotes()
            )

        }
    }
}