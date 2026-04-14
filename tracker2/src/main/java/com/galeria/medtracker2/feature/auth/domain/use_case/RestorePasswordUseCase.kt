package com.galeria.medtracker2.feature.auth.domain.use_case

import com.galeria.medtracker2.feature.auth.domain.AuthRepository
import javax.inject.Inject

class RestorePasswordUseCase @Inject constructor(private val repository: AuthRepository) {
    
    suspend operator fun invoke(email: String): Boolean {
        if (email.isBlank()) {
            return false
        }
        return repository.restorePassword(email)
    }
}
