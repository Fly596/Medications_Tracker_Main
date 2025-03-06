package com.galeria.medicationstracker.ui.screens.auth.signup

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.SnackbarController
import com.galeria.medicationstracker.SnackbarEvent
import com.galeria.medicationstracker.data.BloodType
import com.galeria.medicationstracker.data.UserProfile
import com.galeria.medicationstracker.data.UserType
import com.galeria.medicationstracker.utils.FirestoreFunctions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class SignupScreenState(
    val uid: String = "",
    val age: Int = 18,
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: Timestamp = Timestamp.now(),
    val sex: String = "",
    val bloodType: BloodType = BloodType.UNKNOWN,
    val weight: Float = 0.0f,
    val height: Float = 0.0f,
    val email: String = "",
    val emailErrorMessage: String? = null,
    val password: String = "",
    val passwordErrorMessage: String? = null,
    val showPassword: Boolean = false,
    var userType: UserType = UserType.PATIENT,
)

class SignupScreenViewModel : ViewModel() {
    
    val auth = FirebaseAuth.getInstance()
    var signupScreenState = MutableStateFlow(SignupScreenState())
        private set
    private val db = FirestoreFunctions.FirestoreService.db
    
    private fun validateEmail(): Boolean {
        val emailInput = signupScreenState.value.email.trim()
        var isValid = true
        var errorMessage = ""
        
        if (emailInput.isBlank() || emailInput.isEmpty()) {
            errorMessage = "Email cannot be empty"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            errorMessage = "Wrong email format"
            isValid = false
        }
        
        signupScreenState.value =
            signupScreenState.value.copy(emailErrorMessage = errorMessage)
        return isValid
    }
    
    private fun validatePassword(): Boolean {
        val passwordInput = signupScreenState.value.password
        var isValid = true
        var errorMessage = ""
        
        if (passwordInput.isBlank() || passwordInput.isEmpty()) {
            errorMessage = "Password cannot be empty"
            isValid = false
        } else if (passwordInput.length < 6) {
            errorMessage = "Password must be at least 6 characters"
            isValid = false
        }
        
        signupScreenState.value =
            signupScreenState.value.copy(passwordErrorMessage = errorMessage)
        return isValid
    }
    
    fun onRegisterClick(context: Context, onSignupSuccess: () -> Unit) {
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        
        if (isEmailValid && isPasswordValid) {
            auth
                .createUserWithEmailAndPassword(
                    signupScreenState.value.email.trim(),
                    signupScreenState.value.password.trim()
                )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Получение id пользователя.
                        val userId = task.result?.user?.uid ?: ""
                        updateUserId(userId)
                        // Добавление нового юзера в БД.
                        addUserData()
                        // Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show()
                        viewModelScope.launch {
                            SnackbarController.sendEvent(
                                event = SnackbarEvent(
                                    message = "Account Created!"
                                )
                            )
                        }
                        onSignupSuccess.invoke()
                    } else {
                        viewModelScope.launch {
                            SnackbarController.sendEvent(
                                event = SnackbarEvent(message = "Account Creation Failed${task.exception?.message}")
                            )
                        }
                    }
                }
        } else {
            viewModelScope.launch {
                SnackbarController.sendEvent(event = SnackbarEvent(message = "Invalid email or password."))
            }
        }
    }
    
    fun addUserData() {
        val newUser = UserProfile(
            signupScreenState.value.uid,
            signupScreenState.value.firstName,
            signupScreenState.value.lastName,
            signupScreenState.value.weight,
            signupScreenState.value.height,
            signupScreenState.value.email,
            signupScreenState.value.dateOfBirth,
            signupScreenState.value.bloodType,
            signupScreenState.value.sex
        )
        db.collection("User").document(newUser.email.toString()).set(newUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        SnackbarController.sendEvent(
                            event = SnackbarEvent(
                                message = "Account Created!"
                            )
                        )
                    }
                } else {
                    viewModelScope.launch {
                        SnackbarController.sendEvent(
                            event = SnackbarEvent(
                                message = "Something went wrong :("
                            )
                        )
                    }
                }
            }
    }
    
    fun updateUserAge(input: Int) {
        signupScreenState.value = signupScreenState.value.copy(age = input)
    }
    
    fun updateUserName(input: String) {
        signupScreenState.value =
            signupScreenState.value.copy(firstName = input)
    }
    
    fun updateUserType(input: UserType) {
        signupScreenState.value = signupScreenState.value.copy(userType = input)
    }
    
    fun updateUserId(input: String) {
        signupScreenState.value = signupScreenState.value.copy(uid = input)
    }
    
    fun updateEmail(input: String) {
        signupScreenState.value = signupScreenState.value.copy(email = input)
    }
    
    fun updatePassword(input: String) {
        signupScreenState.value = signupScreenState.value.copy(password = input)
    }
    
    fun isShowPasswordChecked(input: Boolean) {
        signupScreenState.value =
            signupScreenState.value.copy(showPassword = !input)
    }
}
