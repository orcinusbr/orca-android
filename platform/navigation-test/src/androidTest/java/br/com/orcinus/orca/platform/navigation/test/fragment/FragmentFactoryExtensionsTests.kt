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

package br.com.orcinus.orca.platform.navigation.test.fragment

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import assertk.assertThat
import assertk.assertions.isTrue
import kotlin.test.Test

internal class FragmentFactoryExtensionsTests {
  @Test
  fun fragmentFactoryCreatesSpecifiedFragment() {
    var hasBeenCreated = false
    fragmentFactoryOf {
        hasBeenCreated = true
        DialogFragment()
      }
      .instantiate(ClassLoader.getSystemClassLoader(), DialogFragment::class.java.name)
    assertThat(hasBeenCreated).isTrue()
  }

  @Test(expected = Fragment.InstantiationException::class)
  fun throwsWhenCreatingUnspecifiedFragment() {
    fragmentFactoryOf(::DialogFragment)
      .instantiate(ClassLoader.getSystemClassLoader(), ListFragment::class.java.name)
  }
}
