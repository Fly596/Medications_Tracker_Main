package com.galeria.medtracker2.feature.tracker.domain

import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.repository.MedicationsCourseRepository
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

class GetIntakeTimesUseCase
@Inject
constructor(private val medicationsCourseRepository: MedicationsCourseRepository) {

    suspend operator fun invoke(medId: UUID): List<LocalTime> {
        // 1. Получаем курс по id лекарства.
        val course =
            medicationsCourseRepository.getCourseByMedId(medId)
                ?: throw Exception("Course not found")

        // 2. Получаем список всех запланированных приемов этого курса.
        val plannedIntakes = medicationsCourseRepository.getPlanedIntakesByCourseId(course.id)

        val intakeTimes = mutableListOf<LocalTime>()
        for (plannedIntake in plannedIntakes) {
            // 3. Получаем время приема и конвертируем его в LocalTime.
            val scheduledIntakeInstant = plannedIntake.scheduledTimestamp
            val intakeDateTime =
                DateTimeUtils.fromLongToLocalDateTime(scheduledIntakeInstant.toEpochMilli())
            val intakeTime = intakeDateTime.toLocalTime()

            // 4. Добавляем время приема в список, если оно еще не добавлено.
            if (!intakeTimes.contains(intakeTime)) {
                intakeTimes.add(intakeTime)
            }
        }
        return intakeTimes
    }
}
// 1. Получаем id лекарства из savedStateHandle (EditMedicationVM).
// 2. Получаем id курса по id лекарства. (MedicationsCourseRepository.getCourseByMedId) +
// (MedicationCourseDao.getMedicationCourseByMedId)
//
