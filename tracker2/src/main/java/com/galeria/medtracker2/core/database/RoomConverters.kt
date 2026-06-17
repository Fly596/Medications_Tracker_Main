@file:OptIn(ExperimentalUuidApi::class)

package com.galeria.medtracker2.core.database

import androidx.room.TypeConverter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RoomConverters {

    @TypeConverter
    fun fromStringToUuid(value: String?): Uuid? = value?.let { Uuid.parse(it) }

    @TypeConverter
    fun fromUuidToString(uuid: Uuid?): String? = uuid?.toString()
}
