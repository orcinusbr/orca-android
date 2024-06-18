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

package br.com.orcinus.orca.platform.navigation.transition

import androidx.fragment.app.FragmentTransaction

/**
 * Indicates how navigation from one destination to another will transition.
 *
 * @param value [FragmentTransaction]-related value that's equivalent to this [Transition].
 */
@JvmInline
value class Transition internal constructor(internal val value: Int) {
  init {
    require(
      value == FragmentTransaction.TRANSIT_NONE ||
        value == FragmentTransaction.TRANSIT_FRAGMENT_FADE ||
        value == FragmentTransaction.TRANSIT_FRAGMENT_OPEN ||
        value == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE
    ) {
      "Unknown value: $value."
    }
  }
}
