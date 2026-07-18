@file:OptIn(ExperimentalUuidApi::class)

package com.galeria.medtracker2.core.database

import androidx.room.TypeConverter
import java.time.Instant
import kotlin.uuid.ExperimentalUuidApi

class RoomConverters {

  @TypeConverter
  fun fromInstant(value: Instant?): Long? {
    return value?.toEpochMilli()
  }

  @TypeConverter
  fun toInstant(value: Long?): Instant? {
    return value?.let { Instant.ofEpochMilli(it) }
  }
}
