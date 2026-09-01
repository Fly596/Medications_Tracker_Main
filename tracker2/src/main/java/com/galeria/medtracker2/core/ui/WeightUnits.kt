package com.galeria.medtracker2.core.ui

import androidx.annotation.StringRes
import com.galeria.medtracker2.R

// Единицы измерения.
enum class WeightUnits(
    @StringRes
    val label: Int,
) {

    GRAM(label = R.string.unit_grams),
    MILLIGRAM(label = R.string.unit_milligrams),
    MICROGRAM(label = R.string.unit_micrograms),
    KILOGRAM(label = R.string.unit_kilograms),
    MILLILITER(label = R.string.unit_milliliters),
    LITER(label = R.string.unit_liters),
    DEFAULT(label = R.string.unit_default)
}
