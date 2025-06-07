package com.galeria.medicationstracker.di

import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.AuthRepositoryImpl
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
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NewRepositoryModule {

    @Binds abstract fun bindNewAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds abstract fun bindNewIntakeRepository(impl: NewIntakeRepositoryImpl): NewIntakeRepository

    @Binds
    abstract fun bindNewMedicationRepository(
        impl: NewMedicationRepositoryImpl
    ): NewMedicationRepository

    @Binds abstract fun bindNewUserRepository(impl: NewUserRepositoryImpl): NewUserRepository

    @Binds abstract fun bindNewMoodRepository(impl: NewMoodRepositoryImpl): NewMoodRepository

    @Binds abstract fun bindNewNoteRepository(impl: NewNoteRepositoryImpl): NewNoteRepository
}
