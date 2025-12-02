package com.galeria.medtracker2.feature_auth.domain.use_case

import com.galeria.medtracker2.feature_auth.domain.repository.AuthRepository
import com.galeria.medtracker2.utils.ResourceRes
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

/*
*
class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        defaultCurrency: String,
    ): AuthResponse {
        if (email.isBlank() || password.isBlank()) {
            return AuthResponse.Error("Email and password can't be empty")
        }
        var registerResult = repository.OLDregisterReturn(
            email,
            password,
            name,
            defaultCurrency
        )


        return repository.OLDregisterReturn(
            email,
            password,
            name,
            defaultCurrency
        )
    }
}
*/
class LoginUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String): ResourceRes<FirebaseUser> {
        if (email.isBlank() || password.isBlank()) {
            return ResourceRes.Error("Email and password can't be empty")
        }
        return repository.login(email = email, password = password)
    }
}
