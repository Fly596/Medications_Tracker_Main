package com.galeria.medicationstracker.di

import android.content.Context
import androidx.room.Room
import com.galeria.medicationstracker.data.repository.NewIntakeRepository
import com.galeria.medicationstracker.data.repository.NewIntakeRepositoryImpl
import com.galeria.medicationstracker.data.repository.NewMedicationRepository
import com.galeria.medicationstracker.data.repository.NewMedicationRepositoryImpl
import com.galeria.medicationstracker.data.repository.NewMoodRepository
import com.galeria.medicationstracker.data.repository.NewMoodRepositoryImpl
import com.galeria.medicationstracker.data.repository.NewNoteRepository
import com.galeria.medicationstracker.data.repository.NewNoteRepositoryImpl
import com.galeria.medicationstracker.data.repository.NewUserRepository
import com.galeria.medicationstracker.data.repository.NewUserRepositoryImpl
import com.galeria.medicationstracker.data.source.local.AppDatabase
import com.galeria.medicationstracker.data.source.local.daos.MedicationDao
import com.galeria.medicationstracker.data.source.network.AuthRepository
import com.galeria.medicationstracker.data.source.network.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .build()
    
    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()
    
    @Provides
    fun provideMedicationDao(database: AppDatabase) = database.medicationDao()
    
    @Provides
    fun provideIntakeDao(database: AppDatabase) = database.intakeDao()

    @Provides
    @Singleton
    fun bindNewAuthRepository(auth: FirebaseAuth): AuthRepository =
        AuthRepositoryImpl(auth)

    @Provides
    @Singleton
    fun bindNewIntakeRepository(firestore: FirebaseFirestore): NewIntakeRepository =
        NewIntakeRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun bindNewMedicationRepository(
        firestore: FirebaseFirestore,
        medicationDao: MedicationDao,
    ): NewMedicationRepository =
        NewMedicationRepositoryImpl(firestore, medicationDao)

    @Provides
    @Singleton
    fun bindNewUserRepository(firestore: FirebaseFirestore): NewUserRepository =
        NewUserRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun bindNewMoodRepository(firestore: FirebaseFirestore): NewMoodRepository =
        NewMoodRepositoryImpl(firestore)
    
    @Provides
    @Singleton
    fun bindNewNoteRepository(firestore: FirebaseFirestore): NewNoteRepository =
        NewNoteRepositoryImpl(firestore)
}
