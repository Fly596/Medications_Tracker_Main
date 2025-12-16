package com.galeria.medtracker2.feature_meds.data.source.remote

data class MedicationDto(
    val id: String,
    val name: String,
    val doseMg: Double?,
    val stock: Double?, // Кол-во штук/грамм..
    val stockMeasureUnit: String?, // Показывает это штуки или граммы.
    val drugClass: String?, // stim/opioid/benz..
)