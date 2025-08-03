package com.galeria.medicationstracker.data.network

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.time.Instant


data class NetworkUser(
    @DocumentId val id: String = "",
    val name: String = "",
    val email: String = "",
    val weightKg: Float = 0f,
    val heightCm: Float = 0f,
    val dateOfBirth: Instant? = null,
    val schemaVersion: Int = 1
)

data class NetworkMedication(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val dosage: NetworkDosage = NetworkDosage(),
    val form: String = MedicationForm.UNKNOWN.name,
    val startDate: Instant? = null,
    val endDate: Instant? = null,
    val daysOfWeek: List<String> = emptyList(),
    val intakeTime: String = "",
    val schemaVersion: Int = 1
)

data class NetworkDosage(
    val value: Double = 0.0,
    val unit: String = "mg",
)

data class NetworkIntake(
    @DocumentId val id: String = "",
    val userId: String = "",
    val medicationId: String = "",
    val status: String = IntakeStatus.PENDING.name,
    val presetTime: String = "00:00:00",
    @ServerTimestamp var factTimestamp: Instant? = null,
    val schemaVersion: Int = 1
)

data class NetworkUserNote(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val tags: List<String> = emptyList(),
    @ServerTimestamp var timestamp: Instant? = null,
    val schemaVersion: Int = 1
)

data class NetworkUserMood(
    @DocumentId val id: String = "",
    val userId: String = "",
    val moodValue: Int = 3,
    val notes: String = "",
    @ServerTimestamp var timestamp: Instant? = null,
    val schemaVersion: Int = 1
)

enum class IntakeStatus {
    TAKEN,
    SKIPPED,
    PENDING,
}

enum class MedicationForm {
    TABLET,
    CAPSULE,
    LIQUID,
    INJECTION,
    POWDER,
    UNKNOWN,
}
