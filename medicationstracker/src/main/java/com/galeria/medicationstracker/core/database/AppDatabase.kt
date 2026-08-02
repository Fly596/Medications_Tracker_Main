package com.galeria.medicationstracker.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.galeria.medicationstracker.core.database.dao.MedicationDao
import com.galeria.medicationstracker.core.database.dao.UserDao
import com.galeria.medicationstracker.core.database.entity.MedicationEntity
import com.galeria.medicationstracker.core.database.entity.UserEntity

@Database(
  entities =
      [
        UserEntity::class,
        MedicationEntity::class,
      ],
  version = 1,
  exportSchema = false,
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

  abstract fun userDao(): UserDao
  abstract fun medicationDao(): MedicationDao
}
