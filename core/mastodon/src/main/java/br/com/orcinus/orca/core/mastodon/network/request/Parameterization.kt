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

package br.com.orcinus.orca.core.mastodon.network.request

import br.com.orcinus.orca.core.mastodon.network.InternalNetworkApi
import br.com.orcinus.orca.core.mastodon.network.request.headers.form.PartDataKSerializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.strings.serializer
import io.ktor.http.Parameters
import io.ktor.http.content.PartData
import io.ktor.util.StringValues
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer

/**
 * Holds the parameters of a request, which vary in type depending on where they're located.
 *
 * @param T Content to be held.
 */
@InternalNetworkApi
internal sealed class Parameterization<T : Any> {
  /** Content characterizing the parameters of the request. */
  abstract val content: T

  /** [KSerializer] by which the [content] can be serialized. */
  abstract val serializer: KSerializer<T>

  /**
   * `application/x-www-form-urlencoded`-MIME-typed [Parameterization] whose [content] is to be
   * inserted into the body of a request.
   */
  data class Body(override val content: Parameters) : Parameterization<StringValues>() {
    override val serializer = StringValues.serializer()
  }

  /**
   * `multipart/form-data`-MIME-typed [Parameterization] whose [content] is to be inserted as
   * headers of the request.
   */
  data class Headers(override val content: List<PartData>) : Parameterization<List<PartData>>() {
    override val serializer = ListSerializer(PartDataKSerializer)
  }

  companion object {
    /** [Parameterization] whose [content] is empty. */
    val empty: Parameterization<StringValues> = Body(Parameters.Empty)
  }
}
