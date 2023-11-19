@file:JvmName("BordersExtensions")

package com.jeanbarrossilva.orca.platform.theme.autos.borders

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.jeanbarrossilva.orca.autos.borders.Borders
import com.jeanbarrossilva.orca.platform.theme.autos.noLocalProvidedFor

/** [CompositionLocal] that provides [Borders]. */
internal val LocalBorders = compositionLocalOf<Borders> { noLocalProvidedFor("LocalBorders") }

/** Whether components should have the [BorderStroke]s applied to them. */
internal val Borders.Companion.areApplicable
  @Composable get() = areApplicable(LocalContext.current)

/**
 * Checks whether components should have the [BorderStroke]s applied to them.
 *
 * @param context [Context] through which the theme in which the system currently is will be
 *   obtained.
 */
@JvmName("areApplicable")
internal fun Borders.Companion.areApplicable(context: Context): Boolean {
  return with(context.resources.configuration) {
    uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO
  }
}
