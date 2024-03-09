/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination

import io.ktor.client.statement.HttpResponse
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

/**
 * Creates a [KType] from a [KClass] of [T].
 *
 * Such behavior is necessary because of the generic nature of a [MastodonPostPaginator]: since its
 * type parameter (which is the DTO to be received from the API when pagination takes place) cannot
 * be reified (at least not as of Kotlin 1.9.21), the [HttpResponse] needs to know how to actually
 * convert the payload into the specified object. Tends to result in a somewhat manual process if
 * there are, for example, type arguments that need to be given; otherwise, a default [KTypeCreator]
 * can automatically do the work all by itself.
 *
 * It is primarily taken advantage of by Orca's own implementation of [HttpResponse.body], which
 * takes a [KClass] in instead of a reified type parameter (as does [io.ktor.client.call.body]).
 *
 * @param T Type whose [KClass] will be used to create a [KType].
 * @see defaultFor
 */
internal interface KTypeCreator<T : Any> {
  /**
   * Creates a [KType] from the [kClass].
   *
   * @param kClass [KClass] for which the [KType] to be created is.
   */
  fun create(kClass: KClass<T>): KType

  companion object {
    /**
     * [IllegalArgumentException] thrown if a [KType] is tried to be created for a [KClass] that
     * cannot be handled by a default [KTypeCreator].
     *
     * @param kClass [KClass] for which a [KType] couldn't be created.
     * @see defaultFor
     */
    class ComplexTypeException(kClass: KClass<*>) :
      IllegalArgumentException("Cannot create a type from a $kClass with a default creator.")

    /**
     * [KTypeCreator] that creates a [KType] from a [KClass] through [createType], without
     * specifying any type parameters or annotations. Useful for simpler types that do not require
     * these details to be set.
     *
     * **NOTE**: A [ComplexTypeException] will be thrown if [T] is a complex type and a [KType] for
     * a [KClass] of it is requested to be created, denoting that it should be done so manually
     * instead of delegating the creation to a default [KTypeCreator].
     *
     * @param T Type whose [KClass] will be used to create a [KType].
     */
    fun <T : Any> defaultFor(): KTypeCreator<T> {
      return object : KTypeCreator<T> {
        override fun create(kClass: KClass<T>): KType {
          return try {
            kClass.createType()
          } catch (_: IllegalArgumentException) {
            throw ComplexTypeException(kClass)
          }
        }
      }
    }
  }
}
