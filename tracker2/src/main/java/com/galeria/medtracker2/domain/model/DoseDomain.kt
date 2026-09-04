package com.galeria.medtracker2.domain.model

import com.galeria.medtracker2.core.ui.WeightUnits
import java.util.UUID

data class Dose(
    val amount: Double,
    val unit: WeightUnits
)

// Заготовленные дозировки.
data class DosagePreset(
    val id: Long,
    val medicationId: UUID,
    val dose: Dose,
    val name: String?
)
