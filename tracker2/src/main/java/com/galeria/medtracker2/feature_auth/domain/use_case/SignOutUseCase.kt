package com.galeria.medtracker2.feature_auth.domain.use_case

import com.galeria.medtracker2.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke() {
        repository.signOut()
    }
}
