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

package br.com.orcinus.orca.core.mastodon.network.request.headers

import br.com.orcinus.orca.core.mastodon.network.InternalNetworkApi
import io.ktor.http.ContentType
import io.ktor.http.HeaderValueParam
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/**
 * [KSerializer] for serializing and deserializing a [ContentType], returned by
 * [ContentType.Companion.serializer].
 */
private object ContentTypeKSerializer : KSerializer<ContentType> {
  override val descriptor =
    buildClassSerialDescriptor(ContentTypeKSerializer::class.java.name) {
      element<String>("contentType")
      element<String>("contentSubtype")
      element(
        "parameters",
        @OptIn(ExperimentalSerializationApi::class)
        listSerialDescriptor(HeaderValueParamKSerializer.descriptor)
      )
    }

  override fun serialize(encoder: Encoder, value: ContentType) {
    encoder.encodeStructure(descriptor) {
      encodeStringElement(descriptor, index = 0, value.contentType)
      encodeStringElement(descriptor, index = 1, value.contentSubtype)
      encodeSerializableElement(
        descriptor,
        index = 2,
        ListSerializer(HeaderValueParamKSerializer),
        value.parameters
      )
    }
  }

  override fun deserialize(decoder: Decoder): ContentType {
    return decoder.decodeStructure(descriptor) {
      lateinit var contentType: String
      lateinit var contentSubtype: String
      lateinit var parameters: List<HeaderValueParam>
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 -> contentType = decodeStringElement(descriptor, index)
          1 -> contentSubtype = decodeStringElement(descriptor, index)
          2 ->
            parameters =
              decodeSerializableElement(
                descriptor,
                index,
                ListSerializer(HeaderValueParamKSerializer)
              )
          else -> break
        }
      }
      ContentType(contentType, contentSubtype, parameters)
    }
  }
}

/** [KSerializer] for serializing and deserializing a [ContentType]. */
@InternalNetworkApi
internal fun ContentType.Companion.serializer(): KSerializer<ContentType> {
  return ContentTypeKSerializer
}
