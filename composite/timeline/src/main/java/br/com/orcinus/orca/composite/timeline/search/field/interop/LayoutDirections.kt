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

import android.content.Context
import android.util.LayoutDirection as AndroidLayoutDirection
import android.view.View
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.text.layoutDirection
import java.util.Locale

/**
 * Obtains a Compose-based enum entry of the direction of the layout from a [View].
 *
 * @param view [View] from which the Compose layout direction will be provided. Any integer other
 *   than [View.LAYOUT_DIRECTION_LTR] or [View.LAYOUT_DIRECTION_RTL] having been set to this
 *   [View]'s, or non-_LTR_, -_RTL_ or -_locale_ ones to its [Context]'s layout direction results in
 *   this method returning `null`, given that other values are considered invalid.
 */
internal fun layoutDirectionOf(view: View): LayoutDirection? =
  when (view.layoutDirection) {
    View.LAYOUT_DIRECTION_LTR -> LayoutDirection.Ltr
    View.LAYOUT_DIRECTION_RTL -> LayoutDirection.Rtl
    else -> view.context?.resources?.configuration?.layoutDirection?.let(::layoutDirectionOf)
  }

/**
 * Obtains a Compose-based enum entry of the direction of the layout from a [View]-based constant.
 *
 * @param value Constant from which the Compose layout direction will be provided. Any integer other
 *   than _LTR_, _RTL_ or _locale_ results in this method returning `null`, given that a layout
 *   direction cannot be inherited in the case of an _inherit_ and other values are considered
 *   invalid.
 */
private fun layoutDirectionOf(value: Int): LayoutDirection? =
  when (value) {
    AndroidLayoutDirection.LOCALE -> layoutDirectionOf(Locale.getDefault().layoutDirection)
    AndroidLayoutDirection.LTR -> LayoutDirection.Ltr
    AndroidLayoutDirection.RTL -> LayoutDirection.Rtl
    else -> null
  }
