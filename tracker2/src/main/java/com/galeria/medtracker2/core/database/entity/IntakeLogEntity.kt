package com.galeria.medtracker2.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "intake_logs", // Было intakes
    foreignKeys =
        [
            ForeignKey(
                entity = PlannedIntakeEntity::class,
                parentColumns = ["id"],
                childColumns = ["plannedIntakeId"],
                onDelete = ForeignKey.Companion.CASCADE,
            )
        ],
    indices = [Index("plannedIntakeId")],
)
data class IntakeLogEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val plannedIntakeId: UUID,
    val actualTimestamp: Long,
    val isTaken: Boolean = false,
    val notes: String? = null,
)