package com.galeria.medtracker2.domain.model

import com.galeria.medtracker2.core.ui.WeightUnits

data class Dose(
    val amount: Double,
    val unit: WeightUnits
)