package com.galeria.medicationstracker.di

import android.content.Context
import androidx.room.Room
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
import com.galeria.medicationstracker.data.local.AppDatabase
import com.galeria.medicationstracker.data.local.MedicationDao
import com.galeria.medicationstracker.data.network.AuthRepository
import com.galeria.medicationstracker.data.network.AuthRepositoryImpl
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
