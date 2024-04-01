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

package com.jeanbarrossilva.orca.platform.navigation.test.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

/**
 * Creates a [FragmentFactory] that automatically checks whether the name of the class of which an
 * instance has been requested to be created coincides with that of the specified [Fragment].
 *
 * @param T [Fragment] to be instantiated.
 * @param instantiation Provides an instance of the [Fragment].
 */
@PublishedApi
internal inline fun <reified T : Fragment> fragmentFactoryOf(
  crossinline instantiation: () -> T
): FragmentFactory {
  return object : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
      return if (className == T::class.qualifiedName) {
        instantiation()
      } else {
        super.instantiate(classLoader, className)
      }
    }
  }
}
