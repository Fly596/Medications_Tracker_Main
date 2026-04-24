package com.galeria.medtracker2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.galeria.medtracker2.feature.meds.data.local.MedicationDao
import com.galeria.medtracker2.feature.meds.data.local.MedicationEntity

@Database(
    entities =
        [
            MedicationEntity::class,
        ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun medicationDao(): MedicationDao
}