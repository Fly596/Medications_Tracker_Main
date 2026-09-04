package com.galeria.medtracker2.di

import com.galeria.medtracker2.data.repository.IntakesRepositoryImpl
import com.galeria.medtracker2.data.repository.MedicationRepositoryImpl
import com.galeria.medtracker2.domain.repository.IntakesRepository
import com.galeria.medtracker2.domain.repository.MedicationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMedicationRepository(impl: MedicationRepositoryImpl): MedicationRepository

    @Binds
    abstract fun bindMainIntakesRepository(
        impl: IntakesRepositoryImpl
    ): IntakesRepository

    @Binds
    abstract fun bingIntakeRepository(impl: IntakesRepositoryImpl): IntakesRepository
}