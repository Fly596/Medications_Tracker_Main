@file:OptIn(ExperimentalUuidApi::class)

package com.galeria.medtracker2.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val creationTimestamp: Long = Instant.now().toEpochMilli(),
)
