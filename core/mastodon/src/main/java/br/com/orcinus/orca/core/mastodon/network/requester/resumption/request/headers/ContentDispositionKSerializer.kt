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

package br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers

import br.com.orcinus.orca.core.mastodon.network.InternalNetworkApi
import io.ktor.http.ContentDisposition
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
 * [KSerializer] for serializing and deserializing a [ContentDisposition], returned by
 * [ContentDisposition.Companion.serializer].
 */
private object ContentDispositionKSerializer : KSerializer<ContentDisposition> {
  override val descriptor =
    buildClassSerialDescriptor(ContentDispositionKSerializer::class.java.name) {
      element<String>("disposition")

      element(
        "parameters",
        @OptIn(ExperimentalSerializationApi::class)
        listSerialDescriptor(HeaderValueParamKSerializer.descriptor)
      )
    }

  override fun serialize(encoder: Encoder, value: ContentDisposition) {
    encoder.encodeStructure(descriptor) {
      encodeStringElement(descriptor, index = 0, value.disposition)
      encodeSerializableElement(
        descriptor,
        index = 1,
        ListSerializer(HeaderValueParamKSerializer),
        value.parameters
      )
    }
  }

  override fun deserialize(decoder: Decoder): ContentDisposition {
    return decoder.decodeStructure(descriptor) {
      lateinit var disposition: String
      lateinit var parameters: List<HeaderValueParam>
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 -> disposition = decodeStringElement(descriptor, index)
          1 ->
            parameters =
              decodeSerializableElement(
                descriptor,
                index,
                ListSerializer(HeaderValueParamKSerializer)
              )
          else -> break
        }
      }
      ContentDisposition(disposition, parameters)
    }
  }
}

/** [KSerializer] for serializing and deserializing a [ContentDisposition]. */
@InternalNetworkApi
internal fun ContentDisposition.Companion.serializer(): KSerializer<ContentDisposition> {
  return ContentDispositionKSerializer
}
