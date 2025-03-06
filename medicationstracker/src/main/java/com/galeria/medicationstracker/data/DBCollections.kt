package com.galeria.medicationstracker.data

import com.google.firebase.Timestamp

data class UserMedication(
  val uid: String,
  val name: String,
  val form: MedicationForm?,
  val strength: Float?,
  val unit: MedicationUnit?,
  val startDate: Timestamp?,
  val endDate: Timestamp?,
  val daysOfWeek: List<String> = emptyList(),
  val intakeTime: String?,
  val notes: String? = null,
)

data class User(
  val uid: String,
  val email: String, // Логин = email
  val type: UserType,
  val firstName: String? = null,
  val lastName: String? = null,
  val age: Int? = null,
  val weight: Float? = null,
  val height: Float? = null,
  val dateOfBirth: Timestamp? = null,
  val bloodType: BloodType? = null,
  val sex: String? = null,
)

data class Note(
  val title: String,
  val content: String,
  val date: Timestamp = Timestamp.now(),
  val tags: List<String> = emptyList(),
  val medicationIds: List<String> = emptyList(),
)
/* data class UserProfile(
  val uid: String = "",
  val firstName: String,
  val lastName: String,
  val weight: Float,
  val height: Float,
  val email: String,
  val dateOfBirth: Timestamp,
  val bloodType: BloodType,
  val sex: String,
) */

enum class IntakeStatus {
  TAKEN,
  MISSED,
  PENDING,
}

data class UserIntake(
  val uid: String,
  val medicationId: String,
  val dose: String?,
  val status: IntakeStatus = IntakeStatus.PENDING,
  val dateTime: Timestamp,
)

enum class UserType {
  ADMIN,
  PATIENT,
  DOCTOR,
}

enum class MedicationForm {
  TABLET,
  CAPSULE,
  LIQUID,
  INJECTION,
  POWDER,
  TOPICAL,
  CREAM,
  DEVICE,
  DROPS,
  FOAM,
  GEL,
  INHALER,
  LOTION,
  OINTMENT,
  PATCH,
  SPRAY,
  SUPPOSITORY
}

enum class MedicationUnit {
  MG,
  MCG,
  G,
  ML,
  OZ,
}

enum class BloodType {
  A_POSITIVE,
  A_NEGATIVE,
  B_POSITIVE,
  B_NEGATIVE,
  AB_POSITIVE,
  AB_NEGATIVE,
  O_POSITIVE,
  O_NEGATIVE,
  UNKNOWN,
}

