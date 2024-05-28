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

package br.com.orcinus.orca.core.mastodon.network.request.headers.form

import br.com.orcinus.orca.core.mastodon.network.request.headers.form.item.BinaryChannelItemKSerializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.form.item.BinaryItemKSerializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.form.item.FileItemKSerializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.form.item.FormItemKSerializer
import io.ktor.http.content.PartData
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/** [KSerializer] for serializing and deserializing a [PartData]. */
internal object PartDataKSerializer : KSerializer<PartData> {
  override val descriptor =
    buildClassSerialDescriptor(PartDataKSerializer::class.java.name) {
      element<String>("className")
    }

  override fun serialize(encoder: Encoder, value: PartData) {
    encoder.encodeStructure(descriptor) {
      encodeStringElement(descriptor, index = 0, requireNotNull(value::class.qualifiedName))
    }
    when (value) {
      is PartData.BinaryChannelItem -> BinaryChannelItemKSerializer.serialize(encoder, value)
      is PartData.BinaryItem -> BinaryItemKSerializer.serialize(encoder, value)
      is PartData.FileItem -> FileItemKSerializer.serialize(encoder, value)
      is PartData.FormItem -> FormItemKSerializer.serialize(encoder, value)
    }
  }

  override fun deserialize(decoder: Decoder): PartData {
    return when (
      val className =
        decoder.decodeStructure(descriptor) {
          decodeStringElement(descriptor, decodeElementIndex(descriptor))
        }
    ) {
      PartData.BinaryChannelItem::class.qualifiedName ->
        BinaryChannelItemKSerializer.deserialize(decoder)
      PartData.BinaryItem::class.qualifiedName -> BinaryItemKSerializer.deserialize(decoder)
      PartData.FileItem::class.qualifiedName -> FileItemKSerializer.deserialize(decoder)
      PartData.FormItem::class.qualifiedName -> FormItemKSerializer.deserialize(decoder)
      else -> throw IllegalStateException("Unknown part data: $className.")
    }
  }
}
