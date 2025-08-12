package com.galeria.medicationstracker.data

import com.galeria.medicationstracker.data.local.Intake
import com.galeria.medicationstracker.data.local.Medication
import com.galeria.medicationstracker.data.network.NetworkIntake
import com.galeria.medicationstracker.data.network.NetworkMedication

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

