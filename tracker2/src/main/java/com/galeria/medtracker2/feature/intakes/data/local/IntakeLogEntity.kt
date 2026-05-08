package com.galeria.medtracker2.feature.intakes.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.galeria.medtracker2.feature.meds.data.local.plannedintake.PlannedIntakeEntity
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "intakes",
    foreignKeys = [
        ForeignKey(
            entity = PlannedIntakeEntity::class,
            parentColumns = ["id"],
            childColumns = ["plannedIntakeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("plannedIntakeId")]
)
data class IntakeLogEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val plannedIntakeId: UUID,
    val actualIntakeDateTime: Long,
    val status: Boolean = false,
    val notes: String?
)

