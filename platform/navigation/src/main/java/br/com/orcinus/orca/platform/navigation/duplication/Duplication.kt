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

package br.com.orcinus.orca.platform.navigation.duplication

import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

/** Indicates the approval or lack thereof of duplicate navigation. */
sealed class Duplication {
  /** Indicates that duplicate navigation is disallowed. */
  internal data object Disallowed : Duplication() {
    override fun canNavigate(
      currentFragmentClass: KClass<out Fragment>?,
      nextFragmentClass: KClass<out Fragment>
    ): Boolean {
      return currentFragmentClass == null || nextFragmentClass != currentFragmentClass
    }
  }

  /** Indicates that duplicate navigation is allowed. */
  internal data object Allowed : Duplication() {
    override fun canNavigate(
      currentFragmentClass: KClass<out Fragment>?,
      nextFragmentClass: KClass<out Fragment>
    ): Boolean {
      return true
    }
  }

  /**
   * Determines whether navigation can be performed.
   *
   * @param currentFragmentClass [KClass] of the current [Fragment].
   * @param nextFragmentClass [KClass] of the [Fragment] to which navigation has been requested.
   */
  internal abstract fun canNavigate(
    currentFragmentClass: KClass<out Fragment>?,
    nextFragmentClass: KClass<out Fragment>
  ): Boolean
}
