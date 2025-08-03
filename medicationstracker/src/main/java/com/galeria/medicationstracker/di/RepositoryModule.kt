package com.galeria.medicationstracker.di

import com.galeria.medicationstracker.data.NewIntakeRepository
import com.galeria.medicationstracker.data.NewIntakeRepositoryImpl
import com.galeria.medicationstracker.data.NewMedicationRepository
import com.galeria.medicationstracker.data.NewMedicationRepositoryImpl
import com.galeria.medicationstracker.data.NewMoodRepository
import com.galeria.medicationstracker.data.NewMoodRepositoryImpl
import com.galeria.medicationstracker.data.NewNoteRepository
import com.galeria.medicationstracker.data.NewNoteRepositoryImpl
import com.galeria.medicationstracker.data.NewUserRepository
import com.galeria.medicationstracker.data.NewUserRepositoryImpl
import com.galeria.medicationstracker.data.network.AuthRepository
import com.galeria.medicationstracker.data.network.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NewRepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindNewAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindNewIntakeRepository(impl: NewIntakeRepositoryImpl): NewIntakeRepository
    
    @Binds
    @Singleton
    abstract fun bindNewMedicationRepository(
        impl: NewMedicationRepositoryImpl
    ): NewMedicationRepository
    
    @Binds
    @Singleton
    abstract fun bindNewUserRepository(impl: NewUserRepositoryImpl): NewUserRepository
    
    @Binds
    @Singleton
    abstract fun bindNewMoodRepository(impl: NewMoodRepositoryImpl): NewMoodRepository
    
    @Binds
    @Singleton
    abstract fun bindNewNoteRepository(impl: NewNoteRepositoryImpl): NewNoteRepository
}
