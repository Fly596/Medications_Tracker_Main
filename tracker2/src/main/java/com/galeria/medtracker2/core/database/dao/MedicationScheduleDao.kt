package com.galeria.medtracker2.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.galeria.medtracker2.domain.model.MedicationCourseSummary
import com.galeria.medtracker2.domain.model.ScheduledIntakeDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationScheduleDao {

    @Query(
        """
            SELECT
                m.id AS 'medicationId',
                m.name,
                mr.doseMg,
                mr.startDate,
                mr.endDate
            FROM medications AS m
            JOIN medication_courses AS mr ON m.id = mr.medicationId
            """
    )
    fun getCourseSummaries(): Flow<List<MedicationCourseSummary>>

    @Query(
        """
            SELECT 
                std.id AS 'plannedIntakeId',
                mr.id AS 'courseId',
                m.name AS 'medicationName',
                mr.doseMg,
                std.scheduledTimestamp,
                i.isTaken AS isTaken
            FROM medications AS m
            JOIN medication_courses AS mr ON m.id = mr.medicationId
            INNER JOIN planned_intakes AS std ON mr.id = std.courseId
            LEFT JOIN intake_logs AS i ON std.id = i.plannedIntakeId
            ORDER BY std.scheduledTimestamp
            """
    )
    fun getScheduledIntakesWithDetails(): Flow<List<ScheduledIntakeDetails>>
}