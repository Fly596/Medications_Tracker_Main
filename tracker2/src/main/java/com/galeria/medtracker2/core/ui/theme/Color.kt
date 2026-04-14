package com.galeria.medtracker2.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val systemLightRed = Color(0xFFff3b30)
val systemLightOrange = Color(0xFFff9500)
val systemLightYellow = Color(0xFFffcc00)
val systemLightGreen = Color(0xFF34c759)
val systemLightMint = Color(0xFF00c7be)
val systemLightCyan = Color(0xFF32ade6)
val systemLightBlue = Color(0xFF007aff)
val systemLightIndigo = Color(0xFF5856d6)
val systemLightPink = Color(0xFFff2d55)
val systemLightGray = Color(0xFF8e8e93)
val systemLightGray2 = Color(0xFFaeaeb2)
val systemLightGray3 = Color(0xFFc7c7cc)
val systemLightGray4 = Color(0xFFd1d1d6)
val systemLightGray5 = Color(0xFFe5e5ea)
val systemLightGray6 = Color(0xFFf2f2f7)

val systemDarkRed = Color(0xFFff453a)
val systemDarkOrange = Color(0xFFff9f0a)
val systemDarkYellow = Color(0xFFffd60a)
val systemDarkGreen = Color(0xFF30d158)
val systemDarkMint = Color(0xFF66d4cf)
val systemDarkCyan = Color(0xFF64d2ff)
val systemDarkBlue = Color(0xFF0a84ff)
val systemDarkIndigo = Color(0xFF5e5ce6)
val systemDarkPink = Color(0xFFff375f)
val systemDarkGray = Color(0xFF8e8e93)
val systemDarkGray2 = Color(0xFF636366)
val systemDarkGray3 = Color(0xFF48484a)
val systemDarkGray4 = Color(0xFF3a3a3c)
val systemDarkGray5 = Color(0xFF2c2c2e)
val systemDarkGray6 = Color(0xFF1c1c1e)

val backgroundPrimaryLight = Color(0xFFFFFFFF)
val backgroundSecondaryLight = Color(0xFFF2F2F7)
val backgroundTertiaryLight = Color(0xFFFFFFFF)

// Containers.
val backgroundPrimaryGroupedLight = Color(0xFFf2f2f7)
val backgroundSecondaryGroupedLight = Color(0xFFffffff)
val backgroundTertiaryGroupedLight = Color(0xFFf2f2f7)

// Separators.
val separatorOpaque = Color(0xFFC6C6C8)
val separatorNonOpaque = Color(0x57545456)

// Typography.
val labelPrimary = Color(0xFF000000)
val labelSecondary = Color(0x993C3C43)
val labelTertiary = Color(0x4D3C3C43)
val labelQuaternary = Color(0x2E3C3C43)

@Immutable
data class AppSystemColors(
    val backgroundPrimary: Color,
    val backgroundSecondary: Color,
    val backgroundTertiary: Color,
    val backgroundPrimaryGrouped: Color,
    val backgroundSecondaryGrouped: Color,
    val backgroundTertiaryGrouped: Color,
    val separatorOpaque: Color,
    val separatorNonOpaque: Color,
    val labelPrimary: Color,
    val labelSecondary: Color,
    val labelTertiary: Color,
    val labelQuaternary: Color,
)

val userProfileGradient = arrayOf(
    0.0f to Color(0xFFFF9D76),
    0.5f to Color(0xFFFDF5CB),
    1.0f to Color(0xFF7EB8F0),
)

/*val LocalLightRepCustomColors = staticCompositionLocalOf {
    AppSystemColors(
        backgroundPrimary = backgroundPrimaryLight,
        backgroundSecondary = backgroundSecondaryLight,
        backgroundTertiary = backgroundTertiaryLight,
        backgroundPrimaryGrouped = backgroundPrimaryGroupedLight,
        backgroundSecondaryGrouped = backgroundSecondaryGroupedLight,
        backgroundTertiaryGrouped = backgroundTertiaryGroupedLight,
        separatorOpaque = separatorOpaque,
        separatorNonOpaque = separatorNonOpaque,
        labelPrimary = labelPrimary,
        labelSecondary = labelSecondary,
        labelTertiary = labelTertiary,
        labelQuaternary = labelQuaternary,
    )
}*/

val LocalLightSystemColors = staticCompositionLocalOf {
    AppSystemColors(
        backgroundPrimary = Color.Unspecified,
        backgroundSecondary = Color.Unspecified,
        backgroundTertiary = Color.Unspecified,
        backgroundPrimaryGrouped = Color.Unspecified,
        backgroundSecondaryGrouped = Color.Unspecified,
        backgroundTertiaryGrouped = Color.Unspecified,
        separatorOpaque = Color.Unspecified,
        separatorNonOpaque = Color.Unspecified,
        labelPrimary = Color.Unspecified,
        labelSecondary = Color.Unspecified,
        labelTertiary = Color.Unspecified,
        labelQuaternary = Color.Unspecified,
    )
}

@Immutable
data class ColorSpecs(
    val defaultLight: Color,
    val defaultDark: Color,
)

/*@Immutable
data class ExtendedColors(
    val extRed: ColorSpecs,
    val extOrange: ColorSpecs,
    val extYellow: ColorSpecs,
    val extGreen: ColorSpecs,
    val extMint: ColorSpecs,
    val extCyan: ColorSpecs,
    val extBlue: ColorSpecs,
    val extIndigo: ColorSpecs,
    val extPink: ColorSpecs,
    val extGray: ColorSpecs,
    val extGray2: ColorSpecs,
    val extGray3: ColorSpecs,
    val extGray4: ColorSpecs,
    val extGray5: ColorSpecs,
    val extGray6: ColorSpecs,
    val extWhite: ColorSpecs,
    val extBlack: ColorSpecs,
)*/
@Immutable
data class ExtendedColors(
    val extRed: Color,
    val extOrange: Color,
    val extYellow: Color,
    val extGreen: Color,
    val extMint: Color,
    val extCyan: Color,
    val extBlue: Color,
    val extIndigo: Color,
    val extPink: Color,
    val extGray: Color,
    val extGray2: Color,
    val extGray3: Color,
    val extGray4: Color,
    val extGray5: Color,
    val extGray6: Color,
    val extWhite: Color,
    val extBlack: Color,
)

val LocalExtColorsLight = staticCompositionLocalOf {
    ExtendedColors(
        extRed = Color.Unspecified,
        extOrange = Color.Unspecified,
        extYellow = Color.Unspecified,
        extGreen = Color.Unspecified,
        extMint = Color.Unspecified,
        extCyan = Color.Unspecified,
        extBlue = Color.Unspecified,
        extIndigo = Color.Unspecified,
        extPink = Color.Unspecified,
        extGray = Color.Unspecified,
        extGray2 = Color.Unspecified,
        extGray3 = Color.Unspecified,
        extGray4 = Color.Unspecified,
        extGray5 = Color.Unspecified,
        extGray6 = Color.Unspecified,
        extWhite = Color.Unspecified,
        extBlack = Color.Unspecified,
    )
}

val LocalExtColorsDark = staticCompositionLocalOf {
    ExtendedColors(
        extRed = systemDarkRed,
        extOrange = systemDarkOrange,
        extYellow = systemDarkYellow,
        extGreen = systemDarkGreen,
        extMint = systemDarkMint,
        extCyan = systemDarkCyan,
        extBlue = systemDarkBlue,
        extIndigo = systemDarkIndigo,
        extPink = systemDarkPink,
        extGray = systemDarkGray,
        extGray2 = systemDarkGray2,
        extGray3 = systemDarkGray3,
        extGray4 = systemDarkGray4,
        extGray5 = systemDarkGray5,
        extGray6 = systemDarkGray6,
        extWhite = Color.White,
        extBlack = Color.Black,
    )
}

