package com.galeria.medicationstracker.di

import android.content.Context
import androidx.room.Room
import com.galeria.medicationstracker.core.database.AppDatabase
import com.galeria.medicationstracker.core.database.dao.MedicationDao
import com.galeria.medicationstracker.core.database.dao.UserDao
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
  fun providesAppDatabase(
    @ApplicationContext
    context: Context,
  ): AppDatabase =
      Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database",
      )
        .build()

  @Provides
  fun providesUserDao(
    database: AppDatabase,
  ): UserDao = database.userDao()

  @Provides
  fun providesMedicationDao(
    database: AppDatabase,
  ): MedicationDao = database.medicationDao()

}