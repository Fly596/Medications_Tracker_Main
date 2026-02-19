package com.galeria.medicationstracker.di

import android.content.Context
import androidx.room.Room
import com.galeria.medicationstracker.data.source.local.AppDatabase
import com.galeria.medicationstracker.feature_auth.data.repository.AuthRepositoryImpl
import com.galeria.medicationstracker.feature_auth.data.source.local.UserDao
import com.galeria.medicationstracker.feature_auth.domain.repository.AuthRepository
import com.galeria.medicationstracker.feature_medications.data.repository.MedicationRepositoryImpl
import com.galeria.medicationstracker.feature_medications.data.source.local.MedicationDao
import com.galeria.medicationstracker.feature_medications.domain.repository.MedicationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides @Singleton fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()

    @Provides fun provideMedicationDao(database: AppDatabase) = database.medicationDao()

    @Provides fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides fun provideRegimentsDao(database: AppDatabase) = database.regimentsDao()

    @Provides
    @Singleton
    fun bindMedicationRepository(
        firestore: FirebaseFirestore,
        dispatcher: CoroutineDispatcher,
        medicationDao: MedicationDao
    ): MedicationRepository =
        MedicationRepositoryImpl(firestore, FirebaseAuth.getInstance(), dispatcher, medicationDao)

    @Provides
    @Singleton
    fun bindAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        userDao: UserDao
    ): AuthRepository = AuthRepositoryImpl(auth, firestore, userDao)


}
