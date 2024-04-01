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

@file:JvmName("ViewExtensions")

package br.com.orcinus.orca.platform.ime

import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Compatibility version of this [View]'s root [WindowInsets].
 *
 * @see View.getRootWindowInsets
 */
internal val View.rootWindowInsetsCompat
  get() = rootWindowInsets?.let { WindowInsetsCompat.toWindowInsetsCompat(it, this) }

/**
 * Compatibility version of this [View]'s [WindowInsetsController].
 *
 * @see View.getWindowInsetsController
 */
internal val View.windowInsetsControllerCompat
  get() = context?.findActivity()?.window?.let { WindowCompat.getInsetsController(it, this) }
