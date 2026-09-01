package com.galeria.medtracker2.di

import android.content.Context
import androidx.room.Room
import com.galeria.medtracker2.core.database.AppDatabase
import com.galeria.medtracker2.core.database.dao.MedicationDao
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
    fun provideMedicationDao(database: AppDatabase): MedicationDao = database.medicationDao()

    @Provides
    fun provideMainIntakeDao(database: AppDatabase) = database.intakeEntityDao()
}
