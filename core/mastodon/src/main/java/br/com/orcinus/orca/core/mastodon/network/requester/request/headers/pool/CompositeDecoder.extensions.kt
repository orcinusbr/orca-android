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
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.bytes.ByteBufferKSerializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.bytes.serializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.serializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.strings.serializer
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HeaderValueParam
import io.ktor.util.StringValues
import io.ktor.utils.io.ByteReadChannel
import java.nio.ByteBuffer
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder

/**
 * Decodes a value with its own [KSerializer].
 *
 * @param T Object produced by the decoding process.
 * @param descriptor [SerialDescriptor] in which the value to be decoded is described.
 * @param index Index of the value in the [descriptor].
 * @throws IllegalArgumentException If a value of such type cannot be decoded. Specifically, if it
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
 *     - [Memory];
 *     - [Short]; or
 *     - [StringValues].
 */
@InternalRequesterApi
@Throws(IllegalArgumentException::class)
internal inline fun <reified T : Any> CompositeDecoder.decodeElement(
  descriptor: SerialDescriptor,
  index: Int
): T {
  return decodeElement(descriptor, index, T::class)
}

/**
 * Decodes a value with its own [KSerializer].
 *
 * @param T Object produced by the decoding process.
 * @param descriptor [SerialDescriptor] in which the value to be decoded is described.
 * @param index Index of the value in the [descriptor].
 * @param valueClass [KClass] of the value to be decoded.
 * @throws IllegalArgumentException If a value of such type cannot be decoded. Specifically, if it
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
internal fun <T : Any> CompositeDecoder.decodeElement(
  descriptor: SerialDescriptor,
  index: Int,
  valueClass: KClass<T>
): T {
  @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
  return when (valueClass) {
    Boolean::class -> decodeBooleanElement(descriptor, index)
    Byte::class -> decodeByteElement(descriptor, index)
    ByteBuffer::class -> decodeSerializableElement(descriptor, index, ByteBufferKSerializer)
    ByteReadChannel::class ->
      decodeSerializableElement(descriptor, index, ByteReadChannel.serializer())
    Char::class -> decodeCharElement(descriptor, index)
    ContentDisposition::class ->
      decodeSerializableElement(descriptor, index, ContentDisposition.serializer())
    ContentType::class -> decodeSerializableElement(descriptor, index, ContentType.serializer())
    Float::class -> decodeFloatElement(descriptor, index)
    HeaderValueParam::class ->
      decodeSerializableElement(descriptor, index, HeaderValueParamKSerializer)
    Int::class -> decodeIntElement(descriptor, index)
    Long::class -> decodeLongElement(descriptor, index)
    Short::class -> decodeShortElement(descriptor, index)
    StringValues::class -> decodeSerializableElement(descriptor, index, StringValues.serializer())
    else -> throw IllegalArgumentException("Cannot decode a ${valueClass.simpleName}.")
  }
    as T
}
