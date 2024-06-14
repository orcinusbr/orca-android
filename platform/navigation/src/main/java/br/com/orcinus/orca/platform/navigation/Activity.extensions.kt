/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.platform.navigation

import android.app.Activity
import android.view.View
import android.widget.FrameLayout

/** [FrameLayout] containing the [View] set as this [Activity]'s content. */
val Activity.content
  get() = requireViewById<FrameLayout>(android.R.id.content)

/**
 * Gets the extra put into this [Activity]'s [intent][Activity.getIntent] with the given [key]
 * lazily.
 *
 * @param key Key to which the extra is associated.
 * @throws ClassCastException If the extra is present but isn't a [T].
 */
fun <T> Activity.extra(key: String): Lazy<T> {
  return lazy {
    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    intent?.extras?.get(key) as T
  }
}
