package com.galeria.medicationstracker.di

import com.galeria.medicationstracker.data.imp.AuthRepository
import com.galeria.medicationstracker.data.imp.AuthRepositoryImpl
import com.galeria.medicationstracker.data.imp.NewIntakeRepository
import com.galeria.medicationstracker.data.imp.NewIntakeRepositoryImpl
import com.galeria.medicationstracker.data.imp.NewMedicationRepository
import com.galeria.medicationstracker.data.imp.NewMedicationRepositoryImpl
import com.galeria.medicationstracker.data.imp.NewMoodRepository
import com.galeria.medicationstracker.data.imp.NewMoodRepositoryImpl
import com.galeria.medicationstracker.data.imp.NewNoteRepository
import com.galeria.medicationstracker.data.imp.NewNoteRepositoryImpl
import com.galeria.medicationstracker.data.imp.NewUserRepository
import com.galeria.medicationstracker.data.imp.NewUserRepositoryImpl
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
