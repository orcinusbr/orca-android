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

package br.com.orcinus.orca.core.mastodon.network.requester.request.headers.pool

import br.com.orcinus.orca.core.mastodon.network.requester.InternalRequesterApi
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.HeaderValueParamKSerializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.memory.ByteBufferKSerializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.memory.serializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.serializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.strings.serializer
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HeaderValueParam
import io.ktor.util.StringValues
import io.ktor.utils.io.ByteReadChannel
import java.nio.ByteBuffer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder

/**
 * Encodes the [value] with its own [KSerializer].
 *
 * @param descriptor [SerialDescriptor] in which the [value] to be encoded is described.
 * @param index Index of the [value] in the [descriptor].
 * @param value Object on which encoding will be performed.
 * @throws IllegalArgumentException If the [value] cannot be encoded. Specifically, if its type
 *   isn't one of the following supported ones:
 *     - [Boolean];
 *     - [Byte];
 *     - [ByteBuffer];
 *     - [ByteReadChannel];
 *     - [Char];
 *     - [ContentDisposition];
 *     - [ContentType];
 *     - [Float];
 *     - [HeaderValueParam];
 *     - [Int];
 *     - [Long];
 *     - [Short]; or
 *     - [StringValues].
 */
@InternalRequesterApi
@Throws(IllegalArgumentException::class)
internal fun CompositeEncoder.encodeElement(descriptor: SerialDescriptor, index: Int, value: Any) {
  when (value) {
    is Boolean -> encodeBooleanElement(descriptor, index, value)
    is Byte -> encodeByteElement(descriptor, index, value)
    is ByteBuffer -> encodeSerializableElement(descriptor, index, ByteBufferKSerializer, value)
    is ByteReadChannel ->
      encodeSerializableElement(descriptor, index, ByteReadChannel.serializer(), value)
    is Char -> encodeCharElement(descriptor, index, value)
    is ContentDisposition ->
      encodeSerializableElement(descriptor, index, ContentDisposition.serializer(), value)
    is ContentType -> encodeSerializableElement(descriptor, index, ContentType.serializer(), value)
    is Float -> encodeFloatElement(descriptor, index, value)
    is HeaderValueParam ->
      encodeSerializableElement(descriptor, index, HeaderValueParamKSerializer, value)
    is Int -> encodeIntElement(descriptor, index, value)
    is Long -> encodeLongElement(descriptor, index, value)
    is Short -> encodeShortElement(descriptor, index, value)
    is StringValues ->
      encodeSerializableElement(descriptor, index, StringValues.serializer(), value)
    else -> throw IllegalArgumentException("Cannot encode $value.")
  }
}
