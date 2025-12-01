package com.galeria.medicationstracker.feature_auth.domain.use_case

import com.galeria.medicationstracker.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class RestorePasswordUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String): Boolean {
        if (email.isBlank()) {
            return false
        }
        return repository.restorePassword(email)
    }
}
