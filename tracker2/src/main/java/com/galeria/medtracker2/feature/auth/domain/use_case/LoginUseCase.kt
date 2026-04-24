package com.galeria.medtracker2.feature.auth.domain.use_case

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
/*
class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    
    suspend operator fun invoke(
        email: String,
        password: String
    ): ResultState<FirebaseUser> {
        if (email.isBlank() || password.isBlank()) {
            return ResultState.Error("Email and password can't be empty")
        }
        return repository.login(email = email, password = password)
    }
}
*/
