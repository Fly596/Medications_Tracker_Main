package com.galeria.medtracker2.core.ui.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.galeria.medtracker2.R
import com.galeria.medtracker2.core.ui.theme.ButtonShape
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme.colors

enum class HIGButtonStyle {
    Filled,
    Bezeled,
    Borderless,
}

// TODO: вынести цвета в тему.
@Composable
fun HIGButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: HIGButtonStyle = HIGButtonStyle.Filled,
) {
    val buttonColors =
        when (style) {
            HIGButtonStyle.Filled ->
                ButtonDefaults.buttonColors(
                    containerColor = colors.primary400,
                    contentColor = colors.sysWhite,
                    disabledContainerColor = colors.tertiaryFill,
                    disabledContentColor = colors.tertiaryLabel,
                )

            HIGButtonStyle.Bezeled ->
                ButtonDefaults.buttonColors(
                    containerColor = colors.primary400, /*Fills, tertiary*/
                    contentColor = colors.primaryLabelDark,
                    /*.defaultLight*/
                    /*Labels, tertiary*/
                    disabledContainerColor = Color(0x1f787880),
                    disabledContentColor = Color(0x4d3c3c43),
                )

            HIGButtonStyle.Borderless ->
                ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.primary400,
                    disabledContentColor = Color(0x4d3c3c43),
                )
        }

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_large)),
        modifier = modifier,
        colors = buttonColors,
    ) {
        Text(text = text, style = MedTrackerTheme.typography.bodyMedium)
    }
}

@Composable
fun HIGListButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    style: HIGButtonStyle = HIGButtonStyle.Bezeled,
    textStyle: TextStyle = MedTrackerTheme.typography.bodyMedium,
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
    leftIcon: ImageVector? = null,
    rightText: String? = null
) {
    val buttonColors =
        when (style) {
            HIGButtonStyle.Filled ->
                ButtonDefaults.elevatedButtonColors(
                    containerColor = colors.primaryBackgroundGrouped,
                    /*Fills, tertiary*/
                    contentColor = Color.White,
                    disabledContainerColor = Color(0x1f787880),
                    disabledContentColor = Color(0x4d3c3c43),
                )

            HIGButtonStyle.Bezeled ->
                ButtonDefaults.elevatedButtonColors(
                    containerColor = colors.primaryBackground,
                    /*Fills, tertiary*/
                    contentColor = colors.primaryLabel,
                    disabledContainerColor = Color(0x1f787880),
                    disabledContentColor = Color(0x4d3c3c43),
                )

            HIGButtonStyle.Borderless ->
                ButtonDefaults.elevatedButtonColors(
                    contentColor = colors.primary400,
                    disabledContentColor = Color(0x4d3c3c43),
                )
        }

    ElevatedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(dimensionResource(R.dimen.list_corner_radius_default)),
        colors = buttonColors,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (leftIcon != null) {
                    Icon(
                        imageVector = leftIcon,
                        contentDescription = null,
                        tint = colors.primaryLabel,
                        modifier = Modifier.size(24.dp),
                    )
                }

                Text(
                    text = text,
                    style = textStyle,
                    modifier = Modifier.padding(vertical = 8.dp),
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = rightText ?: "",
                    style = MedTrackerTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = "Show options",
                    tint = colors.secondaryLabel,
                    modifier = Modifier.size(16.dp),
                )
            }
    }
}

// Region rework
@Composable
fun FlyTextButton(
    onClick: () -> Unit,
    errorButton: Boolean = false,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MedTrackerTheme.typography.labelLargeEmphasized,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick,
        modifier,
        enabled,
        colors =
            if (errorButton) {
                ButtonDefaults.textButtonColors(
                    contentColor = colors.sysError, containerColor = colors.sysTransparent)
            } else {
                ButtonDefaults.textButtonColors(
                    contentColor = colors.primary600, containerColor = colors.sysTransparent)
            },
        content = { ProvideTextStyle(value = textStyle) { content() } },
    )
}

@Composable
fun FlyElevatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = MedTrackerTheme.typography.labelLargeEmphasized,
    shape: Shape = ButtonShape,
    content: @Composable RowScope.() -> Unit,
) {
    ElevatedButton(
        onClick,
        modifier,
        enabled,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
        shape = shape,
        colors =
            ButtonDefaults.elevatedButtonColors(
                containerColor = colors.tertiaryFill,
                contentColor = colors.primary600,
                disabledContainerColor = colors.tertiaryFill,
                disabledContentColor = colors.tertiaryLabel,
            ),
        content = { ProvideTextStyle(value = textStyle) { content() } },
    )
}

@Composable
fun FlyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = MedTrackerTheme.typography.labelLargeEmphasized,
    shape: Shape = ButtonShape,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick,
        modifier,
        enabled,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
        shape = shape,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = colors.primary400,
                contentColor = colors.primaryLabelDark,
                disabledContainerColor = colors.tertiaryFill,
                disabledContentColor = colors.tertiaryLabel,
            ),
        content = { ProvideTextStyle(value = textStyle) { content() } },
    )
}

@Composable
fun FlyErrorButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = MedTrackerTheme.typography.labelLargeEmphasized,
    shape: Shape = ButtonShape,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick,
        modifier,
        enabled,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
        shape = shape,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = colors.sysError,
                contentColor = colors.primaryLabelDark,
                disabledContainerColor = colors.tertiaryFill,
                disabledContentColor = colors.tertiaryLabel,
            ),
        content = { ProvideTextStyle(value = textStyle) { content() } },
    )
}

@Composable
fun FlyTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = MedTrackerTheme.typography.labelLargeEmphasized,
    shape: Shape = ButtonShape,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick,
        modifier,
        enabled,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
        shape = shape,
        colors =
            ButtonDefaults.filledTonalButtonColors(
                containerColor = colors.primaryTinted,
                contentColor = colors.primary600,
                disabledContainerColor = colors.tertiaryFill,
                disabledContentColor = colors.tertiaryLabel,
            ),
        content = { ProvideTextStyle(value = textStyle) { content() } },
    )
}

@Composable
fun FlyIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    size: Dp = 24.dp,
    @DrawableRes icon: Int = R.drawable.male_symbol
) {
    Box(
        modifier =
            modifier
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = { onClick.invoke() })
                .background(colors.primaryBackgroundGrouped)
                .padding(8.dp)
                .size(size),
        contentAlignment = Alignment.Center) {
            Icon(
                painterResource(icon),
                contentDescription = null,
                tint = colors.primary400,
                modifier = Modifier)
        }
}

@Composable
fun FlyIconButtonWithText(
    modifier: Modifier = Modifier,
    text: String = "Fuck Me",
    onClick: () -> Unit = {},
    size: Dp = 20.dp,
    @DrawableRes icon: Int = R.drawable.male_symbol
) {
    Row(
        modifier = modifier.clickable(onClick = { onClick.invoke() }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FlyIconButton(onClick = onClick, size = size, icon = icon)
            Text(text = text, style = MedTrackerTheme.typography.labelLargeEmphasized)
        }
}

@Composable
fun GSmallButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String = "Sign In"
): Unit {
    Button(
        modifier = modifier,
        enabled = enabled,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = colors.primary400,
                contentColor = colors.sysWhite,
                disabledContainerColor = colors.tertiaryFill,
                disabledContentColor = colors.tertiaryLabel,
            ),
        shape = RoundedCornerShape(40.dp),
        onClick = onClick,
    ) {
        Text(
            text = text,
            style = MedTrackerTheme.typography.bodyMediumEmphasized,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
    }
}

@Composable
fun GMediumButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String = "Sign In"
): Unit {
    Button(
        modifier = modifier,
        enabled = enabled,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = colors.primary400,
                contentColor = colors.sysWhite,
                disabledContainerColor = colors.tertiaryFill,
                disabledContentColor = colors.tertiaryLabel,
            ),
        shape = RoundedCornerShape(40.dp),
        onClick = onClick,
    ) {
        Text(
            text = text,
            style = MedTrackerTheme.typography.bodyMediumEmphasized,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp))
    }
}

@Composable
fun GLargeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String = "Sign In"
): Unit {
    Button(
        modifier = modifier,
        enabled = enabled,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = colors.primary400,
                contentColor = colors.sysWhite,
                disabledContainerColor = colors.tertiaryFill,
                disabledContentColor = colors.tertiaryLabel,
            ),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
    ) {
        Text(
            text = text,
            style = MedTrackerTheme.typography.bodyMediumEmphasized,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp))
    }
}

// Endregion
@Preview(
    name = "Buttons",
    showSystemUi = true,
    device = "spec:parent=pixel_8,navigation=buttons",
    showBackground = true,
    backgroundColor = 0xFFD4CBCB,
    uiMode =
        Configuration.UI_MODE_NIGHT_NO or
            Configuration.UI_MODE_TYPE_NORMAL,
)

@Composable
private fun PreviewButtons() {
    MedTrackerTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Column {
                GMediumButton(onClick = {}, enabled = true, text = "GMediumButton")
                GLargeButton(onClick = {}, enabled = true, text = "GLargeButton")
                                 FlyButton(onClick = {}, enabled = true) { Text(text = "Sign In") }
                FlyTextButton(onClick = {}, enabled = true) { Text(text = "Sign In") }
                FlyElevatedButton(onClick = {}, enabled = true) { Text(text = "Sign In") }
                 FlyTonalButton(onClick = {}, enabled = true) { Text(text = "Sign In") }
                HIGListButton("text", onClick = {}, enabled = true)
            }
        }
    }
}
