package com.galeria.medicationstracker.ui.screens.dashboard.moodtracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.NewMoodRepository
import com.galeria.medicationstracker.data.NewUserMood
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class MoodTrackerUiState(val moodValue: Int = 5, val notes: String? = null)

@HiltViewModel
class MoodTrackerVM
@Inject
constructor(
    private val moodRepository: NewMoodRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    // val db = FirestoreService.db
    private val _uiState = MutableStateFlow(MoodTrackerUiState())
    val uiState: StateFlow<MoodTrackerUiState> = _uiState.asStateFlow()
    private lateinit var currentUserId: String
    private lateinit var currentUserEmail: String

    init {
        viewModelScope.launch {
            // Получение id и почты пользователя.
            val emailResult = authRepository.getUserEmail()
            val uidResult = authRepository.getUserId()

            if (emailResult.isSuccess && uidResult.isSuccess) {
                currentUserEmail = emailResult.getOrNull().toString()
                currentUserId = uidResult.getOrNull().toString()
            }
        }
    }

    fun addMood() {
        viewModelScope.launch {
            val mood = _uiState.value.moodValue
            val notes = _uiState.value.notes
            val currentDate: Date = Date()
            val moodEntry: NewUserMood =
                NewUserMood(
                    userId = currentUserId,
                    moodValue = mood,
                    notes = notes,
                    timestamp = Timestamp(currentDate),
                )
            moodRepository.addMood(userId = currentUserId, moodData = moodEntry)
        }
    }

    fun updateMood(mood: Int) {
        _uiState.value = _uiState.value.copy(moodValue = mood)
    }

    fun updateNotes(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }
}
