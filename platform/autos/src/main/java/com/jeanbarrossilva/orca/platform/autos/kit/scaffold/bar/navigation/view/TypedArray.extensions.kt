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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.navigation.view

import android.content.Context
import android.content.res.TypedArray
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.StyleableRes
import androidx.appcompat.view.menu.MenuBuilder

/**
 * Inflates the [Menu] that has been specified at the given [index] (or `null` if none has been).
 *
 * @param context [Context] from which a [MenuBuilder] will be created.
 * @param menu [Menu] to which the [MenuItem]s will be added.
 * @param index Index at which the [Menu] is expected to be.
 */
internal fun TypedArray.inflateMenu(context: Context, menu: Menu, @StyleableRes index: Int) {
  getResourceId(index, 0).takeUnless { it == 0 }?.let { MenuInflater(context).inflate(it, menu) }
}
