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
    val id: UUID,
    val name: String,
    val pricing: Int, // цена в 0.01 у.е.
    val unit: String,
    val creationTimestamp: Instant,
)
