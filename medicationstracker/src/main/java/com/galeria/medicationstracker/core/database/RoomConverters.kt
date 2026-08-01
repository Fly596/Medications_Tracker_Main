package com.galeria.medicationstracker.core.database

import androidx.room.TypeConverter
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
}