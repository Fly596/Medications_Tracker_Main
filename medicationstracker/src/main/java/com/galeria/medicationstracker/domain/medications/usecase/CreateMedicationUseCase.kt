package com.galeria.medicationstracker.domain.medications.usecase

import com.galeria.medicationstracker.domain.medications.repository.MedicationRepository
import javax.inject.Inject

class CreateMedicationUseCase @Inject constructor(
    private val medicationRepository: MedicationRepository
) {

}