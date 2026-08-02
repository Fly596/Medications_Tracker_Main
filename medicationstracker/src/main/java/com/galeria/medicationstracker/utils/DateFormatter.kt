package com.galeria.medicationstracker.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateTimeUtils {

  private val zoneId: ZoneId = ZoneId.of("UTC")
  val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("mm-dd-yyyy")
  val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

  fun fromDateToTimestamp(date: LocalDate?): Timestamp {
    return date?.let {
      Timestamp(
        date.toEpochDay(),
        0
      )
    } ?: Timestamp.now()
  }

  fun timestampToLocalDate(ts: Timestamp?): LocalDate {
    return ts?.let {
      ts.toInstant().atZone(zoneId).toLocalDate()
    } ?: LocalDateTime.now().toLocalDate()
  }

  fun timestampToInstant(ts: Timestamp?): Instant {
    return ts?.let {
      ts.toInstant()
    } ?: LocalDateTime.now().toInstant(ZoneOffset.UTC)
  }

  fun instantToTimestamp(instant: Instant?): Timestamp {
    return instant?.let {
      Timestamp(it.epochSecond, 0)
    } ?: Timestamp.now()
  }
}

// LocalDate -> timestamp.
fun LocalDate.toTimestamp(): Timestamp {
  // тк LocalDate не имеет времени.
  val localDateTime = this.atStartOfDay()
  // часовой пояс системы, чтоб получить zoneddatetime.
  val zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"))
  // конвертим в date, затем в timestamp.
  return Timestamp(Date.from(zonedDateTime.toInstant()))
}

// LocalDateTime -> timestamp.
fun LocalDateTime.toTimestamp(): Timestamp {
  // часовой пояс системы, чтоб получить zoneddatetime.
  val zonedDateTime = this.atZone(ZoneId.systemDefault())
  // конвертим в date, затем в timestamp.
  return Timestamp(Date.from(zonedDateTime.toInstant()))
}

// Timestamp -> LocalDate.
fun Timestamp.toLocalDate(): LocalDate {
  return this.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

// Timestamp -> LocalDateTime.
fun Timestamp.toLocalDateTime(): LocalDateTime {
  return this.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}

fun Long.toLocalDate(): LocalDate {
  return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault())
    .toLocalDate()
}

fun Instant.toTimestamp(): Timestamp {
  return Timestamp(atZone(ZoneId.systemDefault()).toEpochSecond(), 0)
}
// region bad
fun formatDateStringToTimestampMMMMddyyyy(
  dateText: String,
  locale: Locale = Locale.getDefault(),
): Timestamp? {
  if (dateText.isBlank()) {
    println("Error: Input date string is empty.")
    return null
  }
  return try {
    val dateFormatter = SimpleDateFormat("MMMM dd yyyy", locale)
    val parsedDate: Date = dateFormatter.parse(dateText) ?: return null

    Timestamp(parsedDate)
  } catch (e: Exception) {
    // 6. More specific exception handling
    println("Error parsing date: '$dateText'. Exception: ${e.message}")
    null
  }
}

fun convertMillisToDate(timeInMillis: Long?): String {
  // 1. Handle null input more explicitly and consistently
  if (timeInMillis == null || timeInMillis < 0) {
    return "N/A" // Or throw an exception, or return null, depending on the desired behavior
  }
  // 2. Use a constant for the date format string
  val dateFormat = "MMMM dd yyyy"
  // 3. Create the DateTimeFormatter once and reuse it
  val formatter =
      DateTimeFormatter.ofPattern(dateFormat, Locale.getDefault())
        .withZone(ZoneId.systemDefault())
  // 4. Use 'run' to make the code more readable and avoid repeating formatter.
  return run {
    val instant = Instant.ofEpochMilli(timeInMillis)
    formatter.format(instant)
  }
}

fun Long?.toDateString(
  format: String = "MMMM dd yyyy",
  locale: Locale = Locale.getDefault(),
  zoneId: ZoneId = ZoneId.systemDefault(),
): String {
  if (this == null || this < 0) {
    return "N/A"
  }
  val formatter = DateTimeFormatter.ofPattern(format, locale).withZone(zoneId)

  return formatter.format(Instant.ofEpochMilli(this))
}

fun getTodaysDate(): LocalDate {
  // Gets the current date using the system's default time zone.
  return LocalDate.now(ZoneId.systemDefault())
}

fun formatTimestampTillTheDayMMMMddyyyy(timestamp: Timestamp): String {
  val formatter = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault())
  return formatter.format(timestamp.toDate())
}

fun formatTimestampTillTheHour(timestamp: Timestamp): String {
  val formatter = SimpleDateFormat("K:mm a", Locale.getDefault())
  return formatter.format(timestamp.toDate())
}

fun formatTimestampToMinutemmmmddyyyyhm(timestamp: Timestamp): String {
  val formatter = SimpleDateFormat("MMMM dd yyyy, K:mm a", Locale.getDefault())
  return formatter.format(timestamp.toDate())
}

fun formatTimestampToWeekday(timestamp: Timestamp): String {
  val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE") // Add day of week formatter

  return timestamp.toLocalDateTime().format(dayOfWeekFormatter)
}

// fun LocalDateTime.toTimestamp() = Timestamp(atZone(ZoneId.systemDefault()).toEpochSecond(), nano)

fun Timestamp.toLocalDateTime(zone: ZoneId = ZoneId.systemDefault()) =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(seconds * 1000 + nanoseconds / 1000000), zone)

fun timeToFirestoreTimestamp(hour: Int, minute: Int): Timestamp {
  val now = LocalDate.now() // Get current date
  val localDateTime = LocalDateTime.of(now, LocalTime.of(hour, minute))
  return Timestamp(localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond(), 0)
}
// endregion
