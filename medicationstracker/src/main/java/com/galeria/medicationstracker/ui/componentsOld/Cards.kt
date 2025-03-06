package com.galeria.medicationstracker.ui.componentsOld

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.galeria.medicationstracker.data.UserMedication
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme

@Composable
fun FlySimpleCard(
    modifier: Modifier = Modifier,
    isPrimaryBackground: Boolean = true,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = if (isPrimaryBackground) {
                    MedTrackerTheme.colors.primaryBackground
                } else {
                    MedTrackerTheme.colors.secondaryBackground
                },
                contentColor = MedTrackerTheme.colors.primaryLabel,
                disabledContainerColor = MedTrackerTheme.colors.primaryTinted,
                disabledContentColor = MedTrackerTheme.colors.secondary600
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content.invoke(this)
        }
    }
}

@Composable
fun FlyElevatedCardDashboard(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String = "Medicine Name",
    time: String = "9:00 AM",
    info: String = "Mon, Tue, Fri...",
    medication: UserMedication? = null,
    shape: Shape = RoundedCornerShape(8.dp),
    elevation: CardElevation = CardDefaults.elevatedCardElevation(),
    // content: @Composable (ColumnScope.() -> Unit)
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 1.dp,
            pressedElevation = 8.dp,
            focusedElevation = 10.dp,
        ),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MedTrackerTheme.colors.primaryBackground,
                contentColor = MedTrackerTheme.colors.primaryLabel,
                disabledContainerColor = MedTrackerTheme.colors.primaryTinted,
                disabledContentColor = MedTrackerTheme.colors.secondary600
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Android Icon",
                    modifier = Modifier.size(32.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight(),
            ) {
                Text(title, style = MedTrackerTheme.typography.headline)
                Text(time, style = MedTrackerTheme.typography.bodyMedium)
                Text(info, style = MedTrackerTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.weight(1f))

            var isChecked by remember { mutableStateOf(false) } // State to track icon

            Column {
                IconButton(onClick = { isChecked = !isChecked }) {
                    Icon(
                        imageVector = if (isChecked) {
                            Icons.Filled.CheckCircle
                        } else {
                            Icons.Outlined.CheckCircle
                        },
                        contentDescription = "Android Icon",
                        modifier = Modifier.size(32.dp),
                        tint = if (isChecked) {
                            MedTrackerTheme.colors.primary400
                        } else {
                            MedTrackerTheme.colors.separator
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

            }

        }
    }
}

@Composable
fun FLySimpleCardContainer(
    modifier: Modifier = Modifier,
    shape: Shape = MedTrackerTheme.shapes.medium,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MedTrackerTheme.colors.secondaryBackgroundGrouped,
                contentColor = MedTrackerTheme.colors.primaryLabel,
                disabledContainerColor = MedTrackerTheme.colors.primaryTinted,
                disabledContentColor = MedTrackerTheme.colors.secondary600
            )
    ) {
        content()
    }
}

@Composable
fun NavigationRow(onClick: () -> Unit, label: String? = null) {
    Row(
        modifier = Modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        FlyTextButton(
            onClick
        ) {
            if (label != null) {
                Text(label)
            }
        }
    }
}

@Composable
fun LogsCard(name: String, status: String, date: String, time: String) {
    FlySimpleCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = name,
                    style = MedTrackerTheme.typography.bodyLargeEmphasized,
                    color = MedTrackerTheme.colors.primaryLabel
                )
                Text(
                    text = (if (status == "true") "Taken" else "Not Taken"),
                    style = MedTrackerTheme.typography.title2,
                    color = MedTrackerTheme.colors.secondaryLabel
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = date,
                    style = MedTrackerTheme.typography.title2,
                    color = MedTrackerTheme.colors.primaryLabel
                )
                Text(
                    text = time,
                    style = MedTrackerTheme.typography.title2Emphasized,
                    color = MedTrackerTheme.colors.primaryLabel
                )
            }
        }
    }
}