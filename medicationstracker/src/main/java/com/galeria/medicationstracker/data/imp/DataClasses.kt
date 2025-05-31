package com.galeria.medicationstracker.data.imp

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

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
