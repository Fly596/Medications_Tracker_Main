package com.galeria.medicationstracker.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class NewUser(
    @DocumentId val id: String = "",
    val name: String = "",
    val email: String = "",
    val weight: Float? = null,
    val height: Float? = null,
    val dateOfBirth: Timestamp? = null,
)

data class NewUserMedication(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val dosage: String = "",
    val form: String = MedicationForm.UNKNOWN.name,
    val startDate: Timestamp? = null,
    val endDate: Timestamp? = null,
    val daysOfWeek: List<String> = emptyList(),
    val intakeTime: String? = null,
)

data class NewUserIntake(
    @DocumentId val id: String = "",
    val userId: String = "",
    val medicationId: String = "",
    val status: String = IntakeStatus.PENDING.name,
    val timestamp: Timestamp? = Timestamp.now(),
)

data class NewUserNote(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val tags: List<String> = emptyList(),
    @ServerTimestamp val timestamp: Timestamp? = null,
)

data class NewUserMood(
    @DocumentId val id: String = "",
    val userId: String = "",
    val moodValue: Int? = null,
    val notes: String? = null,
    val timestamp: Timestamp? = null,
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
