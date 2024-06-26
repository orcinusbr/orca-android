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

package br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.strings

import br.com.orcinus.orca.core.mastodon.instance.requester.InternalRequesterApi
import io.ktor.util.StringValues
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapEntrySerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.descriptors.mapSerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * [KSerializer] for serializing and deserializing [StringValues], returned by
 * [StringValues.Companion.serializer].
 */
private object StringValuesKSerializer : KSerializer<StringValues> {
  /**
   * [KSerializer] by which the [Set] containing [StringValues]' entries will be serialized.
   *
   * @see StringValues.entries
   */
  private val setSerializer =
    SetSerializer(MapEntrySerializer(String.serializer(), ListSerializer(String.serializer())))

  @OptIn(ExperimentalSerializationApi::class)
  override val descriptor =
    SerialDescriptor(
      StringValuesKSerializer::class.java.name,
      mapSerialDescriptor(
        keyDescriptor = serialDescriptor<String>(),
        valueDescriptor = listSerialDescriptor(serialDescriptor<String>())
      )
    )

  override fun serialize(encoder: Encoder, value: StringValues) {
    encoder.encodeSerializableValue(setSerializer, value.entries())
  }

  override fun deserialize(decoder: Decoder): StringValues {
    val entries = decoder.decodeSerializableValue(setSerializer)
    return StringValues.build { entries.forEach { appendAll(it.key, it.value) } }
  }
}

/** [KSerializer] for serializing and deserializing [StringValues]. */
@InternalRequesterApi
internal fun StringValues.Companion.serializer(): KSerializer<StringValues> {
  return StringValuesKSerializer
}
