package com.galeria.medicationstracker.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.source.network.OLDAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Loading : AuthState()
    
    object Authenticated : AuthState()
    
    object Unauthenticated : AuthState()
}

@HiltViewModel
class MainViewModel @Inject constructor(private val OLDAuthRepository: OLDAuthRepository) :
    ViewModel() {
    
    private val _uiState = MutableStateFlow<AuthState>(AuthState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        // Проверяем, залогинен ли пользователь при запуске приложения.
        checkUserLoggedIn()
    }
    
    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            OLDAuthRepository.getAuthState().collect { user ->
                if (user == null) {
                    _uiState.value = AuthState.Unauthenticated
                } else {
                    _uiState.value = AuthState.Authenticated
                }
            }
        }
    }
}
