/*
 * Copyright © 2025 Orcinus
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

package br.com.orcinus.orca.platform.autos.forms

import android.content.Context
import android.graphics.drawable.Drawable
import br.com.orcinus.orca.autos.forms.Form

/**
 * Creates another [Drawable] from this one, clipped by the given [form].
 *
 * @param context [Context] for converting density-independent pixels into absolute ones.
 * @param form [Form] by which this [Drawable] will be clipped.
 */
fun Drawable.clip(context: Context, form: Form.PerCorner): Drawable =
  FormDrawable(context, form, this)
