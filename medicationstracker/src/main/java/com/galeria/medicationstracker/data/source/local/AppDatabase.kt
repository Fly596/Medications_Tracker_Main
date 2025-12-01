package com.galeria.medicationstracker.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.galeria.medicationstracker.data.source.local.daos.IntakeDao
import com.galeria.medicationstracker.data.source.local.daos.MoodDao
import com.galeria.medicationstracker.data.source.local.daos.OLDMedicationDao
import com.galeria.medicationstracker.data.source.local.daos.UserDao
import com.galeria.medicationstracker.data.source.local.entities.Intake
import com.galeria.medicationstracker.data.source.local.entities.Medication
import com.galeria.medicationstracker.data.source.local.entities.Mood
import com.galeria.medicationstracker.data.source.local.entities.UserNote
import com.galeria.medicationstracker.feature_auth.data.source.local.User

@Database(
    entities =
        [
            User::class,
            Medication::class,
            Intake::class,
            UserNote::class,
            Mood::class,
        ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): OLDMedicationDao

    abstract fun userDao(): UserDao

    abstract fun intakeDao(): IntakeDao

    abstract fun moodDao(): MoodDao
}
