package com.galeria.medtracker2.shared.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.galeria.medtracker2.feature_auth.data.source.local.UserDao
import com.galeria.medtracker2.feature_auth.data.source.local.UserEntity

@Database(
    entities =
        [
            UserEntity::class,
        ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}
