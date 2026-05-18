package com.galeria.medtracker2.feature.meds.data.di

import com.galeria.medtracker2.feature.meds.data.repository.MedicationRepositoryImpl
import com.galeria.medtracker2.feature.meds.data.repository.MedicationScheduleIntakesRepositoryImp
import com.galeria.medtracker2.feature.meds.domain.MedicationRepository
import com.galeria.medtracker2.feature.meds.domain.MedicationScheduleIntakesRepository
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
    @Singleton
    abstract fun bindMedicationCourseRepository(
        impl: MedicationScheduleIntakesRepositoryImp
    ): MedicationScheduleIntakesRepository

}
