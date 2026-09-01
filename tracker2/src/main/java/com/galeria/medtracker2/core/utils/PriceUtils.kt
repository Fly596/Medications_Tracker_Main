package com.galeria.medtracker2.core.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

fun Double.roundTo(decimals: Int): Double {
    return BigDecimal(this.toString())
        .setScale(decimals, RoundingMode.HALF_UP)
        .toDouble()
}

// Для UI
fun Double.toFormattedString(decimals: Int = 2, locale: Locale = Locale.getDefault()): String {
    return String.format(locale, "%.${decimals}f", this)
}