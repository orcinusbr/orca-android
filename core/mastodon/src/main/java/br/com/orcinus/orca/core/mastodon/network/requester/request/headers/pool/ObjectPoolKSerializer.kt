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
import io.ktor.utils.io.pool.DefaultPool
import io.ktor.utils.io.pool.ObjectPool
import kotlin.reflect.KClass
import kotlin.reflect.full.starProjectedType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/**
 * [KSerializer] for serializing and deserializing an [ObjectPool].
 *
 * **NOTE**: Serializing and deserializing pooled instances can cause an [IllegalArgumentException]
 * to be thrown, given that only a certain set of objects can be encoded and decoded. For more
 * information, refer to the documentations of [CompositeEncoder.encodeElement] and
 * [CompositeDecoder.decodeElement].
 *
 * @param T Object contained by the [ObjectPool].
 * @param instanceClass [KClass] of the pooled instance.
 */
@InternalRequesterApi
internal class ObjectPoolKSerializer<T : Any>(private val instanceClass: KClass<T>) :
  KSerializer<ObjectPool<T>> {
  override val descriptor =
    buildClassSerialDescriptor(ObjectPoolKSerializer::class.java.name) {
      element<Int>("capacity")
      element("instance", serialDescriptor(instanceClass.starProjectedType))
    }

  override fun serialize(encoder: Encoder, value: ObjectPool<T>) {
    encoder.encodeStructure(descriptor) {
      encodeIntElement(descriptor, index = 0, value.capacity)
      encodeElement(descriptor, index = 1, value.borrow())
    }
  }

  @Throws(IllegalArgumentException::class)
  override fun deserialize(decoder: Decoder): ObjectPool<T> {
    return decoder.decodeStructure(descriptor) {
      var capacity = 0
      lateinit var instance: T
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 -> capacity = decodeIntElement(descriptor, index)
          1 -> instance = decodeElement(descriptor, index, instanceClass)
          else -> break
        }
      }
      object : DefaultPool<T>(capacity) {
        override fun produceInstance(): T {
          return instance
        }
      }
    }
  }
}
