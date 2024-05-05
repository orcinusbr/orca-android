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

package br.com.orcinus.orca.platform.navigation.destination

import androidx.annotation.Discouraged
import androidx.fragment.app.Fragment
import br.com.orcinus.orca.ext.reflection.access
import br.com.orcinus.orca.platform.navigation.Navigator
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties

/**
 * Provides a tagged [Fragment].
 *
 * Exists mainly for compatibility reasons, since the [Fragment] to which navigation will be
 * performed by a [Navigator] should currently be provided eagerly instead of being lazily
 * instantiated.
 *
 * @param T [Fragment] to navigate to.
 * @see to
 * @see Navigator.navigate
 * @see Navigator.navigateToDestinationFragment
 */
class FragmentProvisioningScope<T : Fragment> internal constructor() {
  /**
   * Provides the [Fragment] alongside the [tag] to be defined as its own, acting as what was
   * previously conceptualized as being the route.
   *
   * @param tag [String] with which the resulting [Fragment] will be tagged when added to a
   *   container.
   * @param fragment Lazily provides the [Fragment] to navigate to.
   * @throws NoSuchFieldException If the property responsible for holding the tag of the resulting
   *   [Fragment] isn't found (since reflection is performed to access and set its value to that of
   *   the given [tag] because it is private as of `androidx.fragment` 1.7.0).
   */
  @Discouraged(
    "Providing a non-`DestinationFragment` is highly discouraged, since it is a requirement of " +
      "`View`-`Navigator` integration APIs for it to be uniquely identified. When navigating, " +
      "prefer calling `Navigator.navigateToDestinationFragment` instead."
  )
  @Throws(NoSuchFieldException::class)
  fun to(tag: String, fragment: () -> T): T {
    return fragment().apply fragment@{
      Fragment::class
        .declaredMemberProperties
        .filterIsInstance<KMutableProperty1<Fragment, String>>()
        .singleOrNull { it.name == "mTag" }
        ?.access { set(this@fragment, tag) }
        ?: throw NoSuchFieldException("Fragment.mTag")
    }
  }
}
