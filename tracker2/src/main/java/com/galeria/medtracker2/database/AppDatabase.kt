package com.galeria.medtracker2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.galeria.medtracker2.feature.intakes.data.local.IntakeDao
import com.galeria.medtracker2.feature.intakes.data.local.IntakeEntity
import com.galeria.medtracker2.feature.meds.data.local.medication.MedicationDao
import com.galeria.medtracker2.feature.meds.data.local.medication.MedicationEntity
import com.galeria.medtracker2.feature.meds.data.local.schedule.MedicationRegimenDao
import com.galeria.medtracker2.feature.meds.data.local.schedule.MedicationRegimenEntity
import com.galeria.medtracker2.feature.meds.data.local.schedule.ScheduledDateTimeDao
import com.galeria.medtracker2.feature.meds.data.local.schedule.ScheduledDateTimeEntity

@Database(
    entities =
        [
            MedicationEntity::class,
            IntakeEntity::class,
            MedicationRegimenEntity::class,
            ScheduledDateTimeEntity::class,
        ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao

    abstract fun medicationRegimenDao(): MedicationRegimenDao
    abstract fun scheduledDateTimeDao(): ScheduledDateTimeDao
    abstract fun intakeDao(): IntakeDao

}