package com.galeria.medtracker2.di

import android.content.Context
import androidx.room.Room
import com.galeria.medtracker2.feature_auth.data.repository.AuthRepositoryImpl
import com.galeria.medtracker2.feature_auth.data.source.local.UserDao
import com.galeria.medtracker2.feature_auth.domain.repository.AuthRepository
import com.galeria.medtracker2.shared.data.AppDatabase
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

    @Provides @Singleton fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides @Singleton fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()

    @Provides fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides
    @Singleton
    fun bindNewAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        userDao: UserDao
    ): AuthRepository = AuthRepositoryImpl(auth, firestore, userDao)
}
