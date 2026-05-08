package com.galeria.medtracker2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.galeria.medtracker2.core.common.data.CombinedDao
import com.galeria.medtracker2.feature.intakes.data.local.IntakeDao
import com.galeria.medtracker2.feature.intakes.data.local.IntakeLogEntity
import com.galeria.medtracker2.feature.meds.data.local.course.MedicationCourseDao
import com.galeria.medtracker2.feature.meds.data.local.course.MedicationCourseEntity
import com.galeria.medtracker2.feature.meds.data.local.medication.MedicationDao
import com.galeria.medtracker2.feature.meds.data.local.medication.MedicationEntity
import com.galeria.medtracker2.feature.meds.data.local.plannedintake.PlannedIntakeDao
import com.galeria.medtracker2.feature.meds.data.local.plannedintake.PlannedIntakeEntity

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
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao

    abstract fun medicationCourseDao(): MedicationCourseDao
    abstract fun plannedIntakeDao(): PlannedIntakeDao
    abstract fun intakeDao(): IntakeDao

    abstract fun combinedDao(): CombinedDao

}