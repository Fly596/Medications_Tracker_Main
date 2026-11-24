package com.galeria.medicationstracker.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.galeria.medicationstracker.core.ui.theme.MedTrackerTheme

@Composable
fun GDividerNegativePadding(
    modifier: Modifier = Modifier,
    dividerPadding: Dp = 0.dp
) {
    HorizontalDivider(
        modifier = modifier
            .height(20.dp)
            .layout { measurable, constraints ->
                val placeable =
                    // Step 1
                    measurable.measure(
                        constraints.offset(
                            (-dividerPadding * 2).roundToPx()
                        )
                    )
                layout(
                    // Step 2
                    placeable.width + (dividerPadding * 2).roundToPx(),
                    placeable.height
                    // Step 3
                ) { placeable.place(0 + dividerPadding.roundToPx(), 0) }
            },
        color = MedTrackerTheme.colors.separator
    )
}