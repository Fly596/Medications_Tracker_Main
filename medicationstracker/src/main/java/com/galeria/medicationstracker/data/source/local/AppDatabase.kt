package com.galeria.medicationstracker.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.galeria.medicationstracker.data.source.local.entities.Intake
import com.galeria.medicationstracker.feature_auth.data.source.local.UserDao
import com.galeria.medicationstracker.feature_auth.data.source.local.UserEntity
import com.galeria.medicationstracker.feature_medications.data.source.local.MedicationDao
import com.galeria.medicationstracker.feature_medications.data.source.local.MedicationEntity
import com.galeria.medicationstracker.feature_medications.data.source.local.RegimentsDao
import com.galeria.medicationstracker.feature_medications.data.source.local.RegimentsEntity

@Database(
    entities =
        [
            MedicationEntity::class,
            RegimentsEntity::class,
            UserEntity::class,
            Intake::class,
        ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class, DateConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao

    abstract fun userDao(): UserDao

    abstract fun regimentsDao( ): RegimentsDao


}
