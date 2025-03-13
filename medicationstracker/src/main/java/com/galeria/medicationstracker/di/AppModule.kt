package com.galeria.medicationstracker.di

import com.galeria.medicationstracker.data.MedicationRepository
import com.galeria.medicationstracker.data.MedicationRepositoryImpl
import com.galeria.medicationstracker.data.UserMedicationsRepository
import com.galeria.medicationstracker.data.UserMedicationsRepositoryImpl
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
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): UserMedicationsRepository {
        return UserMedicationsRepositoryImpl(firestore, auth)
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
    fun provideMedicationRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): MedicationRepository {
        return MedicationRepositoryImpl(firestore, auth)
    }
}