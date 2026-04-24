package com.galeria.medtracker2.feature.auth.data.repository

// TODO: 12/1/2025, 1:35pm
/*class AuthRepositoryImpl
@Inject
constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao,
) : AuthRepository {
    
    override suspend fun login(
        email: String,
        password: String
    ): ResultState<FirebaseUser> {
        return try {
            val authResult =
                auth.signInWithEmailAndPassword(email, password).await()
            
            if (authResult.user != null) {
                ResultState.Success(authResult.user!!)
            } else {
                ResultState.Error("User Data is null")
            }
        } catch (e: Exception) {
            ResultState.Error(e.localizedMessage ?: "Unknown Error")
        }
    }
    
    override suspend fun register(
        email: String,
        password: String
    ): ResultState<FirebaseUser> {
        return try {
            val result =
                auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            
            if (user != null) {
                ResultState.Success(user)
            } else {
                ResultState.Error("Registration failed: User is null.")
            }
        } catch (e: Exception) {
            ResultState.Error(e.localizedMessage ?: "Registration error")
        }
    }
    
    override suspend fun addUser(userDomain: UserDomain): ResultState<String> {
        return try {
            // Подготовка данных.
            val userDto = userDomain.toDto()
            val userEntity = userDomain.toEntity()
            
            firestore.collection("users").document(userDomain.id).set(userDto)
                .await()
            
            userDao.insertUser(userEntity)
            
            ResultState.Success("User saved successfully")
        } catch (e: Exception) {
            ResultState.Error(e.localizedMessage ?: "Failed to save user data")
        }
    }
    
    override suspend fun restorePassword(email: String): Boolean {
        TODO("Not yet implemented")
    }
    
    override suspend fun signOut() {
        TODO("Not yet implemented")
    }
}*/
