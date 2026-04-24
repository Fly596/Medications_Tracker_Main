@file:OptIn(ExperimentalUuidApi::class)

package com.galeria.medtracker2.feature.meds.data.local.medication

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey
    val id: Uuid = Uuid.random(),
    val name: String,
    val intakeTimeSeconds: Int,
)

