package com.galeria.medtracker2.feature.meds.data.di

import com.galeria.medtracker2.core.notification.ScheduleNotification
import com.galeria.medtracker2.core.notification.ScheduleNotificationRepo
import com.galeria.medtracker2.feature.meds.data.repository.MedicationRegimenRepoImp
import com.galeria.medtracker2.feature.meds.data.repository.MedsRepositoryImpl
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimenRepo
import com.galeria.medtracker2.feature.meds.domain.MedsRepository
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
    abstract fun bindMedicationRepository(impl: MedsRepositoryImpl): MedsRepository

    @Binds
    @Singleton
    abstract fun bindMedicationCourseRepository(
        impl: MedicationRegimenRepoImp
    ): MedicationRegimenRepo

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(impl: ScheduleNotification): ScheduleNotificationRepo
}
