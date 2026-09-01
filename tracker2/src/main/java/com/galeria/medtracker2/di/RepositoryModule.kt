package com.galeria.medtracker2.di

import com.galeria.medtracker2.data.repository.IntakesRepositoryImpl
import com.galeria.medtracker2.data.repository.MedicationRepositoryImpl
import com.galeria.medtracker2.data.repository.MedicationsCourseRepositoryImpl
import com.galeria.medtracker2.domain.repository.IntakesRepository
import com.galeria.medtracker2.domain.repository.MainIntakesRepository
import com.galeria.medtracker2.domain.repository.MainIntakesRepositoryImpl
import com.galeria.medtracker2.domain.repository.MedicationRepository
import com.galeria.medtracker2.domain.repository.MedicationsCourseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindIntakesRepository(
        medsRepository: IntakesRepositoryImpl
    ): IntakesRepository

    @Binds
    @Singleton
    abstract fun bindMedicationRepository(impl: MedicationRepositoryImpl): MedicationRepository

    @Binds
    @Singleton
    abstract fun bindMedicationCourseRepository(
        impl: MedicationsCourseRepositoryImpl
    ): MedicationsCourseRepository

    @Binds
    abstract fun bindMainIntakesRepository(
        impl: MainIntakesRepositoryImpl
    ): MainIntakesRepository
}