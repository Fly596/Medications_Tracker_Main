package com.galeria.medtracker2.core.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

// Утилиты для работы с датой вынесены в object для удобства доступа
object DateTimeUtils {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy, hh:mm a", Locale.US)
    private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.US)
    private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US)

    fun formatLocalDateTime(dateTime: LocalDateTime): String {
        return dateTime.format(dateTimeFormatter)
    }

    fun formatLocalTime(time: LocalTime): String {
        return time.format(timeFormatter)
    }

    fun formatLongToLocalDateString(millis: Long?): String {
        if (millis == null) return "Choose date"
        return fromLongToLocalDate(millis).format(dateFormatter)
    }

    fun formatLongToLocalDateTimeString(millis: Long?): String {
        if (millis == null) return "Choose date"
        return fromLongToLocalDateTime(millis).format(dateTimeFormatter)
    }

    // БЕЗОПАСНАЯ конвертация миллисекунд DatePicker'а (UTC) в LocalDate
    fun fromLongToLocalDate(millis: Long): LocalDate {
        return Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
    }

    fun fromLongToLocalDateTime(value: Long): LocalDateTime {
        return Instant.ofEpochMilli(value).atZone(ZoneOffset.UTC).toLocalDateTime()
        // return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
    }

    fun fromLocalDateToLong(date: LocalDate): Long {
        return date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

    // Соединяет LocalDate и LocalTime в Instant текущей временной зоны
    fun combineDateAndTime(date: LocalDate, time: LocalTime): Instant {
        return date.atTime(time).atZone(ZoneOffset.UTC).toInstant()
    }
}
