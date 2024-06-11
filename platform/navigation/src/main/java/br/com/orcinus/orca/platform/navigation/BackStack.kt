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
import androidx.fragment.app.FragmentTransaction
import java.util.Objects

/** Stack to which [Fragment]s are added. */
internal sealed class BackStack {
  /** Prepares for the addition of [Fragment]s onto a `null` back stack. */
  data object None : BackStack() {
    override fun add(transaction: FragmentTransaction): FragmentTransaction {
      return transaction.addToBackStack(null)
    }
  }

  /**
   * [BackStack] whose [Fragment]s are added to a back stack linked to a given origin one.
   *
   * @property fragment [Fragment] from which those to be added originate.
   */
  class Contextual(private val fragment: Fragment) : BackStack() {
    override fun add(transaction: FragmentTransaction): FragmentTransaction {
      return transaction.addToBackStack("$fragment's back stack")
    }

    override fun equals(other: Any?): Boolean {
      return other is Contextual && fragment == other.fragment
    }

    override fun hashCode(): Int {
      return Objects.hash(fragment)
    }
  }

  /**
   * Adds [Fragment]s onto this [BackStack].
   *
   * @param transaction [FragmentTransaction] from which back-stacking configuration can be
   *   performed.
   */
  internal abstract fun add(transaction: FragmentTransaction): FragmentTransaction
}
