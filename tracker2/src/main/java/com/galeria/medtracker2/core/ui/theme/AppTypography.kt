package com.galeria.medtracker2.core.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


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
            ),
        display2Emphasized =
            TextStyle(
                fontSize = 45.sp,
                fontWeight = FontWeight(700),
                fontFamily = FontFamily.Default,
                lineHeight = 52.sp,
            ),
        display3 =
            TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight(400),
                fontFamily = FontFamily.Default,
                lineHeight = 44.sp,
            ),
        display3Emphasized = TextStyle(
            fontSize = 36.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            lineHeight = 44.sp,
        ),
        headline = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
            lineHeight = 40.sp,
        ),
        headlineEmphasized = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            lineHeight = 40.sp,
        ),
        title1 = TextStyle(
            fontSize = 24.sp, // Увеличено с 22.sp
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
            lineHeight = 32.sp, // Скорректировано под новый размер
        ),
        title1Emphasized = TextStyle(
            fontSize = 24.sp, // Увеличено с 22.sp
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
            lineHeight = 32.sp, // Скорректировано под новый размер
        ),
        title2 = TextStyle(
            fontSize = 20.sp, // Увеличено с 16.sp
            lineHeight = 28.sp, // Скорректировано
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
        ),
        title2Emphasized = TextStyle(
            fontSize = 20.sp, // Увеличено с 16.sp
            lineHeight = 28.sp, // Скорректировано
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
        ),
        title3 = TextStyle(
            fontSize = 18.sp, // Увеличено с 14.sp
            lineHeight = 24.sp, // Скорректировано
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
        ),
        title3Emphasized = TextStyle(
            fontSize = 18.sp, // Увеличено с 14.sp
            lineHeight = 24.sp, // Скорректировано
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
        ),
        bodyLarge = TextStyle(
            fontSize = 18.sp, // Увеличено с 16.sp
            lineHeight = 26.sp, // Скорректировано
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
        ),
        bodyLargeEmphasized = TextStyle(
            fontSize = 18.sp, // Увеличено с 16.sp
            lineHeight = 26.sp, // Скорректировано
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
        ),
        bodyMedium = TextStyle(
            fontSize = 16.sp, // Увеличено с 14.sp
            lineHeight = 22.sp, // Скорректировано
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
        ),
        bodyMediumEmphasized = TextStyle(
            fontSize = 16.sp, // Увеличено с 14.sp
            lineHeight = 22.sp, // Скорректировано
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
        ),
        bodySmall = TextStyle(
            fontSize = 14.sp, // Увеличено с 12.sp
            lineHeight = 20.sp, // Скорректировано
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
        ),
        bodySmallEmphasized = TextStyle(
            fontSize = 14.sp, // Увеличено с 12.sp
            lineHeight = 20.sp, // Скорректировано
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
        ),
        labelLarge = TextStyle(
            // button
            fontSize = 16.sp, // Увеличено с 14.sp
            lineHeight = 22.sp, // Скорректировано
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
        ),
        labelLargeEmphasized = TextStyle(
            // button
            fontSize = 16.sp, // Увеличено с 14.sp
            lineHeight = 22.sp, // Скорректировано
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
        ),
        labelMedium = TextStyle(
            fontSize = 14.sp, // Увеличено с 12.sp
            lineHeight = 18.sp, // Скорректировано
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
        ),
        labelMediumEmphasized = TextStyle(
            fontSize = 14.sp, // Увеличено с 12.sp
            lineHeight = 18.sp, // Скорректировано
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
        ),
        labelSmall = TextStyle(
            fontSize = 12.sp, // Увеличено с 11.sp
            lineHeight = 16.sp,
            fontWeight = FontWeight(400),
            fontFamily = FontFamily.Default,
        ),
        labelSmallEmphasized = TextStyle(
            fontSize = 12.sp, // Увеличено с 11.sp
            lineHeight = 16.sp,
            fontWeight = FontWeight(700),
            fontFamily = FontFamily.Default,
        ),
    )