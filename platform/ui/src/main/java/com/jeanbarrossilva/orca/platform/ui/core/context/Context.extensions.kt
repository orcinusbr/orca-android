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

package com.jeanbarrossilva.orca.platform.ui.core.context

import android.content.Context
import com.jeanbarrossilva.orca.platform.ui.core.Intent

/**
 * Opens the share sheet so that the [text] can be shared.
 *
 * @param text Text to be shared.
 */
fun Context.share(text: String) {
  val intent = Intent(text)
  startActivity(intent)
}
