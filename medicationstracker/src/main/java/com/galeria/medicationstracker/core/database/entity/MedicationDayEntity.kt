package com.galeria.medicationstracker.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
  tableName = "medication_days",
  foreignKeys = [
    ForeignKey(
      entity = MedicationEntity::class,
      parentColumns = ["id"],
      childColumns = ["medicationId"],
      onDelete = ForeignKey.CASCADE // Удаляем лекарство -> удаляются его дни!
    )
  ],
  indices = [
    Index(value = ["medicationId"]),
    Index(value = ["dayOfWeek"]) // ИНДЕКС для молниеносного поиска по дням!
  ]
)
data class MedicationDayEntity(
  @PrimaryKey()
  val id: UUID,
  val medicationId: String, // Внешний ключ
  val dayOfWeek: DayOfWeek // Простой Converter для Enum (из DayOfWeek в String)
)
