package com.galeria.medicationstracker.domain.medications.model

import com.galeria.medicationstracker.data.MedicationForm
import com.galeria.medicationstracker.data.NewUserMedication
import com.galeria.medicationstracker.utils.toLocalDate
import com.galeria.medicationstracker.utils.toTimestamp
import java.time.LocalDate
import java.time.LocalTime

data class Medication(
    val id: String,
    val name: String,
    val dosage: String,
    val form: MedicationForm,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val daysOfWeek: List<String>,
    val intakeTime: LocalTime
)

fun Medication.toNewUserMedication(): NewUserMedication = NewUserMedication(
    id = id,
    name = name,
    dosage = dosage,
    form = form.name,
    startDate = startDate?.toTimestamp(),
    endDate = endDate?.toTimestamp(),
    daysOfWeek = daysOfWeek,
    intakeTime = intakeTime.toString()
)

fun NewUserMedication.toMedication(): Medication = Medication(
    id = id,
    name = name,
    dosage = dosage,
    form = MedicationForm.valueOf(form),
    startDate = startDate?.toLocalDate(),
    endDate = endDate?.toLocalDate(),
    daysOfWeek = daysOfWeek,
    intakeTime = LocalTime.parse(
        intakeTime
    )
)