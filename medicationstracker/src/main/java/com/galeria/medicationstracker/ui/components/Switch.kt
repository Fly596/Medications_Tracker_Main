package com.galeria.medicationstracker.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme

@Composable
fun MySwitch(
  checked: Boolean,
  onCheckedChange: ((Boolean) -> Unit)?,
  modifier: Modifier = Modifier,
  thumbContent: @Composable() (() -> Unit)? = null,
  enabled: Boolean = true,
  colors: SwitchColors = SwitchDefaults.colors(),
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
): Unit {
  Switch(
    checked,
    onCheckedChange,
    modifier,
    thumbContent,
    enabled,
    colors = SwitchDefaults.colors(
      checkedThumbColor = MedTrackerTheme.colors.sysWhite,
      checkedTrackColor = MedTrackerTheme.colors.secondary400,
      checkedBorderColor = MedTrackerTheme.colors.sysTransparent,
      uncheckedBorderColor = MedTrackerTheme.colors.sysTransparent,
      uncheckedThumbColor = MedTrackerTheme.colors.sysWhite,
      uncheckedTrackColor = MedTrackerTheme.colors.secondaryFill

    ),
    interactionSource
  )
}