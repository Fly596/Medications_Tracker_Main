package com.galeria.medtracker2.core.common

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

// Утилиты для работы с датой вынесены в object для удобства доступа
object DateTimeUtils {


    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US)

    fun formatDatePickerMillis(millis: Long?): String {
        if (millis == null) return "Choose date"
        return fromDatePickerMillisToLocalDate(millis).format(dateFormatter)
    }

    fun formatLocalTime(time: LocalTime): String {
        return time.format(timeFormatter)
    }

    // БЕЗОПАСНАЯ конвертация миллисекунд DatePicker'а (UTC) в LocalDate
    fun fromDatePickerMillisToLocalDate(millis: Long): LocalDate {
        return Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
    }

    // Соединяет LocalDate и LocalTime в Instant текущей временной зоны
    fun combineDateAndTime(date: LocalDate, time: LocalTime): Instant {
        return date.atTime(time).atZone(ZoneId.systemDefault()).toInstant()
    }

    // region old
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.US)
    val dateTimeFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("MMM dd, yyyy, hh:mm a", Locale.US)

    fun fromTimestampToLocalDateTime(value: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
    }

    fun fromTimestampToDate(value: Long): LocalDate {
        return LocalDate.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
    }

    fun fromDateTimeValues(date: LocalDate, hour: Int, minute: Int): Instant {
        return date.atTime(hour, minute)
            .atZone(ZoneId.systemDefault()) // Или ZoneOffset.UTC
            .toInstant()
    }
    // endregion

}
