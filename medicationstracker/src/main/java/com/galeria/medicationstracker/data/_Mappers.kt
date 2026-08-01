package com.galeria.medicationstracker.data

import com.galeria.medicationstracker.core.database.entity.DayOfWeek
import com.galeria.medicationstracker.core.database.entity.MedicationDayEntity
import com.galeria.medicationstracker.core.database.entity.MedicationEntity
import com.galeria.medicationstracker.core.database.entity.MedicationWithDays
import com.galeria.medicationstracker.core.database.entity.UserEntity
import com.galeria.medicationstracker.core.domain.model.Medication
import com.galeria.medicationstracker.core.domain.model.User
import com.galeria.medicationstracker.core.firebase.model.MedicationDocument
import com.galeria.medicationstracker.core.firebase.model.UserDocument
import com.galeria.medicationstracker.utils.DateTimeUtils
import java.util.Locale.getDefault
import java.util.UUID

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

fun MedicationDocument.toRoomEntities(): Pair<MedicationEntity, List<MedicationDayEntity>> {
  val medicationEntity = MedicationEntity(
    id = id,
    userId = userId,
    name = name,
    dosage = dosage,
    form = form,
    startDate = DateTimeUtils.timestampToInstant(startDate),
    endDate = DateTimeUtils.timestampToInstant(endDate),
    intakeTime = intakeTime
  )

  val dayEntities = daysOfWeek.map { day ->
    MedicationDayEntity(
      id = UUID.randomUUID(),
      medicationId = id,
      dayOfWeek = DayOfWeek.valueOf(day.uppercase(getDefault()))
    )
  }

  return Pair(medicationEntity, dayEntities)
}

fun Medication.toDocument(): MedicationDocument = MedicationDocument(
  id = id,
  userId = userId,
  dosage = dosage,
  name = name,
  form = form,
  startDate = DateTimeUtils.instantToTimestamp(startDate),
  endDate = DateTimeUtils.instantToTimestamp(endDate),
  daysOfWeek = daysOfWeek.map { it.name },
  intakeTime = intakeTime
)

fun MedicationWithDays.toDomain(): Medication = Medication(
  id = medication.id,
  userId = medication.userId,
  dosage = medication.dosage,
  name = medication.name,
  form = medication.form,
  startDate = medication.startDate,
  endDate = medication.endDate,
  daysOfWeek = days.map { it.dayOfWeek },
  intakeTime = medication.intakeTime
)

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
