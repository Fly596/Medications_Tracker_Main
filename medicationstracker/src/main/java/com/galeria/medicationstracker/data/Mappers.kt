package com.galeria.medicationstracker.data

import com.galeria.medicationstracker.data.local.Intake
import com.galeria.medicationstracker.data.local.Medication
import com.galeria.medicationstracker.data.network.NetworkIntake
import com.galeria.medicationstracker.data.network.NetworkMedication
import java.time.Instant
import java.time.ZoneId

// Extension functions to convert network models to local models.
fun NetworkMedication.toEntity(): Medication {
    return Medication(
        networkId = this.id,
        name = this.name,
        dosageValue = this.dosage.value,
        dosageUnit = this.dosage.unit,
        startDate = this.startDate?.seconds?.times(1000)
            ?.plus(startDate.nanoseconds / 1_000_000),
        endDate = this.endDate?.seconds?.times(1000)
            ?.plus(endDate.nanoseconds / 1_000_000),
        daysOfWeek = this.daysOfWeek,
        intakeTime = this.intakeTimeFromMidnight,
    )
}

// Extension functions to convert network models to local models.
fun NetworkIntake.toEntity(): Intake {
    return Intake(
        networkId = this.id,
        medicationId = this.medicationId,
        status = this.status,
        presetMinutesFromMidnight = this.presetTimeFromMidnight,
        factTimestamp = this.factTimestamp?.seconds?.times(1000)?.plus(
            factTimestamp!!.nanoseconds / 1_000_000
        )
    )
}

fun Medication.toDomain(): DomainMedication {
    val instantStart = Instant.ofEpochMilli(this.startDate ?: 0)
    val instantEnd = Instant.ofEpochMilli(this.endDate ?: 0)
    val localStartDate =
        instantStart.atZone(ZoneId.systemDefault()).toLocalDate()
    val localPublishDate =
        instantEnd.atZone(ZoneId.systemDefault()).toLocalDate()
    
    return DomainMedication(
        id = this.id,
        name = this.name,
        dosageValue = this.dosageValue,
        dosageUnit = this.dosageUnit,
        startDate = localStartDate,
        endDate = localPublishDate,
        daysOfWeek = this.daysOfWeek,
        intakeTime = this.intakeTime,
    )
}

// Мапперы для списка.
fun List<Medication>.toDomain(): List<DomainMedication> {
    return this.map { it.toDomain() }
}


fun List<NetworkMedication>.toEntity(): List<Medication> {
    return this.map { it.toEntity() }
}
