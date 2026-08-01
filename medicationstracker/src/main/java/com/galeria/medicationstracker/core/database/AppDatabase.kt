package com.galeria.medicationstracker.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.galeria.medicationstracker.core.database.dao.UserDao
import com.galeria.medicationstracker.core.database.entity.UserEntity

@Database(
  entities =
      [
        UserEntity::class,
      ],
  version = 1,
  exportSchema = false,
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

  abstract fun userDao(): UserDao
}
