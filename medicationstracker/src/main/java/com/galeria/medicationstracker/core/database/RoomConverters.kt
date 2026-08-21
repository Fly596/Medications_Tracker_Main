package com.galeria.medicationstracker.core.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class RoomConverters {

  @TypeConverter
  fun localDateToString(date: LocalDate?): String? =
      date?.format(DateTimeFormatter.ofPattern("mm-dd-yyyy"))

  @TypeConverter
  fun stringToLocalDate(dateString: String?): LocalDate? = dateString?.let {
    LocalDate.parse(it, DateTimeFormatter.ofPattern("mm-dd-yyyy"))
  }

  @TypeConverter
  fun fromLocalTime(time: LocalTime?): Int? = time?.toSecondOfDay()

  @TypeConverter
  fun toLocalTime(seconds: Int?): LocalTime? = seconds?.let { LocalTime.ofSecondOfDay(it.toLong()) }

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