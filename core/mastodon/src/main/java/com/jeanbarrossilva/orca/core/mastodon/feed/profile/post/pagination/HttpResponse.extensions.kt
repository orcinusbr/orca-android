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

import io.ktor.client.call.DoubleReceiveException
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.reflect.TypeInfo
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.javaType

/**
 * Receives the payload as being a [T].
 *
 * @param T Body to be obtained.
 * @param typeCreator [KTypeCreator] by which a [KType] for the [bodyClass] will be created.
 * @param bodyClass [KClass] of the body to be returned.
 * @throws DoubleReceiveException If the payload has already been received.
 * @throws NoTransformationFoundException If no transformation for [T] is found.
 */
@Throws(DoubleReceiveException::class, NoTransformationFoundException::class)
internal suspend fun <T : Any> HttpResponse.body(
  typeCreator: KTypeCreator,
  bodyClass: KClass<T>
): T {
  val type = typeCreator.create(bodyClass)
  @OptIn(ExperimentalStdlibApi::class) val typeInfo = TypeInfo(bodyClass, type.javaType, type)
  return body(typeInfo)
}
