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
import io.ktor.http.HeaderValueParam
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/** [KSerializer] for serializing and deserializing a [HeaderValueParam]. */
@InternalNetworkApi
internal object HeaderValueParamKSerializer : KSerializer<HeaderValueParam> {
  override val descriptor =
    buildClassSerialDescriptor(HeaderValueParamKSerializer::class.java.name) {
      element<String>("name")
      element<String>("value")
      element<Boolean>("escapeValue")
    }

  override fun serialize(encoder: Encoder, value: HeaderValueParam) {
    encoder.encodeStructure(descriptor) {
      encodeStringElement(descriptor, index = 0, value.name)
      encodeStringElement(descriptor, index = 1, value.value)
      encodeBooleanElement(descriptor, index = 2, value.escapeValue)
    }
  }

  override fun deserialize(decoder: Decoder): HeaderValueParam {
    return decoder.decodeStructure(descriptor) {
      lateinit var name: String
      lateinit var value: String
      var escapeValue = false
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 -> name = decodeStringElement(descriptor, index)
          1 -> value = decodeStringElement(descriptor, index)
          2 -> escapeValue = decodeBooleanElement(descriptor, index)
          else -> break
        }
      }
      HeaderValueParam(name, value, escapeValue)
    }
  }
}
