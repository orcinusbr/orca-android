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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination.type

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

/**
 * [KTypeCreator] that creates a [KType] for [T] through [KClass.createType], without specifying any
 * type arguments or [Annotation]s. Useful for simpler types that do not require these details to be
 * set.
 *
 * **NOTE**: A [ComplexTypeException] will be thrown if [T] is a complex type and a [KType] for it
 * is requested to be created, denoting that it should be done so manually instead of delegating the
 * creation to a [DefaultKTypeCreator].
 *
 * @param T Object for which the [KType] to be created is.
 */
@JvmInline
private value class DefaultKTypeCreator<T : Any>(override val kClass: KClass<T>) : KTypeCreator<T> {
  @Throws(ComplexTypeException::class)
  override fun create(): KType {
    return try {
      kClass.createType()
    } catch (_: IllegalArgumentException) {
      throw ComplexTypeException(kClass)
    }
  }
}

/**
 * [IllegalArgumentException] thrown if a [KType] is tried to be created for a type that cannot be
 * handled by a [DefaultKTypeCreator].
 *
 * @param kClass [KClass] for which a [KType] couldn't be created.
 */
internal class ComplexTypeException(kClass: KClass<*>) :
  IllegalArgumentException("Cannot create a type from a $kClass with a default creator.")

/**
 * Creates a default [KTypeCreator] that creates a [KType] for [T] through [KClass.createType].
 *
 * **NOTE**: A [ComplexTypeException] will be thrown if [T] is a complex type and a [KType] for it
 * is requested to be created, denoting that it should be done so manually instead of delegating the
 * creation to a default [KTypeCreator].
 *
 * @param T Object for which the [KType] to be created is.
 */
internal inline fun <reified T : Any> kTypeCreatorOf(): KTypeCreator<T> {
  return kTypeCreatorOf(T::class)
}

/**
 * Creates a default [KTypeCreator] that creates a [KType] for [T] through [KClass.createType].
 *
 * **NOTE**: A [ComplexTypeException] will be thrown if [T] is a complex type and a [KType] for it
 * is requested to be created, denoting that it should be done so manually instead of delegating the
 * creation to a default [KTypeCreator].
 *
 * @param T Object for which the [KType] to be created is.
 * @param kClass [KClass] from which the [KType] can be created.
 */
@PublishedApi
internal fun <T : Any> kTypeCreatorOf(kClass: KClass<T>): KTypeCreator<T> {
  return DefaultKTypeCreator(kClass)
}
