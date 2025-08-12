package com.galeria.medicationstracker.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.network.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Состояние главного экрана (хранит пользователя и стартовый экран).
data class MainState(
    val user: FirebaseUser? = null,
    val startDestination: Any = GraphRoutes.Home
)

@HiltViewModel
class MainViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    
    private val _uiState = MutableStateFlow(MainState())
    val uiState = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            authRepository.getAuthState().collect { user ->
                if (user != null) {
                    // Если пользователь авторизован → старт с домашнего экрана
                    _uiState.update {
                        it.copy(
                            user = user,
                            startDestination = GraphRoutes.Home
                        )
                    }
                } else {
                    // Если нет → старт с экрана авторизации
                    _uiState.update {
                        it.copy(
                            user = user,
                            startDestination = GraphRoutes.Auth
                        )
                    }
                }
            }
        }
    }
}
