package com.galeria.medicationstracker.ui.componentsOld

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.galeria.medicationstracker.core.ui.theme.MedTrackerTheme

@Composable
fun MyRadioButton(
    modifier: Modifier = Modifier,
    caption: String? = null,
    selected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors =
            RadioButtonDefaults.colors(
                selectedColor = MedTrackerTheme.colors.primary400,
                unselectedColor = MedTrackerTheme.colors.secondaryLabel,
                disabledSelectedColor = MedTrackerTheme.colors.primary400.copy(alpha = 0.38f),
                disabledUnselectedColor = MedTrackerTheme.colors.secondaryLabel.copy(alpha = 0.38f),
            ),
    )
    if (caption != null) {
        Text(
            text = caption
        )
    }
}

@Composable
fun MyCheckbox(
    checked: Boolean = false,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Checkbox(
        checked = checked,
        onCheckedChange,
        modifier,
        enabled,
        colors =
            CheckboxDefaults.colors(
                checkedColor = MedTrackerTheme.colors.primary400,
                uncheckedColor = MedTrackerTheme.colors.secondaryLabel,
                disabledCheckedColor = MedTrackerTheme.colors.primary400.copy(alpha = 0.38f),
                disabledUncheckedColor = MedTrackerTheme.colors.secondaryLabel.copy(alpha = 0.38f),
                disabledIndeterminateColor = MedTrackerTheme.colors.primary400.copy(alpha = 0.38f),
            ),
    )
}
