package com.galeria.medtracker2.feature.meds.data.di

import com.galeria.medtracker2.feature.meds.data.MedsRepositoryImpl
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