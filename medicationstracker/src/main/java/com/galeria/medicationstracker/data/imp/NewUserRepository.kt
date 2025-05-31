package com.galeria.medicationstracker.data.imp

interface NewUserRepository {

    suspend fun addUser(user: NewUser): Result<String>

    suspend fun getUser(userId: String): Result<NewUser>

    suspend fun updateUser(user: NewUser): Result<Unit>
}
