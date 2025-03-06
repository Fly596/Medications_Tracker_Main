package com.galeria.medicationstracker.di

import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.AuthRepositoryImpl
import com.galeria.medicationstracker.data.MedicationsRepository
import com.galeria.medicationstracker.data.MedicationsRepositoryImpl
import com.galeria.medicationstracker.data.UserRepository
import com.galeria.medicationstracker.data.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    
    @Provides
    fun provideFirestoreRepository(
        firestore: FirebaseFirestore
    ): MedicationsRepository {
        return MedicationsRepositoryImpl(firestore)
    }
    
    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): UserRepository {
        return UserRepositoryImpl(firestore, auth)
    }
    
    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): AuthRepository {
        return AuthRepositoryImpl(auth)
    }
}