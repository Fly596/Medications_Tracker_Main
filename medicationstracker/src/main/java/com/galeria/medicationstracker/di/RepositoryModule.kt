package com.galeria.medicationstracker.di

import com.galeria.medicationstracker.data.imp.AuthRepository
import com.galeria.medicationstracker.data.imp.AuthRepositoryImpl
import com.galeria.medicationstracker.data.imp.NewMedicationRepository
import com.galeria.medicationstracker.data.imp.NewMedicationRepositoryImpl
import com.galeria.medicationstracker.data.imp.NewUserRepository
import com.galeria.medicationstracker.data.imp.NewUserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
    
    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: NewUserRepositoryImpl): NewUserRepository
    
    @Binds
    abstract fun bindMedicationRepository(
        medicationRepositoryImpl: NewMedicationRepositoryImpl
    ): NewMedicationRepository
}
