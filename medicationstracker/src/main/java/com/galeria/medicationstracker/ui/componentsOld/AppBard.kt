package com.galeria.medicationstracker.ui.componentsOld

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.galeria.medicationstracker.core.ui.theme.MedTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlyTopAppBar(
    title: String = "Title",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    icon: ImageVector? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    textStyle: TextStyle = MedTrackerTheme.typography.title1Emphasized
) {
    Column(
        modifier = Modifier
            .background(
                color = MedTrackerTheme.colors.primaryBackground
            )
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 0.dp),
            verticalAlignment = Alignment.Top,

        ) {
            Text(text = title, style = textStyle)

            // Spacer(modifier = Modifier.weight(1f))

            if (icon != null) {
                IconButton(
                    onClick = { onClick.invoke() },
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(28.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            }

        }
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        HorizontalDivider(
            color = MedTrackerTheme.colors.opaqueSeparator,
            /*       modifier = Modifier.layout() { measurable, constraints ->
                    val placeable = measurable.measure(
                      constraints.copy(
                        maxWidth = constraints.maxWidth + 16.dp.roundToPx(),
                      )
                    )
                    layout(placeable.width, placeable.height) {
                      placeable.place(8.dp.roundToPx(), 0)
                    }
                  } */
        )

    }
}
