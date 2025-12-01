package com.galeria.medicationstracker.feature_medications.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "medication")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)

@Entity(tableName = "medication_type")
data class MedicationTypeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)

@Entity(
    tableName = "medication_type_cross_ref",
    primaryKeys = ["medicationId", "medicationTypeId"],
    foreignKeys =
        [
            ForeignKey(
                entity = MedicationEntity::class,
                parentColumns = ["id"],
                childColumns = ["medicationId"],
                onDelete = ForeignKey.CASCADE,
            ),
            ForeignKey(
                entity = MedicationTypeEntity::class,
                parentColumns = ["id"],
                childColumns = ["medicationTypeId"],
                onDelete = ForeignKey.CASCADE,
            ),
        ],
)
data class MedicationTypeCrossRef(
    val medicationId: Long,
    val medicationTypeId: Long,
)

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "birth_date") val birthDate: Long,
    val email: String,
)

@Entity(
    tableName = "medication_intake",
    foreignKeys =
        [
            ForeignKey(
                entity = MedicationEntity::class,
                parentColumns = ["id"],
                childColumns = ["medicationId"],
                onDelete = ForeignKey.CASCADE,
            ),
            ForeignKey(
                entity = UserEntity::class,
                parentColumns = ["id"],
                childColumns = ["userId"],
                onDelete = ForeignKey.CASCADE,
            ),
        ],
)
data class MedicationIntakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medicationId: Long,
    val userId: Long,
    @ColumnInfo(name = "intake_time") val intakeTime: Long,
    val dosage: Double,
)

@Entity(
    tableName = "diary_record",
    foreignKeys =
        [
            ForeignKey(
                entity = UserEntity::class,
                parentColumns = ["id"],
                childColumns = ["userId"],
                onDelete = ForeignKey.CASCADE,
            )
        ],
)
data class DiaryRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val date: Long,
    val notes: String,
)

@Entity(tableName = "emotion")
data class EmotionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)

@Entity(
    tableName = "record_emotion_cross_ref",
    primaryKeys = ["recordId", "emotionId"],
    foreignKeys =
        [
            ForeignKey(
                entity = DiaryRecordEntity::class,
                parentColumns = ["id"],
                childColumns = ["recordId"],
                onDelete = ForeignKey.CASCADE,
            ),
            ForeignKey(
                entity = EmotionEntity::class,
                parentColumns = ["id"],
                childColumns = ["emotionId"],
                onDelete = ForeignKey.CASCADE,
            ),
        ],
)
data class RecordEmotionCrossRef(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recordId: Long,
    val emotionId: Long,
)

@Entity(tableName = "mood_reason")
data class MoodReasonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)

@Entity(
    tableName = "record_mood_reason_cross_ref",
    primaryKeys = ["recordId", "moodReasonId"],
    foreignKeys =
        [
            ForeignKey(
                entity = DiaryRecordEntity::class,
                parentColumns = ["id"],
                childColumns = ["recordId"],
                onDelete = ForeignKey.CASCADE,
            ),
            ForeignKey(
                entity = MoodReasonEntity::class,
                parentColumns = ["id"],
                childColumns = ["moodReasonId"],
                onDelete = ForeignKey.CASCADE,
            ),
        ],
)
data class RecordMoodReasonCrossRef(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recordId: Long,
    val moodReasonId: Long,
)
