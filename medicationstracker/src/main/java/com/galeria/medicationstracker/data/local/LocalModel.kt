package com.galeria.medicationstracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.galeria.medicationstracker.data.network.IntakeStatus

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val networkId: String,
    val name: String,
    val email: String,
    val weightKg: Double,
    val heightCm: Double,
    val dateOfBirth: String,
)

@Entity(tableName = "medication")
@TypeConverters(Converters::class)
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val networkId: String,
    val userId: Int,
    val name: String,
    val dosageValue: Double,
    val dosageUnit: String,
    val startDate: Long,
    val endDate: Long,
    val daysOfWeek: List<String>,
    val intakeTime: List<String>,
)

@Entity(tableName = "intake")
data class Intake(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firestoreId: String,
    val networkId: String,
    val userId: String,
    val medicationId: String,
    val status: String = IntakeStatus.PENDING.name,
    val presetTime: String,
    val factTimestamp: Long,
)

@Entity(tableName = "note")
data class UserNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val networkId: String,
    val userId: String,
    val title: String,
    val content: String,
    val tags: List<String>,
    val timestamp: Long,
)

@Entity(tableName = "mood")
data class Mood(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val networkId: String,
    val userId: String,
    val moodValue: Int,
    val note: String,
    val timestamp: Long,
)


class Converters {
    
    @TypeConverter
    fun fromString(value: String): List<String> = value.split(',')
    
    @TypeConverter
    fun fromList(list: List<String>): String = list.joinToString(",")
}