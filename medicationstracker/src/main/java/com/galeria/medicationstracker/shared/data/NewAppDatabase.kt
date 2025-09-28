package com.galeria.medicationstracker.shared.data

import android.R.attr.version
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.galeria.medicationstracker.data.local.Converters
import com.galeria.medicationstracker.data.local.Intake
import com.galeria.medicationstracker.data.local.IntakeDao
import com.galeria.medicationstracker.data.local.Medication
import com.galeria.medicationstracker.data.local.MedicationDao
import com.galeria.medicationstracker.data.local.Mood
import com.galeria.medicationstracker.data.local.User
import com.galeria.medicationstracker.data.local.UserDao
import com.galeria.medicationstracker.data.local.UserNote


@Database(
    entities = [
        //
        ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun medicationDao(): MedicationDao
    
    abstract fun userDao(): UserDao
    
    abstract fun intakeDao(): IntakeDao
}
