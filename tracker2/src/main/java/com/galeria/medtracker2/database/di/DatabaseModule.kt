package com.galeria.medtracker2.database.di

import android.content.Context
import androidx.room.Room
import com.galeria.medtracker2.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()

    @Provides
    fun provideMedicationDao(database: AppDatabase) = database.medicationDao()

    @Provides
    fun providePlannedIntakeDao(database: AppDatabase) = database.plannedIntakeDao()

    @Provides
    fun provideIntakeDao(database: AppDatabase) = database.intakeDao()

    @Provides
    fun provideMedicationCourseDao(database: AppDatabase) = database.medicationCourseDao()

    @Provides
    fun provideCombinedDao(database: AppDatabase) = database.combinedDao()
}
