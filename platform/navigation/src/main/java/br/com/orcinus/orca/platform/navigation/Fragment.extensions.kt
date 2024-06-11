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

package br.com.orcinus.orca.platform.navigation

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView

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
 * [Navigator] through which navigation can be performed.
 *
 * **NOTE**: Because the [FragmentContainerView] that this [Fragment]'s [FragmentActivity] holds
 * needs to have an ID for the [Navigator] to work properly, one is automatically generated and
 * assigned to it if it doesn't already have one.
 *
 * @throws IllegalStateException If a [FragmentContainerView] isn't found.
 */
val Fragment.navigator
  get() = Navigator.Pool.get(this)

/** [Navigator] by which navigation to this [Fragment] has potentially been performed. */
val Fragment.parentNavigator
  get() =
    with(parentFragmentManager) { fragments[fragments.lastIndex - backStackEntryCount.dec()] }
      .navigator

/**
 * Gets the argument put either into this [Fragment]'s arguments or its [Activity]'s [Intent]'s
 * extras with the given [key] lazily.
 *
 * @param key Key to which the argument is associated.
 * @throws ClassCastException If the argument is present but isn't a [T].
 */
fun <T> Fragment.argument(key: String): Lazy<T> {
  return lazy {
    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    arguments?.get(key) as? T ?: requireActivity().intent?.extras?.get(key) as T
  }
}
