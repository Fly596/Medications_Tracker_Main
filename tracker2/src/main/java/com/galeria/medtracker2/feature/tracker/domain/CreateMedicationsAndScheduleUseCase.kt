package com.galeria.medtracker2.feature.tracker.domain

import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.model.AlarmItem
import com.galeria.medtracker2.domain.model.MedicationCourseDomain
import com.galeria.medtracker2.domain.model.MedicationDomain
import com.galeria.medtracker2.domain.model.PlannedIntakeDomain
import com.galeria.medtracker2.domain.repository.AlarmScheduler
import com.galeria.medtracker2.domain.repository.MedicationRepository
import com.galeria.medtracker2.domain.repository.MedicationsCourseRepository
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

class CreateMedicationsAndScheduleUseCase @Inject constructor(
    private val medicationRepository: MedicationRepository,
    private val medicationsCourseRepository: MedicationsCourseRepository,
    //private val notificationService: ScheduleNotificationRepoImpl,
    private val alarmScheduler: AlarmScheduler,

    ) {

    suspend operator fun invoke(
        name: String,
        dose: Double,
        startDate: Long,
        endDate: Long,
        intakeTimes: List<LocalTime>
    ) {
        val today = LocalDate.now()
        val cleanName = name.trim()
        val startLocalDate = DateTimeUtils.fromLongToLocalDate(startDate)
        val endLocalDate = DateTimeUtils.fromLongToLocalDate(endDate)

        // 1. Бизнес-валидация.
        require(cleanName.isNotBlank()) { "Name cannot be empty" }
        require(dose >= 0) { "Dose should be more than 0." }
        require(!startLocalDate.isBefore(today))
        require(endLocalDate.isAfter(startLocalDate))
        require(intakeTimes.isNotEmpty())

        // 2. Проверка на существование лекарства.
        val exitingMedication = medicationRepository.getMedicationByName(cleanName)

        // 3. Определение UUID, которое будем юзать.
        val medicationId: UUID = if (exitingMedication!=null) {
            exitingMedication.id
        } else {
            val newId = UUID.randomUUID()
            // Создаем лекарство.
            medicationRepository.addMedication(MedicationDomain(newId, cleanName, Instant.now()))
            newId
        }

        // 4. Создаем и добавляем курс.
        val medicationCourseId = UUID.randomUUID()
        val medicationCourse = MedicationCourseDomain(
            medicationCourseId,
            medicationId,
            doseMg = dose,
            startDate = startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            endDate = endLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
        )
        medicationsCourseRepository.addCourse(medicationCourse)

        // 5. Генерируем приемы для расписания курса.
        val days = ChronoUnit.DAYS.between(startLocalDate, endLocalDate).toInt()
        val intakesList = mutableListOf<PlannedIntakeDomain>()
        val alarms = mutableListOf<AlarmItem>()

        for (i in 0 until days) {
            val intakeDate = startLocalDate.plusDays(i.toLong())

            intakeTimes.forEach { it ->
                val intakeMoment = DateTimeUtils.combineDateAndTime(intakeDate, it)
                val plannedIntakeId = UUID.randomUUID()

                intakesList.add(
                    PlannedIntakeDomain(
                        id = plannedIntakeId,
                        courseId = medicationCourseId,
                        scheduledTimestamp = intakeMoment
                    )
                )

                alarms.add(
                    AlarmItem(
                        id = plannedIntakeId,
                        timeMillis = intakeMoment.toEpochMilli(),
                        title = cleanName,
                        message = "Dose: $dose"

                    )
                )
            }
        }

        // 6. Сохраняем приемы и будильники.
        medicationsCourseRepository.addAllPlannedIntakes(intakesList)
        alarmScheduler.scheduleAll(alarms)
    }
}