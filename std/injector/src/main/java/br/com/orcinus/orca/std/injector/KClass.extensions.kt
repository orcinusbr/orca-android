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
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties

/**
 * Returns the properties that are overridden by the given one throughout the entire inheritance
 * tree until the [delimiter] is reached (exclusively).
 *
 * @param property [KProperty] whose overridden super-properties will be returned.
 * @param delimiter [KType] up until which matches of the [property] are to be searched for.
 */
internal fun KClass<*>.getPropertiesOverriddenBy(
  property: KProperty<*>,
  delimiter: KType
): List<KProperty<*>> {
  return getPropertiesOverriddenBy(property, delimiter, isInclusive = false)
}

/**
 * Returns the properties that are overridden by the given one throughout the entire inheritance
 * tree until the [delimiter] is reached.
 *
 * @param property [KProperty] whose overridden super-properties will be returned.
 * @param delimiter [KType] up until which the [property] is to be searched for.
 * @param isInclusive Whether the receiver [KClass]' [property] override should be included in the
 *   resulting [List].
 */
private fun KClass<*>.getPropertiesOverriddenBy(
  property: KProperty<*>,
  delimiter: KType,
  isInclusive: Boolean
): List<KProperty<*>> {
  return supertypes
    .filter { it == this && isInclusive || it.isSubtypeOf(delimiter) }
    .mapNotNull { it.classifier as KClass<*>? }
    .mapNotNull { superclass ->
      superclass.memberProperties
        .find { superProperty -> superProperty.name == property.name }
        ?.let { overriddenProperty ->
          superclass
            .getPropertiesOverriddenBy(overriddenProperty, delimiter, isInclusive = true)
            .plus(overriddenProperty)
        }
    }
    .fold(emptyList()) { accumulator, overriddenProperties -> accumulator + overriddenProperties }
}
