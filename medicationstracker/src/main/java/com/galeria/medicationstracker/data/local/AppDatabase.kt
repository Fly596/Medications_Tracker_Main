package com.galeria.medicationstracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        User::class,
        Medication::class,
        Intake::class,
        UserNote::class,
        Mood::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract val medicationDao: MedicationDao
    
    abstract val userDao: UserDao
    abstract val intakeDao: IntakeDao
    
}
