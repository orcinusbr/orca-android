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

package br.com.orcinus.orca.core.sharedpreferences.actor.mirror

import br.com.orcinus.orca.ext.reflection.access
import kotlin.reflect.full.primaryConstructor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

/**
 * Creates an instance of a [kotlinx.serialization.json.internal.JsonTreeDecoder] reflectively.
 *
 * @param value [JsonObject] to be decoded.
 */
internal fun createJsonTreeDecoder(value: JsonObject): Decoder {
  return Class.forName("kotlinx.serialization.json.internal.JsonTreeDecoder")
    .kotlin
    .primaryConstructor
    ?.access { call(Json, value, null as String?, null as SerialDescriptor?) as Decoder }
    ?: throw NoSuchMethodException(
      "JsonTreeDecoder.<init>(Json, JsonObject, String?, SerialDescriptor?)"
    )
}
