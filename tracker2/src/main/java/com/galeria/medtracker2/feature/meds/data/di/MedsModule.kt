package com.galeria.medtracker2.feature.meds.data.di

import com.galeria.medtracker2.core.notification.ScheduleNotification
import com.galeria.medtracker2.core.notification.ScheduleNotificationRepo
import com.galeria.medtracker2.database.AppDatabase
import com.galeria.medtracker2.feature.meds.data.local.intakes.IntakeDao
import com.galeria.medtracker2.feature.meds.data.local.schedule.MedicationRegimenDao
import com.galeria.medtracker2.feature.meds.data.repository.MedsRepositoryImpl
import com.galeria.medtracker2.feature.meds.domain.MedsRepository
import com.galeria.medtracker2.feature.meds.presentation.MedicationRegimenRepo
import com.galeria.medtracker2.feature.meds.presentation.MedicationRegimenRepoImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MedsModule {

    @Binds
    abstract fun bindMedsRepository(
        medsRepository: MedsRepositoryImpl
    ): MedsRepository
}
@Module
@InstallIn(SingletonComponent::class)
abstract class ScheduleNotificationModule {

    @Binds
    abstract fun bindMedsRepository(
        medsRepository: ScheduleNotification
    ): ScheduleNotificationRepo
}

@Module
@InstallIn(SingletonComponent::class)
object IntakeDaoModule {

    @Provides
    fun providesIntakeDao(
        database: AppDatabase,
    ): IntakeDao = database.intakeDao()

}
@Module
@InstallIn(SingletonComponent::class)
object MedicationRegimenDaoModule {

    @Provides
    fun provideMedicationRegimenDao(
        database: AppDatabase,
    ): MedicationRegimenDao = database.medicationRegimenDao()

}

@Module
@InstallIn(SingletonComponent::class)
abstract class MedsRegModule {

    @Binds
    abstract fun bindMedsRegimenRepository(
        medsRepository: MedicationRegimenRepoImp
    ): MedicationRegimenRepo
}
