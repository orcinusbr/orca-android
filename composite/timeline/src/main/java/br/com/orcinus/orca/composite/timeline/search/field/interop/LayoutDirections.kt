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
import androidx.compose.ui.unit.LayoutDirection

/**
 * Obtains a Compose-based direction of the layout from a [View].
 *
 * @param view [View] from which the Compose layout direction will be provided. Any integer other
 *   than [View.LAYOUT_DIRECTION_LTR] or [View.LAYOUT_DIRECTION_RTL] having been set to this
 *   [View]'s layout direction results in this method returning `null`, given that other values are
 *   considered invalid.
 * @see View.getLayoutDirection
 * @see View.setLayoutDirection
 */
internal fun layoutDirectionOf(view: View): LayoutDirection? =
  when (view.layoutDirection) {
    View.LAYOUT_DIRECTION_LTR -> LayoutDirection.Ltr
    View.LAYOUT_DIRECTION_RTL -> LayoutDirection.Rtl
    else -> null
  }
