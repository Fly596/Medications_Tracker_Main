package com.galeria.medtracker2.core.common

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// Утилиты для работы с датой вынесены в object для удобства доступа
object DateTimeUtils {

    private val zoneId: ZoneId = ZoneId.of("UTC")
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.US)

    fun fromTimestampToLocalDateTime(value: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), zoneId)
    }

    fun fromTimestampToLocalDate(value: Long): LocalDate {
        return LocalDate.ofInstant(Instant.ofEpochMilli(value), zoneId)
    }

    fun fromDateTimeValues(date: LocalDate, hour: Int, minute: Int): Instant {
        return date.atTime(hour, minute)
            .atZone(ZoneId.systemDefault()) // Или ZoneOffset.UTC
            .toInstant()
    }
}
