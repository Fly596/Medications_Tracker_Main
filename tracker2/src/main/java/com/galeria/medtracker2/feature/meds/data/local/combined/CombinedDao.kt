package com.galeria.medtracker2.feature.meds.data.local.combined

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CombinedDao {

    @Query(
        """
            SELECT m.name, mr.doseMg, mr.startDate, mr.endDate
            FROM medications AS m JOIN medications_regimens AS mr
            ON m.id = mr.medicationId
            """
    )
    fun getMedicationsNameDoseDates(): Flow<List<RegimentWithNameDoseDate>>

    @Query(
        """
            SELECT std.id AS 'idDateTime', mr.id AS 'idRegiment', m.name, mr.doseMg, std.scheduledIntakeDateTime
            FROM medications AS m
            JOIN medications_regimens AS mr ON m.id = mr.medicationId
            INNER JOIN scheduled_date_times AS std ON mr.id = std.medicationScheduleId
            """
    )
    fun getFullScheduleDateTimes(): Flow<List<FullSchedule>>
}