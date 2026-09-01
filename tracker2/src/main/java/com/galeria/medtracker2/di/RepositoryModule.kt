package com.galeria.medtracker2.di

import com.galeria.medtracker2.data.repository.MainIntakesRepositoryImpl
import com.galeria.medtracker2.data.repository.MedicationRepositoryImpl
import com.galeria.medtracker2.domain.repository.MainIntakesRepository
import com.galeria.medtracker2.domain.repository.MedicationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMedicationRepository(impl: MedicationRepositoryImpl): MedicationRepository

    @Binds
    abstract fun bindMainIntakesRepository(
        impl: MainIntakesRepositoryImpl
    ): MainIntakesRepository
}