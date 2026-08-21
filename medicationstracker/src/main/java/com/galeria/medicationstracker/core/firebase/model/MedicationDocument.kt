package com.galeria.medicationstracker.core.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

// Firestore model.
data class MedicationDocument(
  @DocumentId
  val id: String = "",
  val name: String = "",
  val dosage: String = "",
  val form: String = "UNKNOWN",
  val startDate: Timestamp? = null,
  val endDate: Timestamp? = null,
  val daysOfWeek: List<String> = emptyList(),
  val intakeTimeInSeconds: Int = 0,
)
