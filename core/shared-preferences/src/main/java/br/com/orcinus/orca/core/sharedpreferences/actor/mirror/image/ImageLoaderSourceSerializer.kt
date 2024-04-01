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

package br.com.orcinus.orca.core.sharedpreferences.actor.mirror.image

import br.com.orcinus.orca.std.image.ImageLoader
import java.net.URL
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * [KSerializer] that serializes and deserializes an [ImageLoader] source into and from a [String].
 */
internal class ImageLoaderSourceSerializer : KSerializer<Any> {
  /** [KClass] of the [ImageLoader] source that's been last serialized. */
  private var sourceClass: KClass<*>? = null

  override val descriptor = PrimitiveSerialDescriptor("Source", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: Any) {
    return encoder.encodeString(
      ImageLoaderProviderFactory.fold(
        value::class.also { sourceClass = it },
        onURL = value::toString,
        onSampleImageSource = { value::class.java.name }
      )
    )
  }

  override fun deserialize(decoder: Decoder): Any {
    return decoder.decodeString().let { decoded ->
      ImageLoaderProviderFactory.fold<Any, _>(
        decoded,
        onURL = { URL(decoded) },
        onSampleImageSource = { sampleImageSource -> sampleImageSource }
      )
    }
  }
}
