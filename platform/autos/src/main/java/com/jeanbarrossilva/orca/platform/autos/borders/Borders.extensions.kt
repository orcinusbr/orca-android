/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

@file:JvmName("BordersExtensions")

package com.jeanbarrossilva.orca.platform.autos.borders

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import br.com.orcinus.orca.autos.borders.Borders
import com.jeanbarrossilva.orca.platform.autos.noLocalProvidedFor

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
