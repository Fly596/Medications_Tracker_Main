package com.galeria.medicationstracker.data.source.local

import androidx.room.TypeConverter
import java.time.Instant


class Converters {
    
    @TypeConverter
    fun stringToList(value: String): List<String> = value.split(',')
    
    @TypeConverter
    fun listToString(list: List<String>): String = list.joinToString(",")
}

class DateConverters {
    
    @TypeConverter
    fun fromInstant(value: Instant?): Long? {
        return value?.toEpochMilli()
    }
    
    @TypeConverter
    fun toInstant(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }
    
    
}