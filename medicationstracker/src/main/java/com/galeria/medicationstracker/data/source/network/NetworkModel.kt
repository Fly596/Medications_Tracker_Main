package com.galeria.medicationstracker.data.source.network

import com.galeria.medicationstracker.data.source.local.entities.Dosage
import com.galeria.medicationstracker.feature_medications.domain.model.MedicationForm
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp


data class NetworkUser(
    @DocumentId val id: String = "",
    val name: String = "",
    val email: String = "",
    val weightKg: Float = 0f,
    val heightCm: Float = 0f,
    val dateOfBirth: Timestamp? = null,
    val schemaVersion: Int = 1
)

data class NetworkMedication(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val dosage: Dosage = Dosage(),
    val form: String = MedicationForm.UNKNOWN.name,
    val startDate: Timestamp? = null,
    val endDate: Timestamp? = null,
    val daysOfWeek: List<String> = emptyList(),
    val intakeTimeFromMidnight: Int = 0,
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
    val presetTimeFromMidnight: Int = 0,
    @ServerTimestamp var factTimestamp: Timestamp? = null,
    val schemaVersion: Int = 1
)

data class NetworkUserNote(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val tags: List<String> = emptyList(),
    @ServerTimestamp var timestamp: Timestamp? = null,
    val schemaVersion: Int = 1
)

data class NetworkUserMood(
    @DocumentId val id: String = "",
    val userId: String = "",
    val moodValue: Int = 3,
    val notes: String = "",
    @ServerTimestamp var timestamp: Timestamp? = null,
    val schemaVersion: Int = 1
)

enum class IntakeStatus {
    TAKEN, // green color.
    SKIPPED, // yellow color.
    PENDING, // default color.
}

