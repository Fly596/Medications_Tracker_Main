package com.galeria.medicationstracker.domain.medications.usecase

import com.galeria.medicationstracker.core.domain.repository._MedicationRepository
import javax.inject.Inject

class CreateMedicationUseCase @Inject constructor(
    private val medicationRepository: _MedicationRepository
) {

}