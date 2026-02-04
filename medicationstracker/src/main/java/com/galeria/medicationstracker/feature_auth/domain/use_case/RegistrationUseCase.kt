package com.galeria.medicationstracker.feature_auth.domain.use_case

import com.galeria.medicationstracker.feature_auth.domain.repository.AuthRepository
import com.galeria.medicationstracker.utils.Response
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Response<FirebaseUser> {
        if (email.isBlank() || password.isBlank()) {
            return Response.Error("Email and password can't be empty")
        }
        return repository.register(email, password)
    }
}
