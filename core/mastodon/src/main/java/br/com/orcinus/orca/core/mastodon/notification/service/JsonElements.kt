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

package br.com.orcinus.orca.core.mastodon.notification.service

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/** Class that provides [JsonElement]-based utility methods. */
internal object JsonElements {
  /**
   * Converting a [JsonElement] directly into a [String] by calling [toString] includes two double
   * quotes as both the prefix and the suffix of each [JsonPrimitive] value. This method converts
   * the receiver [JsonElement] into a [String], but omits these surrounding double quotes from
   * every one of such nested and non-nested values.
   */
  @JvmStatic
  fun JsonElement.toNonLiteralString(): String {
    return when (this) {
      is JsonArray -> toNonLiteralString()
      is JsonObject -> toNonLiteralString()
      is JsonPrimitive -> content.ifEmpty { toString() }
    }
  }

  /**
   * Converts this [JsonArray] into a [String], omitting the two surrounding double quotes from
   * every [JsonPrimitive] contained in it.
   */
  @JvmStatic
  private fun JsonArray.toNonLiteralString(): String {
    return buildString(capacity = "$this".length) {
      append('[')
      for ((index, element) in this@toNonLiteralString.withIndex()) {
        val isNotLastElement = index < this@toNonLiteralString.lastIndex
        append(element.toNonLiteralString())
        if (isNotLastElement) {
          append(',')
        }
      }
      append(']')
    }
  }

  /**
   * Converts this [JsonObject] into a [String], omitting the two surrounding double quotes from
   * every [JsonPrimitive] value (nested and non-nested).
   */
  @JvmStatic
  private fun JsonObject.toNonLiteralString(): String {
    return buildString(capacity = "$this".length) {
      append('{')
      for ((index, entry) in entries.withIndex()) {
        val (key, element) = entry
        val isNotLastEntry = index < entries.size.dec()
        append('"').append(key).append("\":")
        append(element.toNonLiteralString())
        if (isNotLastEntry) {
          append(',')
        }
      }
      append('}')
    }
  }
}
