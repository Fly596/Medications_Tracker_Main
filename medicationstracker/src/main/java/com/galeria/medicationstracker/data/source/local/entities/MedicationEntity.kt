package com.galeria.medicationstracker.data.source.local.entities
/*
data class Dosage(val value: Double = 0.0, val unit: String = "mg")

@Entity(tableName = "medication", indices = [Index(value = ["firestoreId"], unique = true)])
@TypeConverters(Converters::class)
data class Medication(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Добавляем индекс, чтобы поиск по этому полю был быстрым.
    val firestoreId: String,
    val name: String,
    @Embedded val dosage: Dosage,
    val startDate: Instant?,
    val endDate: Instant?,
    val daysOfWeek: List<String>,
    val intakeTime: Int,
)
*/
