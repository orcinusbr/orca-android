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

package com.jeanbarrossilva.orca.platform.autos.iconography

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource

/** [ImageVector] version of this icon ID. */
val String.asImageVector
  @Composable
  @Suppress("DiscouragedApi")
  get() =
    with("icon_${replace('-', '_')}") id@{
      with(LocalContext.current) {
        try {
          ImageVector.vectorResource(
            theme,
            resources,
            resources.getIdentifier(this@id, "drawable", packageName)
          )
        } catch (_: Resources.NotFoundException) {
          throw Resources.NotFoundException(this@id)
        }
      }
    }
