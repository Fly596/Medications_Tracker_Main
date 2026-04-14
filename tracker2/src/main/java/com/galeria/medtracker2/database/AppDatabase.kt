package com.galeria.medtracker2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.galeria.medtracker2.feature.auth.data.source.local.UserDao
import com.galeria.medtracker2.feature.auth.data.source.local.UserEntity
import com.galeria.medtracker2.feature.meds.data.source.local.MedicationDao
import com.galeria.medtracker2.feature.meds.data.source.local.MedicationEntity

@Database(
    entities =
        [
            UserEntity::class,
            MedicationEntity::class,
        ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun medicationDao(): MedicationDao
}