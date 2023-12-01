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

package com.jeanbarrossilva.orca.platform.ui.core

import android.app.Application
import androidx.fragment.app.Fragment

/**
 * [Application] to which this [Fragment] is attached.
 *
 * @throws IllegalStateException If it's not attached.
 */
val Fragment.application
  get() =
    activity?.application
      ?: throw IllegalStateException("Fragment $this not attached to an Application.")

/**
 * Gets the argument put with the given [key] lazily.
 *
 * @param key Key to which the argument is associated.
 * @throws ClassCastException If the argument is present but isn't a [T].
 */
fun <T> Fragment.argument(key: String): Lazy<T> {
  return lazy {
    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    requireArguments().get(key) as T
  }
}
