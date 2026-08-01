package com.galeria.medicationstracker.core.database

import androidx.room.TypeConverter
import com.galeria.medicationstracker.core.database.entity.DayOfWeek
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDate

class RoomConverters {

  @TypeConverter
  fun fromLocalDate(value: LocalDate?): Long? {
    return value?.toEpochDay()
  }

  @TypeConverter
  fun toLocalDate(value: Long?): LocalDate? {
    return value?.let { LocalDate.ofEpochDay(it) }
  }

  @TypeConverter
  fun fromInstant(value: Instant?): Long? {
    return value?.toEpochMilli()
  }

  @TypeConverter
  fun toInstant(value: Long?): Instant? {
    return value?.let { Instant.ofEpochMilli(it) }
  }

  @TypeConverter
  fun fromDayOfWeekList(value: List<DayOfWeek>): String {
    return Json.encodeToString(value)
    // Результат в БД будет выглядеть как TEXT: '["MONDAY","WEDNESDAY"]'
  }

  // 2. Из строки JSON обратно в объект List<DayOfWeek> при чтении из SQLite
  @TypeConverter
  fun toDayOfWeekList(value: String): List<DayOfWeek> {
    return Json.decodeFromString(value)
  }
}