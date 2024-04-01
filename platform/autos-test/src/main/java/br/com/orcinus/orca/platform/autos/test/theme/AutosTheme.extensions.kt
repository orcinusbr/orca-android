/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.platform.autos.test.theme

import androidx.compose.runtime.Composable
import br.com.orcinus.orca.platform.autos.theme.AutosTheme

/**
 * [IllegalStateException] thrown if some content is required to be themed with [AutosTheme] but
 * isn't.
 *
 * @see AutosTheme.require
 */
class MissingThemingException internal constructor() :
  IllegalStateException("AutosTheme was required but content wasn't themed.")

/**
 * Requires the content to be themed.
 *
 * @throws MissingThemingException If [AutosTheme] isn't applied.
 */
@Composable
@Suppress("ComposableNaming")
@Throws(IllegalStateException::class)
fun AutosTheme.require() {
  runCatching { spacings }
    .onFailure {
      if (it is IllegalStateException) {
        throw MissingThemingException()
      }
    }
}
