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

package br.com.orcinus.orca.platform.navigation.test

import androidx.annotation.Discouraged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

/**
 * Asserts that the current [Fragment] within the [FragmentActivity] matches the given one.
 *
 * @param A [FragmentActivity] in which the [Fragment] is.
 * @param F [Fragment] that it is expected to be at.
 */
inline fun <A : FragmentActivity, reified F : Fragment> Assert<A>.isAt(): Assert<A> {
  given {
    val fragment = it.supportFragmentManager.fragments.lastOrNull()
    if (fragment == null) {
      expected("to be at:${show(F::class)} but none has been added.")
    } else if (fragment::class != F::class) {
      expected("to be at:${show(F::class)} but was at:${show(fragment::class)}")
    }
  }
  return this
}

/**
 * Asserts that the tag of the current [Fragment] within the [FragmentActivity] matches the given
 * one.
 *
 * @param T [FragmentActivity] in which the [Fragment] is.
 * @param tag Tag of the [Fragment] that's expected to be the current one.
 * @see Fragment.getTag
 */
@Discouraged(
  "Assert against the current `Fragment` via this method's overload that receives its type as a " +
    "parameter type instead, as it tends to provide a more detailed message when the assertion " +
    "fails."
)
fun <T : FragmentActivity> Assert<T>.isAt(tag: String): Assert<T> {
  given { it.supportFragmentManager.findFragmentByTag(tag) ?: expected("to be at:${show(tag)}") }
  return this
}
