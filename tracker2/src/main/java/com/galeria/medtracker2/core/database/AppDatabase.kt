package com.galeria.medtracker2.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.galeria.medtracker2.core.database.dao.IntakeDao
import com.galeria.medtracker2.core.database.dao.MedicationCourseDao
import com.galeria.medtracker2.core.database.dao.MedicationDao
import com.galeria.medtracker2.core.database.dao.PlannedIntakeDao
import com.galeria.medtracker2.core.database.dao.ScheduleDao
import com.galeria.medtracker2.core.database.entity.IntakeLogEntity
import com.galeria.medtracker2.core.database.entity.MedicationCourseEntity
import com.galeria.medtracker2.core.database.entity.MedicationEntity
import com.galeria.medtracker2.core.database.entity.PlannedIntakeEntity

@Database(
    entities =
            [
                MedicationEntity::class,
                IntakeLogEntity::class,
                MedicationCourseEntity::class,
                PlannedIntakeEntity::class,
            ],
    version = 1,
    exportSchema = false,
) // @TypeConverters(Converters::class, DateConverters::class)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao

    abstract fun medicationCourseDao(): MedicationCourseDao

    abstract fun plannedIntakeDao(): PlannedIntakeDao

    abstract fun intakeDao(): IntakeDao

    abstract fun medicationScheduleDao(): ScheduleDao
}
