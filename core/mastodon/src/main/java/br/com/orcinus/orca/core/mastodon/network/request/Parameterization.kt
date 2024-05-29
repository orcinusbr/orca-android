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
import br.com.orcinus.orca.core.mastodon.network.request.headers.strings.toParameters
import io.ktor.http.Parameters
import io.ktor.http.content.PartData
import io.ktor.util.StringValues
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

/**
 * Holds the parameters of a request, which vary in type depending on where they're located.
 *
 * @param T Content to be held.
 */
@InternalNetworkApi
internal sealed class Parameterization<T : Any> {
  /** [KSerializer] by which the [content] can be serialized. */
  protected abstract val serializer: KSerializer<T>

  /** Name of this specific parameterization strategy. */
  abstract val name: String

  /** Content characterizing the parameters of the request. */
  abstract val content: T

  /** Serialized, [String] version of the [content]. */
  val serializedContent
    get() = Json.encodeToString(serializer, content)

  /**
   * `application/x-www-form-urlencoded`-MIME-typed [Parameterization] whose [content] is to be
   * inserted into the body of a request.
   */
  data class Body(override val content: Parameters) : Parameterization<StringValues>() {
    override val name = Companion.name
    override val serializer = Companion.serializer

    companion object {
      /** Name of a [Body] strategy. */
      val name = requireNotNull(Body::class.simpleName)

      /**
       * [KSerializer] by which the [Parameters] of a [Body] parameterization strategy can be
       * serialized.
       */
      val serializer = StringValues.serializer()
    }
  }

  /**
   * `multipart/form-data`-MIME-typed [Parameterization] whose [content] is to be inserted as
   * headers of the request.
   */
  data class Headers(override val content: List<PartData>) : Parameterization<List<PartData>>() {
    override val name = Companion.name
    override val serializer = Companion.serializer

    companion object {
      /** Name of a [Headers] strategy. */
      val name = requireNotNull(Headers::class.simpleName)

      /**
       * [KSerializer] by which the multiple parts of a [Body] parameterization strategy can be
       * serialized.
       */
      val serializer = ListSerializer(PartDataKSerializer)
    }
  }

  companion object {
    /** [Parameterization] whose [content] is empty. */
    val empty: Parameterization<StringValues> = Body(Parameters.Empty)

    /**
     * Deserializes the given serialized content, converting it into the parameterization strategy
     * whose name equals to the given one.
     *
     * @param name Name of the parameterization strategy into which the [serializedContent] will be
     *   deserialized.
     * @param serializedContent [String]-serialized content.
     * @throws IllegalArgumentException If the [name] isn't that of an existing parameterization
     *   strategy or [T] isn't serializable by the found [KSerializer].
     * @see Parameterization.name
     */
    @Throws(IllegalArgumentException::class)
    fun deserialize(name: String, serializedContent: String): Parameterization<*> {
      val serializer = getSerializerOf(name)
      val content = Json.decodeFromString(serializer, serializedContent)
      @Suppress("UNCHECKED_CAST")
      return fold(
        name,
        onBody = { Body((content as StringValues).toParameters()) },
        onHeaders = { Headers(content as List<PartData>) }
      )
    }

    /**
     * Obtains the [KSerializer] for serializing and deserializing the content of a parameterization
     * strategy whose name equals to the given one.
     *
     * @param name Name of the parameterization strategy whose [KSerializer] will be returned.
     * @throws IllegalArgumentException If the [name] isn't that of an existing parameterization
     *   strategy or [T] isn't serializable by the found [KSerializer].
     * @see Parameterization.name
     */
    @Throws(IllegalArgumentException::class)
    private fun getSerializerOf(name: String): KSerializer<*> {
      return fold(name, onBody = Body::serializer, onHeaders = Headers::serializer)
    }

    /**
     * Invokes one of the specified lambdas depending on whose parameterization strategy the [name]
     * is.
     *
     * @param T Result of executing one of the functions.
     * @param name Name of the parameterization strategy whose lambda will be invoked.
     * @param onBody Callback called if the [name] is of a [Body] strategy.
     * @param onHeaders Operation to be performed in case the name is of a [Headers] strategy.
     * @throws IllegalArgumentException If the [name] isn't that of an existing strategy.
     * @see Parameterization.name
     */
    @OptIn(ExperimentalContracts::class)
    @Throws(IllegalArgumentException::class)
    private inline fun <T> fold(name: String, onBody: () -> T, onHeaders: () -> T): T {
      contract {
        callsInPlace(onBody, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onHeaders, InvocationKind.AT_MOST_ONCE)
      }
      return when (name) {
        Body.name -> onBody()
        Headers.name -> onHeaders()
        else -> throw IllegalArgumentException("No parameterization strategy named \"$name\".")
      }
    }
  }
}
