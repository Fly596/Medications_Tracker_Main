package com.galeria.medtracker2.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography =
    MedTrackerTypography(
        largeTitle = // display large
            TextStyle(
                fontSize = 57.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Default,
                lineHeight = 64.sp,
            ), // Used for titles and headings that need to make a strong visual impact.
        largeTitleEmphasized =
            TextStyle(
                fontSize = 57.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                lineHeight = 64.sp,
            ), // Used for titles and headings that need to make a strong visual impact.
        title1 =
            // Display Medium
            TextStyle(
                fontSize = 45.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Default,
                lineHeight = 52.sp,
                // color = Color.Black
            ),
        title1Emphasized =
            // Display Medium
            TextStyle(
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                lineHeight = 52.sp,
                // color = Color.Black
            ),
        title2 =
            TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Default,
                lineHeight = 44.sp,
                // color = Color.Black
            ),
        title2Emphasized =
            TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                lineHeight = 28.sp,
                // color = Color.Black
            ),
        title3 =
            TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Default,
                lineHeight = 25.sp,
                // color = Color.Black
            ),
        title3Emphasized =
            TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Default,
                lineHeight = 25.sp,
                // color = Color.Black
            ),
        headline =
            TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Default,
                lineHeight = 36.sp,
                // color = Color.Black
            ),
        headlineItalic =
            TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Default,
                lineHeight = 36.sp,
                // color = Color.Black
            ),
        body =
            TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Default,
                lineHeight = 22.sp,
                // color = Color.Black
            ),
        bodyEmphasized =
            TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Default,
                lineHeight = 22.sp,
                // color = Color.Black
            ),
        callout =
            TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Default,
                lineHeight = 21.sp,
                // color = Color.Black
            ),
        calloutEmphasized =
            TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Default,
                lineHeight = 21.sp,
                // color = Color.Black
            ),
        subhead =
            TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Default,
                lineHeight = 20.sp,
                // color = Color.Black
            ),
        subheadEmphasized =
            TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Default,
                lineHeight = 20.sp,
                // color = Color.Black
            ),
        footnote =
            TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Default,
                lineHeight = 18.sp,
                // color = Color.Black
            ),
        footnoteEmphasized =
            TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Default,
                lineHeight = 18.sp,
                // color = Color.Black
            ),
        caption1 =
            TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Default,
                lineHeight = 16.sp,
                // color = Color.Black
            ),
        caption1Emphasized =
            TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Default,
                lineHeight = 16.sp,
                // color = Color.Black
            ),
        caption2 =
            TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Default,
                lineHeight = 13.sp,
                // color = Color.Black
            ),
        caption2Emphasized =
            TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Default,
                lineHeight = 13.sp,
                // color = Color.Black
            ),
    )
val GTypography =
    GAppTypography(
        display1 =
            TextStyle(
                fontSize = 57.sp,
                fontWeight = FontWeight(400),
                fontFamily = FontFamily.Default,
                lineHeight = 64.sp,
            ), // Used for titles and headings that need to make a strong visual impact.
        display1Emphasized =
            TextStyle(
                fontSize = 57.sp,
                fontWeight = FontWeight(700),
                fontFamily = FontFamily.Default,
                lineHeight = 64.sp,
            ), // Used for titles and headings that need to make a strong visual impact.
        display2 =
            TextStyle(
                fontSize = 45.sp,
                fontWeight = FontWeight(400),
                fontFamily = FontFamily.Default,
                lineHeight = 52.sp,
                // color = Color.Black
            ),
        display2Emphasized =
            TextStyle(
                fontSize = 45.sp,
                fontWeight = FontWeight(700),
                fontFamily = FontFamily.Default,
                lineHeight = 52.sp,
                // color = Color.Black
            ),
        display3 =
            TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight(400),
                fontFamily = FontFamily.Default,
                lineHeight = 44.sp,
                // color = Color.Black
            ),
        display3Emphasized = TextStyle(
            fontSize = 36.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            lineHeight = 44.sp,
            // color = Color.Black
        ),
        headline = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
            lineHeight = 40.sp,
            // color = Color.Black
        ),
        headlineEmphasized = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            lineHeight = 40.sp,
            // color = Color.Black
        ),
        title1 = TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
            lineHeight = 28.sp,
            // color = Color.Black
        ),
        title1Emphasized = TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            lineHeight = 28.sp,
            // color = Color.Black
        ),
        title2 = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        title2Emphasized = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        title3 = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        title3Emphasized = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        bodyLarge = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        bodyLargeEmphasized = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        bodyMedium = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        bodyMediumEmphasized = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        bodySmall = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        bodySmallEmphasized = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        labelLarge = TextStyle(
            // button
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        labelLargeEmphasized = TextStyle(
            // button
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        labelMedium = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        labelMediumEmphasized = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        labelSmall = TextStyle(
            fontSize = 11.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
        labelSmallEmphasized = TextStyle(
            fontSize = 11.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            // color = Color.Black
        ),
    )
