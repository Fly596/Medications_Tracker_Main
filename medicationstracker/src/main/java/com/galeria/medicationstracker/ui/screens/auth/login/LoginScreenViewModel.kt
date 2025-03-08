package com.galeria.medicationstracker.ui.screens.auth.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.SnackbarController
import com.galeria.medicationstracker.SnackbarEvent
import com.galeria.medicationstracker.data.UserType
import com.galeria.medicationstracker.utils.FirestoreFunctions
import com.galeria.medicationstracker.utils.ValidateUtils
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class LoginScreenState(
    val email: String = "tom@gmail.com",
    val password: String = "tomtom",
    val emailError: String? = null,
    val passwordError: String? = null,
    val showPassword: Boolean = false,
    val userType: UserType? = null,
)


class LoginScreenViewModel : ViewModel() {
    
    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val loginScreenState = _loginScreenState.asStateFlow()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirestoreFunctions.FirestoreService.db
    private var currentUserType = MutableStateFlow<String?>(null)
    
    // запрашивает тип юзера.
    private fun getUserType() {
        val userEmail = firebaseAuth.currentUser?.email
        
        viewModelScope.launch {
            if (userEmail == null) {
                currentUserType.value = null
                return@launch
            }
            
            try {
                val snapshot = db.collection("User")
                    .document(userEmail)
                    .get()
                    .await()
                
                if (snapshot.exists()) {
                    currentUserType.value =
                        snapshot.data?.get("type") as? String
                } else {
                    currentUserType.value = null
                }
            } catch (exception: FirebaseFirestoreException) {
                Log.e(
                    "MainViewModel",
                    "Error getting user type",
                    exception
                )
            }
        }
    }
    
    private suspend fun signIn(email: String, password: String): AuthResult =
        withContext(Dispatchers.IO) {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        }
    
    private suspend fun fetchUserType(email: String): UserType? =
        withContext(Dispatchers.IO) {
            val snapshot = db.collection("User")
                .document(email)
                .get()
                .await()
            
            snapshot.getString("type")?.let {
                UserType.valueOf(it.uppercase())
            }
        }
    
    
    private fun validateInputs() {
        val state = _loginScreenState.value
        _loginScreenState.value = state.copy(
            emailError = ValidateUtils.validateEmail(state.email),
            passwordError = ValidateUtils.validatePassword(state.password)
        )
    }
    
    fun onSignInClick(
        email: String,
        password: String,
        onLoginClick: (userType: UserType) -> Unit
    ) {
        viewModelScope.launch {
            validateInputs()
            val state = _loginScreenState.value
            
            if (state.emailError == null || state.passwordError == null) {
                try {
                    signIn(email, password)
                    val userType = fetchUserType(email)
                        ?: throw Exception("User type not found")
                    onLoginClick(userType)
                    SnackbarController.sendEvent(SnackbarEvent("Login Successful!"))
                } catch (e: Exception) {
                    SnackbarController.sendEvent(SnackbarEvent("Invalid email or password."))
                }
            }
        }
    }
    
    fun updateEmail(input: String) {
        _loginScreenState.value = _loginScreenState.value.copy(email = input)
    }
    
    fun updatePassword(input: String) {
        _loginScreenState.value = _loginScreenState.value.copy(password = input)
    }
    
    fun updateShowPassword(show: Boolean) {
        _loginScreenState.value =
            _loginScreenState.value.copy(showPassword = show)
    }
}