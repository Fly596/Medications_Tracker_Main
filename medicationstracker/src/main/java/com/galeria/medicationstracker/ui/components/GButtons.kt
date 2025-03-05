package com.galeria.medicationstracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galeria.medicationstracker.ui.theme.GAppTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.colors


@Composable
fun GPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = GAppTheme.typography.labelLargeEmphasized,
    isError: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    val buttonColors =
        when (isError) {
            true -> {
                ButtonDefaults.buttonColors(
                    containerColor = colors.sysError,
                    contentColor = colors.primaryLabelDark,
                    disabledContainerColor = colors.secondaryFill,
                    disabledContentColor = colors.tertiaryLabel,
                )
            }
            
            false -> {
                ButtonDefaults.buttonColors(
                    containerColor = colors.sysBlack,
                    contentColor = colors.primaryLabelDark,
                    disabledContainerColor = colors.secondaryFill,
                    disabledContentColor = colors.tertiaryLabel,
                )
            }
        }
    Button(
        onClick,
        modifier,
        enabled,
        shape = GAppTheme.shapes.small,
        colors = buttonColors,
        content = { ProvideTextStyle(value = textStyle) { content() } },
    )
}

@Composable
fun GSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = GAppTheme.typography.labelLarge,
    contentPaddings: PaddingValues = PaddingValues(
        horizontal = 24.dp,
        vertical = 8.dp
    ),
    shape: Shape = GAppTheme.shapes.small,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick,
        modifier,
        enabled,
        contentPadding = contentPaddings,
        shape = shape,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = colors.secondary400,
                contentColor = colors.primaryLabelDark,
                disabledContainerColor = colors.secondaryFill,
                disabledContentColor = colors.tertiaryLabel,
            ),
        content = { ProvideTextStyle(value = textStyle) { content() } },
    )
}

@Composable
fun GTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    errorButton: Boolean = false,
    enabled: Boolean = true,
    textStyle: TextStyle = GAppTheme.typography.bodyMedium,
    contentPaddings: PaddingValues = PaddingValues(
    ),
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick,
        modifier,
        enabled,
        contentPadding = contentPaddings,
        shape = GAppTheme.shapes.small,
        colors = if (errorButton) {
            ButtonDefaults.textButtonColors(
                contentColor = colors.sysError,
                containerColor = colors.sysTransparent,
            )
        } else {
            ButtonDefaults.textButtonColors(
                containerColor = Color.Transparent,
                contentColor = colors.primaryLabel,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = colors.tertiaryLabel,
            )
        },
        content = { ProvideTextStyle(value = textStyle) { content() } },
    )
}

@Composable
fun GOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = GAppTheme.typography.labelLarge,
    isError: Boolean = false,
    contentPaddings: PaddingValues = PaddingValues(
        horizontal = 24.dp,
        vertical = 8.dp
    ),
    shape: Shape = GAppTheme.shapes.small,
    content: @Composable RowScope.() -> Unit,
) {
    val buttonColors =
        when (isError) {
            true -> {
                ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.sysError,
                    containerColor = colors.sysError,
                    disabledContentColor = colors.tertiaryLabel,
                )
            }
            
            false -> {
                ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.primaryLabel,
                    disabledContentColor = colors.tertiaryLabel,
                )
            }
        }
    
    OutlinedButton(
        onClick,
        modifier,
        enabled,
        contentPadding = contentPaddings,
        shape = GAppTheme.shapes.small,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = colors.primaryLabel,
            disabledContentColor = colors.tertiaryLabel,
        ),
        border = BorderStroke(
            width = 0.5.dp,
            color = colors.secondaryLabel
        ),
        content = { ProvideTextStyle(value = textStyle) { content() } },
    )
}

@Composable
fun GTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = GAppTheme.typography.labelLarge,
    contentPaddings: PaddingValues = PaddingValues(
        horizontal = 24.dp,
        vertical = 8.dp
    ),
    shape: Shape = GAppTheme.shapes.small,
    content: @Composable RowScope.() -> Unit,
) {
    FilledTonalButton(
        onClick,
        modifier,
        enabled,
        contentPadding = contentPaddings,
        shape = shape,
        colors =
            ButtonDefaults.filledTonalButtonColors(
                containerColor = colors.separator,
                contentColor = colors.primary600,
                disabledContainerColor = colors.secondaryFill,
                disabledContentColor = colors.tertiaryLabel,
            ),
        content = { ProvideTextStyle(value = textStyle) { content() } },
    )
}

@Composable
fun GRadioButton(
    modifier: Modifier = Modifier,
    caption: String? = null,
    selected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors =
                RadioButtonDefaults.colors(
                    selectedColor = colors.sysBlack,
                    unselectedColor = colors.secondaryLabel,
                    disabledSelectedColor = colors.sysBlack.copy(alpha = 0.38f),
                    disabledUnselectedColor = colors.secondaryLabel.copy(alpha = 0.38f),
                ),
        )
        if (caption != null) {
            Text(
                text = caption
            )
        }
    }
    
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun GPrimaryButtonPreview() {
    MedTrackerTheme {
        Column {
            Row {
                GPrimaryButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(16.dp),
                    enabled = true,
                    textStyle = MedTrackerTheme.typography.bodyMedium,
                    content = {
                        androidx.compose.material3.Text("Button Text")
                    },
                )
                GPrimaryButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(16.dp),
                    enabled = false,
                    textStyle = MedTrackerTheme.typography.bodyMedium,
                    content = {
                        androidx.compose.material3.Text("Button Text")
                    },
                )
            }
            Row {
                GSecondaryButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(16.dp),
                    enabled = true,
                    textStyle = MedTrackerTheme.typography.bodyMedium,
                    content = {
                        androidx.compose.material3.Text("Button Text")
                    },
                )
                GSecondaryButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(16.dp),
                    enabled = false,
                    textStyle = MedTrackerTheme.typography.bodyMedium,
                    content = {
                        androidx.compose.material3.Text("Button Text")
                    },
                )
            }
            Row {
                GTextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(16.dp),
                    enabled = true,
                    textStyle = MedTrackerTheme.typography.bodyMedium,
                    content = {
                        androidx.compose.material3.Text("Button Text")
                    },
                )
                GTextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(16.dp),
                    enabled = false,
                    textStyle = MedTrackerTheme.typography.bodyMedium,
                    content = {
                        androidx.compose.material3.Text("Button Text")
                    },
                )
            }
            Row {
                GOutlinedButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(16.dp),
                    enabled = true,
                    textStyle = MedTrackerTheme.typography.bodyMedium,
                    content = {
                        androidx.compose.material3.Text("Button Text")
                    }
                )
                GOutlinedButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(16.dp),
                    enabled = false,
                    textStyle = MedTrackerTheme.typography.bodyMedium,
                    content = {
                        androidx.compose.material3.Text("Button Text")
                    }
                )
            }
            Row {
                GTonalButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(16.dp),
                    enabled = true,
                    textStyle = MedTrackerTheme.typography.bodyMedium,
                    content = {
                        androidx.compose.material3.Text("Button Text")
                    }
                )
                GTonalButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(16.dp),
                    enabled = false,
                    textStyle = MedTrackerTheme.typography.bodyMedium,
                    content = {
                        androidx.compose.material3.Text("Button Text")
                    }
                )
            }
        }
    }
    
}
/* @Composable
fun GIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = GappTheme.typography.labelLarge,
    @DrawableRes icon: Int = R.drawable.male_symbol,
) {
    Box(
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable(onClick = { onClick.invoke() })
            .size(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painterResource(icon),
            contentDescription = null,
            tint = MedTrackerTheme.colors.primary400,
        )
    }
} */