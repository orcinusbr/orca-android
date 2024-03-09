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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination.type

import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination.MastodonPostPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination.body
import io.ktor.client.statement.HttpResponse
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * Creates a [KType] for [T].
 *
 * Such behavior is necessary because of the generic nature of a [MastodonPostPaginator]: since its
 * type parameter (which is the DTO to be received from the API when pagination takes place) cannot
 * be reified (at least not as of Kotlin 1.9.21), the [HttpResponse] needs to know how to actually
 * convert the payload into the specified object. Tends to result in a somewhat manual process if
 * there are, for example, type arguments that need to be given; otherwise, a default [KTypeCreator]
 * provided by [kTypeCreatorOf] can automatically do the work all by itself.
 *
 * It is primarily taken advantage of by Orca's own implementation of [body] for an [HttpResponse],
 * which takes a [KTypeCreator] in instead of a reified type parameter (as does
 * [io.ktor.client.call.body]).
 *
 * @param T Object for which the [KType] to be created is.
 */
internal interface KTypeCreator<T : Any> {
  /**
   * [KClass] from which the [KType] can be created.
   *
   * @see create
   */
  val kClass: KClass<T>

  /** Creates a [KType] for [T]. */
  fun create(): KType
}
