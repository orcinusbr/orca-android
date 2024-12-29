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

package br.com.orcinus.orca.composite.timeline.search.field.interop

import android.view.View
import androidx.compose.foundation.layout.PaddingValues

/**
 * Applies these [PaddingValues] to this [View].
 *
 * @param padding Padding to apply to each edge of this [View].
 */
internal fun View.setPadding(padding: PaddingValues) {
  layoutDirectionOf(this)?.let {
    val density = context?.resources?.displayMetrics?.density ?: return
    setPadding(
      (padding.calculateLeftPadding(it).value * density).toInt(),
      (padding.calculateTopPadding().value * density).toInt(),
      (padding.calculateRightPadding(it).value * density).toInt(),
      (padding.calculateBottomPadding().value * density).toInt()
    )
  }
}
