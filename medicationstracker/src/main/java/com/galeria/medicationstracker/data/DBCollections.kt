package com.galeria.medicationstracker.data

import android.icu.text.SimpleDateFormat
import com.google.firebase.Timestamp
import java.util.Locale

data class UserMedication(
  val uid: String? = null,
  val name: String = "",
  val form: String? = null,
  val strength: Float? = null,
  val unit: String? = null,
  val startDate: Timestamp? = null,
  val endDate: Timestamp? = null,
  val daysOfWeek: List<String> = emptyList(),
  val intakeTime: String? = null,
  val notes: String? = null,
  val chosenStrengths: List<Float> = emptyList(),
)

data class User(
  val uid: String = "",
  val login: String = "",
  val type: UserType = UserType.PATIENT,
  val age: Int? = null,
  val name: String? = null,
  val weight: Float? = null,
  val height: Float? = null,
)

data class Note(
  val title: String = "",
  val content: String = "",
  val date: Timestamp = Timestamp.now(),
  val tags: List<String> = emptyList(),
  val medication: List<String> = emptyList(),
)

data class UserProfile(
  val uid: String = "",
  val firstName: String,
  val lastName: String,
  val weight: Float,
  val height: Float,
  val email: String,
  val dateOfBirth: Timestamp,
  val bloodType: BloodType,
  val sex: String,
)

data class UserIntake(
  val uid: String? = null,
  val medicationName: String? = null,
  val dose: String? = null,
  val status: Boolean? = null,
  val dateTime: Timestamp? = null,
)

data class Appointment(
  val date: Timestamp? = null,
  val time: String? = null,
  val doctor: String? = null,
  val patient: String? = null,
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
  /*     TOPICAL,
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
      SUPPOSITORY */
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
  UNKNOWN
}

val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
