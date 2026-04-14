package com.galeria.medtracker2.core.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class GShapesImmut(
    val maxLarge: Shape,
    val extraLarge: Shape,
    val large: Shape,
    val medium: Shape,
    val small: Shape,
    val extraSmall: Shape,
    val circle: Shape,
    val rectangle: Shape,
)

val GShapes = GShapesImmut(
    maxLarge = RoundedCornerShape(100.dp),
    extraLarge = RoundedCornerShape(32.dp),
    large = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(4.dp),
    extraSmall = RoundedCornerShape(2.dp),
    circle = CircleShape,
    rectangle = RectangleShape,
)