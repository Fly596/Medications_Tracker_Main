package com.galeria.medtracker2.core.notifications.di

import com.galeria.medtracker2.core.notifications.ScheduleNotificationRepo
import com.galeria.medtracker2.core.notifications.data.ScheduleNotificationRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    abstract fun bindScheduleNotificationRepo(
        impl: ScheduleNotificationRepoImpl
    ): ScheduleNotificationRepo

}