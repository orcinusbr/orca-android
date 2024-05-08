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

import androidx.fragment.app.Fragment
import br.com.orcinus.orca.ext.reflection.access
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties

/**
 * Changes this [Fragment]'s tag to the given one.
 *
 * @param tag [String] by which this [Fragment] will be tagged.
 * @throws NoSuchFieldException If the property responsible for holding the tag of the resulting
 *   [Fragment] isn't found (since reflection is performed to access and set its value to that of
 *   the given [tag] because it is private as of `androidx.fragment` 1.7.0).
 */
@PublishedApi
@Throws(NoSuchFieldException::class)
internal fun Fragment.setTag(tag: String) {
  Fragment::class
    .declaredMemberProperties
    .filterIsInstance<KMutableProperty1<Fragment, String>>()
    .singleOrNull { it.name == "mTag" }
    ?.access { set(this@setTag, tag) }
    ?: throw NoSuchFieldException("Fragment.mTag")
}
