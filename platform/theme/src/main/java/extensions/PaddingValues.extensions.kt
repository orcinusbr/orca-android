package com.jeanbarrossilva.mastodonte.platform.theme.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection

/** Alias for calling [PaddingValues.calculateBottomPadding]. **/
internal val PaddingValues.bottom
    get() = calculateBottomPadding()

/** End padding calculated through the [LocalLayoutDirection]. **/
internal val PaddingValues.end
    @Composable get() = calculateEndPadding(LocalLayoutDirection.current)

/** Start padding calculated through the [LocalLayoutDirection]. **/
internal val PaddingValues.start
    @Composable get() = calculateStartPadding(LocalLayoutDirection.current)

/** Alias for calling [PaddingValues.calculateTopPadding]. **/
internal val PaddingValues.top
    get() = calculateTopPadding()

/**
 * Adds the [PaddingValues].
 *
 * @param other [PaddingValues] to add to the receiver one.
 **/
@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    return PaddingValues(
        start + other.start,
        calculateTopPadding() + other.calculateTopPadding(),
        end + other.end,
        calculateBottomPadding() + other.calculateBottomPadding()
    )
}
