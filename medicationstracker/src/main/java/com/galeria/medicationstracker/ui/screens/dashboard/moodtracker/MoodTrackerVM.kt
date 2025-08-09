package com.galeria.medicationstracker.ui.screens.dashboard.moodtracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.NewMoodRepository
import com.galeria.medicationstracker.data.network.AuthRepository
import com.galeria.medicationstracker.data.network.NetworkUserMood
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class MoodTrackerUiState(
    val moodValue: Int = 5,
    val notes: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)

@HiltViewModel
class MoodTrackerVM
@Inject
constructor(
    private val moodRepository: NewMoodRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MoodTrackerUiState())
    val uiState: StateFlow<MoodTrackerUiState> = _uiState.asStateFlow()
    
    fun addMood() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val uid = authRepository.getUserId().getOrThrow()
                val mood = _uiState.value.moodValue
                val notes = _uiState.value.notes
                val currentDate: Date = Date()
                val moodEntry: NetworkUserMood =
                    NetworkUserMood(
                        userId = uid.toString(),
                        moodValue = mood,
                        notes = notes,
                        timestamp = Timestamp(currentDate),
                    )
                
                moodRepository.addMood(uid.toString(), moodEntry)
                
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "ERROR: ${e.message}"
                    )
                }
            }
        }
    }
    
    fun updateMood(mood: Int) {
        _uiState.value = _uiState.value.copy(moodValue = mood)
    }
    
    fun updateNotes(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }
}
