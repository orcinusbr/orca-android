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

package br.com.orcinus.orca.platform.navigation

import androidx.fragment.app.Fragment
import java.io.Serializable

/**
 * Stack to which [Fragment]s are added.
 *
 * @property name Name for identifying the origin from which the [Fragment]s are added.
 */
@JvmInline
value class BackStack private constructor(internal val name: String) : Serializable {
  companion object {
    /** [String] with which a [BackStack] is obtained as a [Fragment]'s argument. */
    const val KEY = "back-stack"

    /**
     * Creates a [BackStack].
     *
     * @param name Name for identifying the origin from which the [Fragment]s are added.
     */
    fun named(name: String): BackStack {
      return BackStack(name)
    }

    /**
     * Lazily obtains the [BackStack] passed into the [fragment] as an argument.
     *
     * @param fragment [Fragment] from which a [BackStack] will be obtained.
     */
    fun from(fragment: Fragment): Lazy<BackStack> {
      return fragment.argument(KEY)
    }
  }
}
