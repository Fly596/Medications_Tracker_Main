package com.galeria.medicationstracker.utils

object ValidateUtils {
    
    fun validateEmail(email: String): String? = when {
        email.isBlank() -> "Email cannot be empty"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() -> "Wrong email format"
        
        else -> null
    }
    
    fun validatePassword(password: String): String? = when {
        password.isBlank() -> "Password cannot be empty"
        password.length < 6 -> "Password must be at least 6 characters"
        else -> null
    }
}