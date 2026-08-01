package com.galeria.medicationstracker.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class MedicationWithDays(
  @Embedded
  val medication: MedicationEntity,

  // Связанный список из второй таблицы
  @Relation(
    parentColumn = "id",            // Поле из MedicationEntity
    entityColumn = "medicationId"   // Поле из MedicationDayEntity
  )
  val days: List<MedicationDayEntity>
)
