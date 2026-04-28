package com.galeria.medtracker2.feature.intakes.data.di

import com.galeria.medtracker2.feature.intakes.domain.IntakesRepository
import com.galeria.medtracker2.feature.intakes.domain.IntakesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class IntakesModule {

    @Binds
    abstract fun bindIntakesRepository(
        medsRepository: IntakesRepositoryImpl
    ): IntakesRepository
}

/*
@Module
@InstallIn(SingletonComponent::class)
object IntakeDaoModule {

    @Provides
    fun providesIntakeDao(
        database: AppDatabase,
    ): IntakeDao = database.intakeDao()

}*/
