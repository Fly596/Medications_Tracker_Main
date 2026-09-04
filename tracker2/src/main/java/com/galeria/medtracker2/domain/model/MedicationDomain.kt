package com.galeria.medtracker2.domain.model

import com.galeria.medtracker2.core.ui.WeightUnits
import java.time.Instant
import java.util.UUID

data class MedicationDomain(
    val id: UUID,
    val name: String,
    val unit: WeightUnits,
    val defaultPricePerUnit: Money?,
    val creationTimestamp: Instant
)

data class Money(
    val cents: Long,
    val currencyCode: String = "USD"
)

