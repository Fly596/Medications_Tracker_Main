package com.galeria.medicationstracker.data

import android.icu.text.SimpleDateFormat
import com.google.firebase.Timestamp
import java.time.LocalDateTime
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

data class Medication(
    val name: String = "",
    val classType: String = "", // Переименовал class, потому что это зарезервированное слово в Kotlin
    val form: MedicationForm = MedicationForm.UNKNOWN,
    val strength: Float = 0f,
    val unit: MedicationUnit = MedicationUnit.MG,
    val requiresPrescription: Boolean = false,
    val sideEffects: List<String> = emptyList(),
    val contraindications: List<String> = emptyList(),
    val interactions: Map<String, InteractionType> = emptyMap(),
    val dosageInstructions: String = "",
)

data class Interaction(
    val medication1: String = "",
    val medication2: String = "",
    val effect: String = "",
    val severity: String = "",
)

sealed class InteractionType(val severity: String) {
    object Major : InteractionType("Высокий риск")
    object Moderate : InteractionType("Средний риск")
    object Minor : InteractionType("Низкий риск")
    object Unknown : InteractionType("Неизвестно")

    companion object {

        fun fromString(value: String): InteractionType {
            return when (value.uppercase()) {
                "MAJOR" -> Major
                "MODERATE" -> Moderate
                "MINOR" -> Minor
                else -> Unknown
            }
        }
    }
}

enum class Symptom {
    FATIGUE,
    FEVER,
}

data class Note(
    val title: String = "",
    val content: String = "",
    val date: Timestamp = Timestamp.now(),
    val tags: List<String> = emptyList(),
    val medication: List<String> = emptyList(),
)

data class UserProfile(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val weight: Float? = null,
    val height: Float? = null,
    val email: String = "",
    val dateOfBirth: Timestamp = Timestamp.now(),
    val bloodType: BloodType = BloodType.UNKNOWN,
    val sex: String = "Unknown",
)

data class UserIntake(
    val uid: String? = null,
    val medicationName: String? = null,
    val dose: String? = null,
    val status: IntakeStatus = IntakeStatus.PENDING,
    val dateTime: Timestamp? = null,
)

enum class IntakeStatus {
    TAKEN,
    SKIPPED,
    PENDING,
}

sealed class UserIntakeStatus {
    object Taken : UserIntakeStatus() // Принято
    object Skipped : UserIntakeStatus() // Пропущено
    object Pending : UserIntakeStatus() // Ожидает приема

    companion object {

        fun fromBoolean(status: Boolean?): UserIntakeStatus {
            return when (status) {
                true -> Taken
                false -> Skipped
                null -> Pending
            }
        }
    }
}


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
    UNKNOWN,
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

/*TODO: Symptoms, mood..*/
data class MoodLog(
    val date: LocalDateTime = LocalDateTime.now()
    // TODO
)

data class NotificationPreferences(
    val sound: Boolean = true,
    val vibration: Boolean = true,
)

