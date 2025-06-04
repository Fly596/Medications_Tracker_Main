package com.galeria.medicationstracker.data.imp

import com.galeria.medicationstracker.data.IntakeStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import java.util.Date

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
    val timestamp: Date? = null,
)

data class NewUserNote(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val tags: List<String> = emptyList(),
    val timestamp: Date? = null,
)
data class NewUserMood(
    @DocumentId val id: String = "",
    val userId: String = "",
    val moodValue: Int? = null,
    val timestamp: Date? = null,
)