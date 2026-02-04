package com.galeria.medicationstracker.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.galeria.medicationstracker.data.source.local.daos.IntakeDao
import com.galeria.medicationstracker.data.source.local.daos.MoodDao
import com.galeria.medicationstracker.data.source.local.entities.Intake
import com.galeria.medicationstracker.data.source.local.entities.Mood
import com.galeria.medicationstracker.data.source.local.entities.UserNote
import com.galeria.medicationstracker.feature_auth.data.source.local.UserDao
import com.galeria.medicationstracker.feature_auth.data.source.local.UserEntity
import com.galeria.medicationstracker.feature_medications.data.source.local.MedicationDao
import com.galeria.medicationstracker.feature_medications.data.source.local.MedicationEntity

@Database(
    entities =
        [
            UserEntity::class,
            MedicationEntity::class,
            Intake::class,
            UserNote::class,
            Mood::class,
        ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao

    abstract fun userDao(): UserDao

    abstract fun intakeDao(): IntakeDao

    abstract fun moodDao(): MoodDao
}
