package com.galeria.medicationstracker.core.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.UnfoldMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galeria.medicationstracker.core.ui.theme.MedTrackerTheme

// iOS HIG Color Tokens
private val IOSSystemBlue = Color(0xFF007AFF)
private val IOSGroupedBackground = Color(0xFFF2F2F7)
private val IOSCardBackground = Color(0xFFFFFFFF)
private val IOSButtonBackground = Color(0x1F767680)
private val IOSDividerColor = Color(0x20000000)
private val IOSSecondaryText = Color(0x99000000)

/**
 * iOS-styled Dropdown List component adhering to Apple Human Interface Guidelines (HIG).
 * Features rounded squircle corners (18.dp), spring press feedback, thin 0.5.dp borders,
 * iOS System Blue highlights, and a glassmorphic menu popup with checkmark indicator.
 */
@Composable
fun GDropdownList(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedItem: String? = null,
    onItemSelected: (String) -> Unit
) {
    var internalSelected by remember { mutableStateOf(items.firstOrNull() ?: "") }
    val currentSelected = selectedItem ?: internalSelected
    var expanded by remember { mutableStateOf(false) }

    // Tactile spring press animation characteristic of iOS
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "iOSDropdownButtonSpring"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // iOS Capsule / Rounded Selection Button
        Surface(
            onClick = { expanded = true },
            interactionSource = interactionSource,
            shape = RoundedCornerShape(18.dp),
            color = IOSButtonBackground,
            border = BorderStroke(0.5.dp, IOSDividerColor),
            modifier = Modifier
                .scale(buttonScale)
                .clip(RoundedCornerShape(18.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = currentSelected,
                    style = MedTrackerTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = IOSSystemBlue,
                        fontSize = 15.sp
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Rounded.UnfoldMore,
                    contentDescription = "Select item",
                    tint = IOSSystemBlue,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // iOS Glassmorphic Popover Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White.copy(alpha = 0.94f),
            border = BorderStroke(0.5.dp, IOSDividerColor),
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = item == currentSelected

                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = item,
                                    style = MedTrackerTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (isSelected) IOSSystemBlue else Color.Black,
                                        fontSize = 16.sp
                                    )
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = "Selected",
                                        tint = IOSSystemBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        onClick = {
                            internalSelected = item
                            expanded = false
                            onItemSelected(item)
                        },
                        colors = MenuItemColors(
                            textColor = if (isSelected) IOSSystemBlue else Color.Black,
                            leadingIconColor = IOSSystemBlue,
                            trailingIconColor = IOSSystemBlue,
                            disabledTextColor = IOSSecondaryText,
                            disabledLeadingIconColor = IOSSecondaryText,
                            disabledTrailingIconColor = IOSSecondaryText
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    if (index < items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = IOSDividerColor
                        )
                    }
                }
            }
        }
    }
}

/**
 * Preview demonstrating iOS HIG Native Styling (Inset Grouped list, TopAppBar, Capsule Button,
 * System Blue accent, 20.dp rounded corners, and thin dividers).
 */
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DropdownMenuExamplePreview() {
    var selectedForm by remember { mutableStateOf("Pill") }
    var selectedFrequency by remember { mutableStateOf("Daily") }
    val forms = listOf("Pill", "Capsule", "Liquid", "Injection", "Ointment")
    val frequencies = listOf("Daily", "Twice a day", "Weekly", "As needed")

    MedTrackerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(IOSGroupedBackground)
        ) {
            // iOS Style TopAppBar (Centered title, elevation = 0, thin bottom divider)
            Surface(
                color = IOSCardBackground.copy(alpha = 0.85f),
                border = BorderStroke(0.5.dp, IOSDividerColor),
                shadowElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Add Medication",
                        style = MedTrackerTheme.typography.title3Emphasized.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 17.sp
                        )
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // iOS Large Title
                Text(
                    text = "Details",
                    style = MedTrackerTheme.typography.display3Emphasized.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 28.sp
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )

                // iOS Inset Grouped List Card
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = IOSCardBackground,
                    border = BorderStroke(0.5.dp, IOSDividerColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        // Row 1: Form Picker
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = "Medication Form",
                                style = MedTrackerTheme.typography.bodyLargeEmphasized.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                            )
                            GDropdownList(
                                items = forms,
                                selectedItem = selectedForm,
                                onItemSelected = { selectedForm = it }
                            )
                        }

                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = IOSDividerColor,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        // Row 2: Frequency Picker
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = "Schedule Frequency",
                                style = MedTrackerTheme.typography.bodyLargeEmphasized.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                            )
                            GDropdownList(
                                items = frequencies,
                                selectedItem = selectedFrequency,
                                onItemSelected = { selectedFrequency = it }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // iOS Capsule Action Button with Spring Animation
                val buttonInteraction = remember { MutableInteractionSource() }
                val isButtonPressed by buttonInteraction.collectIsPressedAsState()
                val buttonScale by animateFloatAsState(
                    targetValue = if (isButtonPressed) 0.96f else 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "iOSCapsuleButtonSpring"
                )

                Surface(
                    onClick = { },
                    interactionSource = buttonInteraction,
                    shape = RoundedCornerShape(50.dp),
                    color = IOSSystemBlue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .scale(buttonScale)
                        .clip(RoundedCornerShape(50.dp))
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Save Medication",
                            style = MedTrackerTheme.typography.bodyLargeEmphasized.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 17.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
