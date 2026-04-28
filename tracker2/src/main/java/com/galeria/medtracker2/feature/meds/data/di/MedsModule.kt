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

//@Module
//@InstallIn(SingletonComponent::class)
//object MedicationRegimenDaoModule {
//
//    @Provides
//    fun provideMedicationRegimenDao(
//        database: AppDatabase,
//    ): MedicationRegimenDao = database.medicationRegimenDao()
//
//}

@Module
@InstallIn(SingletonComponent::class)
abstract class MedsRegModule {

    @Binds
    abstract fun bindMedsRegimenRepository(
        medsRepository: MedicationRegimenRepoImp
    ): MedicationRegimenRepo
}
