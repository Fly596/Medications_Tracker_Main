package com.galeria.medicationstracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Immutable
data class GMedTrackerColors(
  val gPrimary: Color,
  val gPrimarybackground: Color,
  val gSecondarybackground: Color,
  val gSurface: Color,
  val gPrimarytext: Color,
  val gSecondarytext: Color,
  val gDisabledtext: Color,
  val gSeparator: Color,
  val gOpaqueSeparator: Color,
  val systemBlack: Color,
  val systemWhite: Color,
  val systemRed: Color,
  val systemGreen: Color,
  val systemYellow: Color,
  val systemTransparent: Color,
)

@Immutable
data class MedTrackerColors(
  val primary400: Color,
  val primary500: Color,
  val primary600: Color,
  val primaryLight: Color,
  val primaryTinted: Color,
  val secondary400: Color,
  val secondary500: Color,
  val secondary600: Color,
  val secondaryLight: Color,
  val secondaryTinted: Color,
  val primaryFill: Color, // for thin and small shapes, such as slider's track.
  val secondaryFill: Color, // for medium-size shapes, such as the background of a switch.
  val tertiaryFill: Color, // for large shapes, such as input fields, search bars, or buttons.
  val primaryBackground: Color,
  val secondaryBackground: Color,
  val tertiaryBackground: Color,
  // Containers.
  val primaryBackgroundGrouped: Color,
  val secondaryBackgroundGrouped: Color,
  val tertiaryBackgroundGrouped: Color,
  val primaryBackgroundDark: Color,
  val secondaryBackgroundDark: Color,
  val tertiaryBackgroundDark: Color,
  // Containers.
  val primaryGroupedBackgroundDark: Color,
  val secondaryGroupedBackgroundDark: Color,
  val tertiaryGroupedBackgroundDark: Color,
  // typo.
  val primaryLabel: Color,
  val secondaryLabel: Color,
  val tertiaryLabel: Color,
  val primaryLabelDark: Color,
  val secondaryLabelDark: Color,
  val tertiaryLabelDark: Color,
  // Separators.
  val separator: Color,
  val opaqueSeparator: Color,
  // Functional colors.
  val sysError: Color,
  val sysWarning: Color,
  val sysSuccess: Color,
  val sysWhite: Color,
  val sysBlack: Color,
  val sysTransparent: Color,
)

@Immutable
data class GAppTypography(
  val display1: TextStyle,
  val display1Emphasized: TextStyle,
  val display2: TextStyle,
  val display2Emphasized: TextStyle,
  val display3: TextStyle,
  val display3Emphasized: TextStyle,
  val headline: TextStyle,
  val headlineEmphasized: TextStyle,
  val title1: TextStyle,
  val title1Emphasized: TextStyle,
  val title2: TextStyle,
  val title2Emphasized: TextStyle,
  val title3: TextStyle,
  val title3Emphasized: TextStyle,
  val bodyLarge: TextStyle,
  val bodyLargeEmphasized: TextStyle,
  val bodyMedium: TextStyle,
  val bodyMediumEmphasized: TextStyle,
  val bodySmall: TextStyle,
  val bodySmallEmphasized: TextStyle,
  val labelLarge: TextStyle,
  val labelLargeEmphasized: TextStyle,
  val labelMedium: TextStyle,
  val labelMediumEmphasized: TextStyle,
  val labelSmall: TextStyle,
  val labelSmallEmphasized: TextStyle,
)

@Immutable
data class MedTrackerTypography(
  val largeTitle:
  TextStyle, // Used for titles and headings that need to make a strong visual impact.
  val largeTitleEmphasized: TextStyle,
  val title1:
  TextStyle, // A major section heading, often used for top-level content within a page or screen.
  val title1Emphasized:
  TextStyle, // A major section heading, often used for top-level content within a page or screen.
  val title2:
  TextStyle, // A secondary heading, used to introduce subsections or groups of related content.
  val title2Emphasized:
  TextStyle, // A secondary heading, used to introduce subsections or groups of related content.
  val title3: TextStyle, // A tertiary heading, used to organize content within a subsection.
  val title3Emphasized:
  TextStyle, // A tertiary heading, used to organize content within a subsection.
  // Body Text and Related Styles.
  val headline:
  TextStyle, // A short, attention-grabbing piece of text, often used for promotional or
  // call-to-action purposes.
  val headlineItalic:
  TextStyle, // A short, attention-grabbing piece of text, often used for promotional or
  // call-to-action purposes.
  val body:
  TextStyle, // The main content of a page or screen, used for paragraphs and blocks of text.
  val bodyEmphasized:
  TextStyle, // The main content of a page or screen, used for paragraphs and blocks of text.
  val callout:
  TextStyle, // Text that highlights important information or provides additional context.
  val calloutEmphasized:
  TextStyle, // Text that highlights important information or provides additional context.
  val subhead:
  TextStyle, // A smaller heading used to introduce a paragraph or section within the body
  // content.
  val subheadEmphasized:
  TextStyle, // A smaller heading used to introduce a paragraph or section within the body
  // content.
  val footnote:
  TextStyle, // Small-sized text typically placed at the bottom of a page or screen for additional
  // information or credits.
  val footnoteEmphasized:
  TextStyle, // Small-sized text typically placed at the bottom of a page or screen for additional
  // information or credits.
  val caption1:
  TextStyle, // Descriptive text accompanying an image or graphic, often placed below the visual
  // content.
  val caption1Emphasized:
  TextStyle, // Descriptive text accompanying an image or graphic, often placed below the visual
  // content.
  val caption2:
  TextStyle, // Smaller caption text, used for additional details or metadata related to an image
  // or graphic.
  val caption2Emphasized:
  TextStyle, // Smaller caption text, used for additional details or metadata related to an image
  // or graphic.
)

// TODO: shapes
val LocalMedTrackerColors = staticCompositionLocalOf {
  MedTrackerColors(
    primary400 = Color.Unspecified,
    primary500 = Color.Unspecified,
    primary600 = Color.Unspecified,
    primaryLight = Color.Unspecified,
    primaryTinted = Color.Unspecified,
    secondary400 = Color.Unspecified,
    secondary500 = Color.Unspecified,
    secondary600 = Color.Unspecified,
    secondaryLight = Color.Unspecified,
    secondaryTinted = Color.Unspecified,
    primaryFill = Color.Unspecified, // for thin and small shapes, such as slider's track.
    secondaryFill =
      Color.Unspecified, // for medium-size shapes, such as the background of a switch.
    tertiaryFill =
      Color.Unspecified, // for large shapes, such as input fields, search bars, or buttons.
    primaryBackground = Color.Unspecified,
    secondaryBackground = Color.Unspecified,
    tertiaryBackground = Color.Unspecified,
    primaryBackgroundGrouped = Color.Unspecified,
    secondaryBackgroundGrouped = Color.Unspecified,
    tertiaryBackgroundGrouped = Color.Unspecified,
    primaryBackgroundDark = Color.Unspecified,
    secondaryBackgroundDark = Color.Unspecified,
    tertiaryBackgroundDark = Color.Unspecified,
    primaryGroupedBackgroundDark = Color.Unspecified,
    secondaryGroupedBackgroundDark = Color.Unspecified,
    tertiaryGroupedBackgroundDark = Color.Unspecified,
    primaryLabel = Color.Unspecified,
    secondaryLabel = Color.Unspecified,
    tertiaryLabel = Color.Unspecified,
    primaryLabelDark = Color.Unspecified,
    secondaryLabelDark = Color.Unspecified,
    tertiaryLabelDark = Color.Unspecified,
    separator = Color.Unspecified,
    opaqueSeparator = Color.Unspecified,
    sysError = Color.Unspecified,
    sysWarning = Color.Unspecified,
    sysSuccess = Color.Unspecified,
    sysWhite = Color.Unspecified,
    sysBlack = Color.Unspecified,
    sysTransparent = Color.Unspecified,
  )
}

// region G
val LocalGAppTypography = staticCompositionLocalOf {
  GAppTypography(
    display1 = TextStyle.Default,
    display1Emphasized = TextStyle.Default,
    display2 = TextStyle.Default,
    display2Emphasized = TextStyle.Default,
    display3 = TextStyle.Default,
    display3Emphasized = TextStyle.Default,
    headline = TextStyle.Default,
    headlineEmphasized = TextStyle.Default,
    title1 = TextStyle.Default,
    title1Emphasized = TextStyle.Default,
    title2 = TextStyle.Default,
    title2Emphasized = TextStyle.Default,
    title3 = TextStyle.Default,
    title3Emphasized = TextStyle.Default,
    bodyLarge = TextStyle.Default,
    bodyLargeEmphasized = TextStyle.Default,
    bodyMedium = TextStyle.Default,
    bodyMediumEmphasized = TextStyle.Default,
    bodySmall = TextStyle.Default,
    bodySmallEmphasized = TextStyle.Default,
    labelLarge = TextStyle.Default,
    labelLargeEmphasized = TextStyle.Default,
    labelMedium = TextStyle.Default,
    labelMediumEmphasized = TextStyle.Default,
    labelSmall = TextStyle.Default,
    labelSmallEmphasized = TextStyle.Default,
  )
}
val LocalGShapes = staticCompositionLocalOf {
  GShapesImmut(
    maxLarge = RoundedCornerShape(ZeroCornerSize),
    extraLarge = RoundedCornerShape(ZeroCornerSize),
    large = RoundedCornerShape(ZeroCornerSize),
    medium = RoundedCornerShape(ZeroCornerSize),
    small = RoundedCornerShape(ZeroCornerSize),
    extraSmall = RoundedCornerShape(ZeroCornerSize),
    circle = RoundedCornerShape(ZeroCornerSize),
    rectangle = RoundedCornerShape(ZeroCornerSize),
  )
}
val LocalGColors = staticCompositionLocalOf {
  GMedTrackerColors(
    gPrimary = Color.Unspecified,
    gPrimarybackground = Color.Unspecified,
    gSecondarybackground = Color.Unspecified,
    gSurface = Color.Unspecified,
    gPrimarytext = Color.Unspecified,
    gSecondarytext = Color.Unspecified,
    gDisabledtext = Color.Unspecified,
    gSeparator = Color.Unspecified,
    gOpaqueSeparator = Color.Unspecified,
    systemBlack = Color.Unspecified,
    systemWhite = Color.Unspecified,
    systemRed = Color.Unspecified,
    systemGreen = Color.Unspecified,
    systemYellow = Color.Unspecified,
    systemTransparent = Color.Unspecified,
  )
}

// endregion
@Composable
fun MedTrackerTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val medTrackerColors =
    MedTrackerColors(
      primary400,
      primary500,
      primary600,
      primaryLight,
      primaryTinted,
      secondary400,
      secondary500,
      secondary600,
      secondaryLight,
      secondaryTinted,
      primaryFill,
      secondaryFill,
      tertiaryFill,
      primaryBackground,
      secondaryBackground,
      tertiaryBackground,
      primaryBackgroundGrouped,
      secondaryBackgroundGrouped,
      tertiaryBackgroundGrouped,
      primaryBackgroundDark,
      secondaryBackgroundDark,
      tertiaryBackgroundDark,
      primaryGroupedBackgroundDark,
      secondaryGroupedBackgroundDark,
      tertiaryGroupedBackgroundDark,
      primaryLabel,
      secondaryLabel,
      tertiaryLabel,
      primaryLabelDark,
      secondaryLabelDark,
      tertiaryLabelDark,
      separator,
      opaqueSeparator,
      sysError,
      sysWarning,
      sysSuccess,
      sysWhite,
      sysBlack,
      sysTransparent,
    )
  // val medTrackerTypography = AppTypography
  val gAppTypography = GTypography
  val gAppShapes = GShapes
  
  
  CompositionLocalProvider(
    LocalMedTrackerColors provides medTrackerColors,
    LocalGAppTypography provides gAppTypography,
    LocalGShapes provides gAppShapes,
    content = content,
  )
}

object MedTrackerTheme {
  
  val colors: MedTrackerColors
    @Composable get() = LocalMedTrackerColors.current
  val typography: GAppTypography
    @Composable get() = LocalGAppTypography.current
  val shapes: GShapesImmut
    @Composable get() = LocalGShapes.current
}

val ButtonShape = RoundedCornerShape(percent = 50)

// New theme
@Composable
fun GAppTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val gMedTrackerColors =
    GMedTrackerColors(
      gPrimary,
      gPrimarybackground,
      gSecondarybackground,
      gSurface,
      gPrimarytext,
      gSecondarytext,
      gDisabledtext,
      gSeparator,
      gOpaqueSeparator,
      gblack,
      gwhite,
      gred,
      ggreen,
      gyellow,
      gtransparent,
    )
  val gAppTypography = GTypography
  val gAppShapes = GShapes
  
  
  CompositionLocalProvider(
    LocalGColors provides gMedTrackerColors,
    LocalGAppTypography provides gAppTypography,
    LocalGShapes provides gAppShapes,
    content = content,
  )
}

object GAppTheme {
  
  val colors: GMedTrackerColors
    @Composable get() = LocalGColors.current
  val typography: GAppTypography
    @Composable get() = LocalGAppTypography.current
  val shapes: GShapesImmut
    @Composable get() = LocalGShapes.current
  
  
}