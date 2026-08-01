package com.galeria.medicationstracker.di

import com.galeria.medicationstracker.core.domain.repository._AuthRepository
import com.galeria.medicationstracker.core.domain.repository._MedicationRepository
import com.galeria.medicationstracker.core.domain.repository._UserRepository
import com.galeria.medicationstracker.core.firebase.datasource.AuthDataSource
import com.galeria.medicationstracker.core.firebase.datasource.AuthDataSourceImpl
import com.galeria.medicationstracker.core.firebase.datasource.MedicationDataSource
import com.galeria.medicationstracker.core.firebase.datasource.MedicationDataSourceImpl
import com.galeria.medicationstracker.core.firebase.datasource.UserDataSource
import com.galeria.medicationstracker.core.firebase.datasource.UserDataSourceImpl
import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.AuthRepositoryImpl
import com.galeria.medicationstracker.data.NewIntakeRepository
import com.galeria.medicationstracker.data.NewIntakeRepositoryImpl
import com.galeria.medicationstracker.data.NewMedicationRepository
import com.galeria.medicationstracker.data.NewMedicationRepositoryImpl
import com.galeria.medicationstracker.data.NewMoodRepository
import com.galeria.medicationstracker.data.NewMoodRepositoryImpl
import com.galeria.medicationstracker.data.NewNoteRepository
import com.galeria.medicationstracker.data.NewNoteRepositoryImpl
import com.galeria.medicationstracker.data.NewUserRepository
import com.galeria.medicationstracker.data.NewUserRepositoryImpl
import com.galeria.medicationstracker.data._repository._AuthRepositoryImpl
import com.galeria.medicationstracker.data._repository._MedicationRepositoryImpl
import com.galeria.medicationstracker.data._repository._UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NewRepositoryModule {

  @Binds
  abstract fun bindNewAuthRepository(impl: AuthRepositoryImpl): AuthRepository

  @Binds
  abstract fun bindNewIntakeRepository(impl: NewIntakeRepositoryImpl): NewIntakeRepository

  @Binds
  abstract fun bindNewMedicationRepository(
    impl: NewMedicationRepositoryImpl
  ): NewMedicationRepository

  @Binds
  abstract fun bindNewUserRepository(impl: NewUserRepositoryImpl): NewUserRepository

  @Binds
  abstract fun bindNewMoodRepository(impl: NewMoodRepositoryImpl): NewMoodRepository

  @Binds
  abstract fun bindNewNoteRepository(impl: NewNoteRepositoryImpl): NewNoteRepository

  @Binds
  @Singleton
  abstract fun bindAuthDatasource(impl: AuthDataSourceImpl): AuthDataSource

  @Binds
  abstract fun bindAuthRepository(impl: _AuthRepositoryImpl): _AuthRepository

  @Binds
  @Singleton
  abstract fun bindUserDatasource(impl: UserDataSourceImpl): UserDataSource
  @Binds
  abstract fun bindUserRepository(impl: _UserRepositoryImpl): _UserRepository

  @Binds
  @Singleton
  abstract fun bindMedicationDatasource(impl: MedicationDataSourceImpl): MedicationDataSource

  @Binds
  @Singleton
  abstract fun bindMedicationRepository(impl: _MedicationRepositoryImpl): _MedicationRepository
}
