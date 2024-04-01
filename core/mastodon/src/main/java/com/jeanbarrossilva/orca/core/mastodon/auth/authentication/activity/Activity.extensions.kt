/*
 * Copyright © 2023–2024 Orcinus
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

package com.jeanbarrossilva.orca.core.mastodon.auth.authentication.activity

import android.app.Activity
import android.content.Intent

/**
 * Gets this [Activity]'s [Intent][Intent] extra put with the given [key] lazily.
 *
 * @param key Key to which the extra is associated.
 * @throws ClassCastException If the extra is present but isn't a [T].
 */
internal inline fun <reified T> Activity.extra(key: String): Lazy<T> {
  return lazy {
    @Suppress("DEPRECATION")
    intent?.extras?.get(key) as T
  }
}
