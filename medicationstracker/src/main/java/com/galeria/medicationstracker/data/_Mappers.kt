package com.galeria.medicationstracker.data

import com.galeria.medicationstracker.core.database.entity.MedicationEntity
import com.galeria.medicationstracker.core.database.entity.UserEntity
import com.galeria.medicationstracker.core.domain.model.Medication
import com.galeria.medicationstracker.core.domain.model.User
import com.galeria.medicationstracker.core.firebase.model.MedicationDocument
import com.galeria.medicationstracker.core.firebase.model.UserDocument
import com.galeria.medicationstracker.utils.DateTimeUtils
import com.google.firebase.Timestamp
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

// DTO -> Entity
fun UserDocument.toEntity(): UserEntity = UserEntity(
  id = id,
  email = email,
  name = name,
  weight = weight,
  height = height,
  dateOfBirth = DateTimeUtils.timestampToLocalDate(dateOfBirth)
)

// Domain -> DTO
fun User.toDocument(): UserDocument = UserDocument(
  id = id,
  email = email,
  name = name,
  weight = weight,
  height = height,
  dateOfBirth = DateTimeUtils.fromDateToTimestamp(dateOfBirth)
)

// Entity -> Domain
fun UserEntity.toDomain(): User = User(
  id = id,
  email = email,
  name = name,
  weight = weight,
  height = height,
  dateOfBirth = dateOfBirth
)

// --- Domain -> Room Entity ---
fun Medication.toEntity() = MedicationEntity(
  id = id,
  userId = userId,
  name = name,
  dosage = dosage,
  form = form.name,
  startDate = startDate,
  endDate = endDate,
  daysOfWeek = daysOfWeek,
  intakeTime = intakeTime
)

// --- Room Entity -> Domain ---
fun MedicationEntity.toDomain() = Medication(
  id = id,
  userId = userId,
  name = name,
  dosage = dosage,
  form = runCatching { MedicationForm.valueOf(form) }.getOrDefault(MedicationForm.UNKNOWN),
  startDate = startDate,
  endDate = endDate,
  daysOfWeek = daysOfWeek,
  intakeTime = intakeTime
)

// --- Firebase Document -> Domain ---
fun MedicationDocument.toDomain(): Medication {
  val zone = ZoneId.systemDefault()
  val startLocalDate = startDate?.let {
    Instant.ofEpochMilli(it.seconds * 1000).atZone(zone).toLocalDate()
  } ?: LocalDate.now()

  val endLocalDate = endDate?.let {
    Instant.ofEpochMilli(it.seconds * 1000).atZone(zone).toLocalDate()
  } ?: LocalDate.now()

  return Medication(
    id = id,
    userId = userId,
    name = name,
    dosage = dosage,
    form = runCatching { MedicationForm.valueOf(form) }.getOrDefault(MedicationForm.UNKNOWN),
    startDate = startLocalDate,
    endDate = endLocalDate,
    daysOfWeek = daysOfWeek.mapNotNull { runCatching { DayOfWeek.valueOf(it) }.getOrNull() },
    intakeTime = LocalTime.ofSecondOfDay(intakeTimeInSeconds.toLong())
  )
}

// --- Domain -> Firebase Document ---
fun Medication.toDocument(): MedicationDocument {
  val zone = ZoneId.systemDefault()
  val startTimestamp = Timestamp(Date.from(startDate.atStartOfDay(zone).toInstant()))
  val endTimestamp = Timestamp(java.util.Date.from(endDate.atStartOfDay(zone).toInstant()))

  return MedicationDocument(
    id = id,
    userId = userId,
    name = name,
    dosage = dosage,
    form = form.name,
    startDate = startTimestamp,
    endDate = endTimestamp,
    daysOfWeek = daysOfWeek.map { it.name },
    intakeTimeInSeconds = intakeTime.toSecondOfDay()
  )
}


/*
fun MedicationEntity.toDomain(): Medication = Medication(
  id = id,
  userId = userId,
  dosage = dosage,
  name = name,
  form = form,
  startDate = startDate,
  endDate = endDate,
  daysOfWeek = daysOfWeek,
  intakeTime = intakeTime
)*/
