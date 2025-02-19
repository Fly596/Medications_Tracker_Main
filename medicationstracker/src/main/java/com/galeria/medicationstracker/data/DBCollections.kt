package com.galeria.medicationstracker.data

import android.icu.text.SimpleDateFormat
import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.util.Locale

data class UserMedication(
    val uid: String? = null,
    val name: String? = null,
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

data class HospitalDrugs(
    val name: String? = null, // Drisdol.
    val drugClass: String? = null, // vitamins d derivatives.
    val availability: String? = null, // prescription sometimes needed.
    val strength: Float? = null, // 1.25.
    val unit: String? = null, // MG.
    val form: String? = null, // capsules.
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

val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.US)

/*TODO: Symptoms, mood..*/
data class MoodLog(
    val date: LocalDateTime = LocalDateTime.now()
    // TODO
)
/* data class Users(
  val name: String = "",
  val email: String = "",
  val pfp: String = ""  *//* url to pic*//* ,
  val born: String = ""  *//*yyyy-MM-dd*//* ,
  val notificationPreferences: NotificationPreferences = NotificationPreferences()
) */

data class NotificationPreferences(
    val sound: Boolean = true,
    val vibration: Boolean = true,
)
/* data class UserMedication(
  val medication: Medication = Medication(),
  val notes: String = "",
  val startDate: LocalDate = LocalDate.now(),
  val endDate: LocalDate? = null,
  val frequency: String = "",
  val time: LocalTime = LocalTime.now(),
) */
