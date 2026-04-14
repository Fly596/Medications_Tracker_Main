package com.galeria.medtracker2.feature.auth.data.di

import com.galeria.medtracker2.feature.auth.data.repository.AuthRepositoryImpl
import com.galeria.medtracker2.feature.auth.domain.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    
    @Binds
    abstract fun bindAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        authRepository: AuthRepositoryImpl
    ): AuthRepository
}