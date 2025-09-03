package com.galeria.medicationstracker.data

import com.galeria.medicationstracker.data.local.Dosage
import com.galeria.medicationstracker.data.local.Medication
import com.galeria.medicationstracker.data.network.NetworkMedication
import java.time.Instant
import java.time.ZoneId

// Extension functions to convert network models to local models.
fun NetworkMedication.toEntity(firestoreId: String): Medication {
    return Medication(
        firestoreId = firestoreId,
        name = this.name,
        dosage = Dosage(value = this.dosage.value, unit = this.dosage.unit),
        startDate = this.startDate?.toDate()?.time,
        endDate = this.endDate?.toDate()?.time,
        daysOfWeek = this.daysOfWeek,
        intakeTime = this.intakeTimeFromMidnight,
    )
}

// Extension functions to convert network models to local models.
/* fun NetworkIntake.toEntity(firestoreId: String): Intake {
    return Intake(
        firestoreId = firestoreId,
        medicationId = this.medicationId,
        status = this.status,
        presetMinutesFromMidnight = this.presetTimeFromMidnight,
        factTimestamp = this.factTimestamp?.toDate()?.time,
    )
} */

fun Medication.toDomain(): DomainMedication {
    val localStartDate =
        this.startDate?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
    val localEndDate =
        this.endDate?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
    return DomainMedication(
        id = this.id,
        networkId = this.firestoreId,
        name = this.name,
        dosage = this.dosage, // Просто передаем объект дальше..
        startDate = localStartDate,
        endDate = localEndDate,
        daysOfWeek = this.daysOfWeek,
        intakeTime = this.intakeTime,
    )
}

// Мапперы для списка.
fun List<Medication>.toDomain(): List<DomainMedication> {
    return this.map { it.toDomain() }
}

fun List<NetworkMedication>.toEntity(): List<Medication> {
    return this.map { it.toEntity(firestoreId = it.id) }
}
