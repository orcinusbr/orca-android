package com.jeanbarrossilva.orca.platform.theme.extensions

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

/**
 * Applies the [fontFamily] to each [TextStyle].
 *
 * @param fontFamily [FontFamily] to be applied.
 **/
internal fun Typography.with(fontFamily: FontFamily): Typography {
    return copy(
        displayLarge.copy(fontFamily = fontFamily),
        displayMedium.copy(fontFamily = fontFamily),
        displaySmall.copy(fontFamily = fontFamily),
        headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall.copy(fontFamily = fontFamily),
        titleLarge.copy(fontFamily = fontFamily),
        titleMedium.copy(fontFamily = fontFamily),
        titleSmall.copy(fontFamily = fontFamily),
        bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium.copy(fontFamily = fontFamily),
        bodySmall.copy(fontFamily = fontFamily),
        labelLarge.copy(fontFamily = fontFamily),
        labelMedium.copy(fontFamily = fontFamily),
        labelSmall.copy(fontFamily = fontFamily)
    )
}
