package com.galeria.medicationstracker.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun formatDateStringToTimestampMMMMddyyyy(
  dateText: String,
  locale: Locale = Locale.getDefault()
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
  val formatter = DateTimeFormatter.ofPattern(dateFormat, Locale.getDefault())
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
  zoneId: ZoneId = ZoneId.systemDefault()
): String {
  if (this == null || this < 0) {
    return "N/A"
  }
  val formatter = DateTimeFormatter.ofPattern(format, locale)
    .withZone(zoneId)
  
  return formatter.format(Instant.ofEpochMilli(this))
}

fun getTodaysDate(): LocalDate {
  // Gets the current date using the system's default time zone.
  return LocalDate.now(ZoneId.systemDefault())
}

fun getStringFormattedDate(inputDate: LocalDate): String {
  val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
  val formattedCurrentDate = inputDate.format(dateFormatter)
  
  return formattedCurrentDate.toString()
}

fun parseDateString(dateString: String): LocalDate {
  return try {
    val formatter = DateTimeFormatter.ofPattern("MMM dd")
    
    LocalDate.parse(dateString, formatter)
  } catch (e: Exception) {
    // Handle parsing errors, e.g., log the error or return null
    LocalDate.now()
  }
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
  val dayOfWeekFormatter =
    DateTimeFormatter.ofPattern("EEEE") // Add day of week formatter
  
  return timestamp.toLocalDateTime().format(dayOfWeekFormatter)
  
}

fun LocalDateTime.toTimestamp() =
  Timestamp(atZone(ZoneId.systemDefault()).toEpochSecond(), nano)

fun Timestamp.toLocalDateTime(zone: ZoneId = ZoneId.systemDefault()) =
  LocalDateTime.ofInstant(
    Instant.ofEpochMilli(seconds * 1000 + nanoseconds / 1000000),
    zone
  )

fun timeToFirestoreTimestamp(hour: Int, minute: Int): Timestamp {
  val now = LocalDate.now() // Get current date
  val localDateTime = LocalDateTime.of(now, LocalTime.of(hour, minute))
  return Timestamp(
    localDateTime.atZone(ZoneId.systemDefault())
      .toEpochSecond(), 0
  )
}