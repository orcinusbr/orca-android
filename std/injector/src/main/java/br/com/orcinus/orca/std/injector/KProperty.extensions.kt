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

package br.com.orcinus.orca.std.injector

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.instanceParameter

/**
 * Returns whether the [predicate] is satisfied by the receiver [KProperty] or any of the ones which
 * are overridden by it in the entire inheritance tree of the [KClass] that declares it.
 *
 * @param delimiter [KType] up until which matches of this [KProperty] are to be searched for.
 * @param predicate Condition to be met by one of the properties in order for this method to return
 *   `true`.
 */
internal fun KProperty<*>.any(delimiter: KType, predicate: KProperty<*>.() -> Boolean): Boolean {
  return instanceParameter
    ?.type
    ?.classifier
    ?.let { it as KClass<*>? }
    ?.getPropertiesOverriddenBy(this, delimiter)
    ?.plus(this)
    ?.any(predicate)
    ?: false
}
