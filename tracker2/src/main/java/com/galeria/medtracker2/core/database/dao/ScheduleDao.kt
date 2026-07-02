package com.galeria.medtracker2.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.galeria.medtracker2.domain.model.MedicationCourseSummary
import com.galeria.medtracker2.domain.model.ScheduledIntakeDetails
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ScheduleDao {

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
    fun getActiveCoursesStream(): Flow<List<MedicationCourseSummary>>

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
            WHERE m.id = :courseId  
            """
    )
    suspend fun getActiveCourseById(courseId: UUID): MedicationCourseSummary

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
    fun getFullScheduleStream(): Flow<List<ScheduledIntakeDetails>>
}

/*    @Query(
    """
        SELECT
            m.name AS 'medicationName',
            mr.id AS 'courseId',
            mr.doseMg,
            mr.startDate,
            mr.endDate,
            COUNT(std.id) AS 'plannedCount',
            COUNT(CASE WHEN i.isTaken = 1 THEN 1 END) AS 'takenCount'
        FROM medications AS m
        JOIN medication_courses AS mr ON m.id = mr.medicationId
        LEFT JOIN planned_intakes AS std ON mr.id = std.courseId
        LEFT JOIN intake_logs AS i ON std.id = i.plannedIntakeId
        GROUP BY m.id, mr.id, mr.doseMg, mr.startDate, mr.endDate
    """
)
fun getScheduleStatsStream(): Flow<List<MedicationScheduleStats>>*/
