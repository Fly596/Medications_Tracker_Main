package com.galeria.medicationstracker.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides @Singleton fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    // region OLD FirebaseRepositoryModule
    /*
    @Provides
    fun provideFirestoreRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
    ): UserMedicationsRepository {
        return UserMedicationsRepositoryImpl(firestore, auth)
    }

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore, auth: FirebaseAuth): UserRepository {
        return UserRepositoryImpl(firestore, auth)
    }

    @Provides
    @Singleton
    fun provideMedicationRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
    ): MedicationRepository {
        return MedicationRepositoryImpl(firestore, auth)
    } */
    // endregion
}
