package com.galeria.medicationstracker.data.source.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "note",
    // Указываем, что в этой таблице есть внешний ключ..
    foreignKeys =
        [
            ForeignKey(
                entity = User::class, // С какой таблицей связываем..
                parentColumns =
                    ["firestoreId"], // По какому полю в родительской таблице..
                childColumns = ["userId"], // По какому полю в текущей таблице..
                onDelete =
                    ForeignKey
                        .CASCADE, // Что делать, если родитель удален (удалить и
                // эту запись)..
            )],
)
data class UserNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Добавляем индекс, чтобы поиск по этому полю был быстрым.
    @ColumnInfo(index = true) val firestoreId: String? = null,
    @ColumnInfo(index = true) val userId: String? = null,
    val title: String,
    val content: String,
    val tags: List<String>,
    val timestamp: Long,
)

