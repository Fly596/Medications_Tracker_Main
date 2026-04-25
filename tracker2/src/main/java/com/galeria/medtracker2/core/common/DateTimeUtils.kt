package com.galeria.medtracker2.core.common

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// Утилиты для работы с датой вынесены в object для удобства доступа
object DateTimeUtils {

    private val zoneId: ZoneId = ZoneId.of("UTC")
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.US)

    fun fromTimestamp(value: Long?): LocalDateTime {
        return value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), zoneId)
        } ?: LocalDateTime.now()
    }
}