package com.galeria.medtracker2.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.galeria.medtracker2.core.database.dao.DosagePresetDao
import com.galeria.medtracker2.core.database.dao.IntakeDao
import com.galeria.medtracker2.core.database.dao.MedicationDao
import com.galeria.medtracker2.core.database.entity.DosagePresetEntity
import com.galeria.medtracker2.core.database.entity.IntakeEntity
import com.galeria.medtracker2.core.database.entity.MedicationEntity

@Database(
    entities =
            [
                MedicationEntity::class,
                IntakeEntity::class,
                DosagePresetEntity::class
            ],
    version = 1,
    exportSchema = false,
) // @TypeConverters(Converters::class, DateConverters::class)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao

    abstract fun intakeDao(): IntakeDao

    abstract fun dosagePresetDao(): DosagePresetDao
}
