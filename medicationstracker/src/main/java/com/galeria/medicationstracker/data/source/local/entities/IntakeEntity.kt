package com.galeria.medicationstracker.data.source.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.galeria.medicationstracker.data.source.network.IntakeStatus
import com.galeria.medicationstracker.feature_medications.data.source.local.MedicationEntity

@Entity(
    tableName = "intake",
    // Указываем, что в этой таблице есть внешний ключ..
    foreignKeys =
        [
            ForeignKey(
                entity = MedicationEntity::class, // С какой таблицей связываем..
                parentColumns = ["id"], // По какому полю в родительской таблице..
                childColumns = ["medicationId"], // По какому полю в текущей таблице..
                onDelete = ForeignKey.CASCADE, // Что делать, если родитель удален (удалить и
                // эту запись)..
            )],
)
data class Intake(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Добавляем индекс, чтобы поиск по этому полю был быстрым.
    @ColumnInfo(index = true) val firestoreId: String? = null,
    @ColumnInfo(index = true) val medicationId: String,
    val status: String = IntakeStatus.PENDING.name,
    val presetMinutesFromMidnight: Int,
    val factTimestamp: Long?,
)
