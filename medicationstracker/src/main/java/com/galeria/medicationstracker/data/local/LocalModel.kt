package com.galeria.medicationstracker.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.galeria.medicationstracker.data.network.IntakeStatus

data class Dosage(
    val value: Double = 0.0,
    val unit: String = "mg",
)

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(index = true)
    val firestoreId: String? = null,
    val name: String,
    val email: String,
    val weightKg: Double,
    val heightCm: Double,
    val dateOfBirth: Long,
)

@Entity(
    tableName = "medication",
)
@TypeConverters(Converters::class)
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Добавляем индекс, чтобы поиск по этому полю был быстрым.
    @ColumnInfo(index = true)
    val firestoreId: String? = null,
    val name: String,
    @Embedded val dosage: Dosage,
    val startDate: Long?,
    val endDate: Long?,
    val daysOfWeek: List<String>,
    val intakeTime: Int,
)

@Entity(
    tableName = "intake",
    // Указываем, что в этой таблице есть внешний ключ..
    foreignKeys =
        [
            ForeignKey(
                entity = Medication::class, // С какой таблицей связываем..
                parentColumns = ["firestoreId"], // По какому полю в родительской таблице..
                childColumns = ["medicationId"], // По какому полю в текущей таблице..
                onDelete =
                    ForeignKey.CASCADE, // Что делать, если родитель удален (удалить и эту запись)..
            ),
        ],
)
data class Intake(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Добавляем индекс, чтобы поиск по этому полю был быстрым.
    @ColumnInfo(index = true)
    val firestoreId: String? = null,
    @ColumnInfo(index = true)
    val medicationId: String,
    val status: String = IntakeStatus.PENDING.name,
    val presetMinutesFromMidnight: Int,
    val factTimestamp: Long?,
)

@Entity(
    tableName = "note",
    // Указываем, что в этой таблице есть внешний ключ..
    foreignKeys =
        [
            ForeignKey(
                entity = User::class, // С какой таблицей связываем..
                parentColumns = ["firestoreId"], // По какому полю в родительской таблице..
                childColumns = ["userId"], // По какому полю в текущей таблице..
                onDelete =
                    ForeignKey.CASCADE, // Что делать, если родитель удален (удалить и эту запись)..
            )
        ],
)
data class UserNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Добавляем индекс, чтобы поиск по этому полю был быстрым.
    @ColumnInfo(index = true) val noteFirestoreId: String? = null,
    @ColumnInfo(index = true) val userId: String? = null,
    val title: String,
    val content: String,
    val tags: List<String>,
    val timestamp: Long,
)

@Entity(
    tableName = "mood",
    // Указываем, что в этой таблице есть внешний ключ..
    foreignKeys =
        [
            ForeignKey(
                entity = User::class, // С какой таблицей связываем..
                parentColumns = ["firestoreId"], // По какому полю в родительской таблице..
                childColumns = ["userId"], // По какому полю в текущей таблице..
                onDelete =
                    ForeignKey.CASCADE, // Что делать, если родитель удален (удалить и эту запись)..
            )
        ],
)
data class Mood(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Добавляем индекс, чтобы поиск по этому полю был быстрым.
    @ColumnInfo(index = true) val moodFirestoreId: String? = null,
    @ColumnInfo(index = true) val userId: String? = null,
    val moodValue: Int,
    val note: String,
    val timestamp: Long,
)

class Converters {
    
    @TypeConverter
    fun stringToList(value: String): List<String> = value.split(',')
    
    @TypeConverter
    fun listToString(list: List<String>): String = list.joinToString(",")
}
