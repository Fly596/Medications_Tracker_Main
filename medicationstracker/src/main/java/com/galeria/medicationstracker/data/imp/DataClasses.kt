package com.galeria.medicationstracker.data.imp

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class NewUser(
    @DocumentId val id: String = "",
    val name: String = "",
    val email: String = "",
    val weight: Float? = null,
    val height: Float? = null,
    val dateOfBirth: Date? = null,
)

data class NewUserMedication(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val dosage: String = "",
    val form: String = MedicationForm.UNKNOWN.name,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val daysOfWeek: List<String> = emptyList(),
    val intakeTime: String? = null,
)

data class NewUserIntake(
    @DocumentId val id: String = "",
    val userId: String = "",
    val medicationId: String = "",
    val status: String = IntakeStatus.PENDING.name,
    val timestamp: Date? = null,
)

data class NewUserNote(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val tags: List<String> = emptyList(),
    @ServerTimestamp val timestamp: Date? = null,
)

data class NewUserMood(
    @DocumentId val id: String = "",
    val userId: String = "",
    val moodValue: Int? = null,
    val timestamp: Date? = null,
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
