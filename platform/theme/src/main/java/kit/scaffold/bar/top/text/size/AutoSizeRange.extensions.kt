package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.text.size

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

/**
 * Remembers an [AutoSizeRange], defaulting the [minimum][AutoSizeRange.min] size to
 * [OrcaTheme.typography]'s [Typography.labelSmall]'s.
 *
 * @param max Maximum, default size.
 **/
@Composable
fun rememberAutoSizeRange(max: TextUnit): AutoSizeRange {
    return rememberAutoSizeRange(min = OrcaTheme.typography.labelSmall.fontSize, max)
}

/**
 * Remembers an [AutoSizeRange].
 *
 * @param min Minimum size.
 * @param max Maximum, default size.
 **/
@Composable
fun rememberAutoSizeRange(min: TextUnit, max: TextUnit): AutoSizeRange {
    val density = LocalDensity.current
    return remember(density, min, max) { AutoSizeRange(density, min, max) }
}
