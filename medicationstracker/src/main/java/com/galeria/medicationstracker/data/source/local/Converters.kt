package com.galeria.medicationstracker.data.source.local

import androidx.room.TypeConverter


class Converters {

  @TypeConverter
  fun stringToList(value: String): List<String> = value.split(',')

  @TypeConverter
  fun listToString(list: List<String>): String = list.joinToString(",")
}
