package com.galeria.medtracker2.di

import com.galeria.medtracker2.core.notifications1.MedsAlarmScheduler
import com.galeria.medtracker2.core.notifications1.MedsNotificationManager
import com.galeria.medtracker2.core.notifications1.MedsNotificationManagerImpl
import com.galeria.medtracker2.domain.repository.AlarmScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmModule {

    // @Binds @Singleton abstract fun bindAlarmScheduler(impl: AlarmSchedulerImpl): AlarmScheduler
    @Binds
    @Singleton
    abstract fun bindAlarmScheduler(impl: MedsAlarmScheduler): AlarmScheduler

    @Binds
    @Singleton
    abstract fun bindMedsNotificationManager(
        impl: MedsNotificationManagerImpl
    ): MedsNotificationManager
}
