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

package com.jeanbarrossilva.orca.platform.ui.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.junit.Assert.assertNotNull

/**
 * Asserts that a [Fragment] tagged as [tag] is the current one within the given [activity].
 *
 * @param activity [FragmentActivity] in which the [Fragment] is supposed to be.
 * @param tag Tag of the [Fragment] that's supposed to be the current one.
 */
fun assertIsAtFragment(activity: FragmentActivity, tag: String) {
  assertNotNull(
    "Fragment tagged as \"$tag\" not found.",
    activity.supportFragmentManager.findFragmentByTag(tag)
  )
}
