package com.galeria.medtracker2.core.common.data

// Для отображения основной информации о препарате.
data class Medication(
    val name: String,
    val dosage: String,
    val startDate: String,
    val endDate: String,
    val intakeTimes: List<String>,
)