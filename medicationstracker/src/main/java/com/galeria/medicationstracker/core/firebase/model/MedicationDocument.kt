package com.galeria.medicationstracker.core.firebase.model

import com.galeria.medicationstracker.data.MedicationForm
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class MedicationDocument(
  @DocumentId
  val id: String,
  val userId: String,
  val name: String = "",
  val dosage: String = "",
  val form: String = MedicationForm.UNKNOWN.name,
  val startDate: Timestamp? = null,
  val endDate: Timestamp? = null,
  val daysOfWeek: List<String> = emptyList(),
  val intakeTime: String,
)
