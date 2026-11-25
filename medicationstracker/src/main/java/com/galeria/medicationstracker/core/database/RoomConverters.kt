package com.galeria.medicationstracker.core.database

import androidx.room.TypeConverter
import com.galeria.medicationstracker.data.MedicationForm
import com.galeria.medicationstracker.domain.medications.model.IntakeStatus
import kotlinx.serialization.json.Json
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class RoomConverters {

    @TypeConverter
    fun localDateToString(date: LocalDate?): String? =
        date?.toString()

    @TypeConverter
    fun stringToLocalDate(dateString: String?): LocalDate? =
        dateString?.let { runCatching { LocalDate.parse(it) }.getOrNull() }

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): Int? =
        time?.toSecondOfDay()

    @TypeConverter
    fun toLocalTime(seconds: Int?): LocalTime? =
        seconds?.let { runCatching { LocalTime.ofSecondOfDay(it.toLong()) }.getOrNull() }

    @TypeConverter
    fun fromDayOfWeekList(value: List<DayOfWeek>?): String? =
        value?.let { Json.encodeToString(it) }

    @TypeConverter
    fun toDayOfWeekList(value: String?): List<DayOfWeek>? =
        value?.let {
            runCatching { Json.decodeFromString<List<DayOfWeek>>(it) }.getOrDefault(
                emptyList(),
            )
        }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? =
        value?.let { Json.encodeToString(it) }

    @TypeConverter
    fun toStringList(value: String?): List<String>? =
        value?.let {
            runCatching { Json.decodeFromString<List<String>>(it) }.getOrDefault(
                emptyList(),
            )
        }

    @TypeConverter
    fun instantToLong(value: Instant?): Long? =
        value?.toEpochMilli()

    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    fun medicationFormToString(form: MedicationForm?): String? =
        form?.name

    @TypeConverter
    fun stringToMedicationForm(value: String?): MedicationForm? =
        value?.let { runCatching { MedicationForm.valueOf(it) }.getOrDefault(MedicationForm.UNKNOWN) }

    @TypeConverter
    fun intakeStatusToString(status: IntakeStatus?): String? =
        status?.name

    @TypeConverter
    fun stringToIntakeStatus(value: String?): IntakeStatus? =
        value?.let { runCatching { IntakeStatus.valueOf(it) }.getOrDefault(IntakeStatus.PENDING) }
}