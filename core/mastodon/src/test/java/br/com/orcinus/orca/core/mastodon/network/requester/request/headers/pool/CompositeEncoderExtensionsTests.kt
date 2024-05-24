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

import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.HeaderValueParamKSerializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.input.DefaultInputTestRule
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.input.serializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.memory.ByteBufferKSerializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.memory.serializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.serializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.strings.serializer
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HeaderValueParam
import io.ktor.util.StringValues
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.bits.Memory
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.core.internal.ChunkBuffer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import org.junit.Rule

internal class CompositeEncoderExtensionsTests {
  @get:Rule val defaultInputRule = DefaultInputTestRule()

  private object Unknown

  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenEncodingAnUnknownElement() {
    mockk<CompositeEncoder> {
      encodeElement(buildClassSerialDescriptor(Unknown::class.java.name), index = 0, Unknown)
    }
  }

  @Test
  fun encodesBooleanElement() {
    mockk<CompositeEncoder> {
      every { encodeBooleanElement(serialDescriptor<Boolean>(), index = 0, false) } returns Unit
      encodeElement(serialDescriptor<Boolean>(), index = 0, false)
      verify(exactly = 1) { encodeBooleanElement(serialDescriptor<Boolean>(), index = 0, false) }
    }
  }

  @Test
  fun encodesByteElement() {
    mockk<CompositeEncoder> {
      every { encodeByteElement(serialDescriptor<Byte>(), index = 0, 0b00000001) } returns Unit
      encodeElement(serialDescriptor<Byte>(), index = 0, 0b00000001.toByte())
      verify(exactly = 1) { encodeByteElement(serialDescriptor<Byte>(), index = 0, 0b00000001) }
    }
  }

  @Test
  fun encodesByteBufferElement() {
    mockk<CompositeEncoder> {
      every {
        encodeSerializableElement(
          ByteBufferKSerializer.descriptor,
          index = 0,
          ByteBufferKSerializer,
          ByteBuffer.wrap(byteArrayOf(0b00000001))
        )
      } returns Unit
      encodeElement(
        ByteBufferKSerializer.descriptor,
        index = 0,
        ByteBuffer.wrap(byteArrayOf(0b00000001))
      )
      verify(exactly = 1) {
        encodeSerializableElement(
          ByteBufferKSerializer.descriptor,
          index = 0,
          ByteBufferKSerializer,
          ByteBuffer.wrap(byteArrayOf(0b00000001))
        )
      }
    }
  }

  @Test
  fun encodesByteReadChannelElement() {
    val content = ByteArray(size = 1) { 0b00000001 }
    val byteReadChannel = ByteReadChannel(content)
    mockk<CompositeEncoder> {
      every {
        encodeSerializableElement(
          ByteReadChannel.serializer().descriptor,
          index = 0,
          ByteReadChannel.serializer(),
          byteReadChannel
        )
      } returns Unit
      encodeElement(ByteReadChannel.serializer().descriptor, index = 0, byteReadChannel)
      verify(exactly = 1) {
        encodeSerializableElement(
          ByteReadChannel.serializer().descriptor,
          index = 0,
          ByteReadChannel.serializer(),
          byteReadChannel
        )
      }
    }
  }

  @Test
  fun encodesCharElement() {
    mockk<CompositeEncoder> {
      every { encodeCharElement(serialDescriptor<Char>(), index = 0, ' ') } returns Unit
      encodeElement(serialDescriptor<Char>(), index = 0, ' ')
      verify(exactly = 1) { encodeCharElement(serialDescriptor<Char>(), index = 0, ' ') }
    }
  }

  @Test
  fun encodesChunkBufferElement() {
    mockk<CompositeEncoder> {
      every {
        encodeSerializableElement(
          ChunkBuffer.serializer().descriptor,
          index = 0,
          ChunkBuffer.serializer(),
          ChunkBuffer.Empty
        )
      } returns Unit
      encodeElement(ChunkBuffer.serializer().descriptor, index = 0, ChunkBuffer.Empty)
      verify(exactly = 1) {
        encodeSerializableElement(
          ChunkBuffer.serializer().descriptor,
          index = 0,
          ChunkBuffer.serializer(),
          ChunkBuffer.Empty
        )
      }
    }
  }

  @Test
  fun encodesContentDispositionElement() {
    mockk<CompositeEncoder> {
      every {
        encodeSerializableElement(
          ContentDisposition.serializer().descriptor,
          index = 0,
          ContentDisposition.serializer(),
          ContentDisposition.File
        )
      } returns Unit
      encodeElement(ContentDisposition.serializer().descriptor, index = 0, ContentDisposition.File)
      verify(exactly = 1) {
        encodeSerializableElement(
          ContentDisposition.serializer().descriptor,
          index = 0,
          ContentDisposition.serializer(),
          ContentDisposition.File
        )
      }
    }
  }

  @Test
  fun encodesContentTypeElement() {
    mockk<CompositeEncoder> {
      every {
        encodeSerializableElement(
          ContentType.serializer().descriptor,
          index = 0,
          ContentType.serializer(),
          ContentType.Any
        )
      } returns Unit
      encodeElement(ContentType.serializer().descriptor, index = 0, ContentType.Any)
      verify(exactly = 1) {
        encodeSerializableElement(
          ContentType.serializer().descriptor,
          index = 0,
          ContentType.serializer(),
          ContentType.Any
        )
      }
    }
  }

  @Test
  fun encodesFloatElement() {
    mockk<CompositeEncoder> {
      every { encodeFloatElement(serialDescriptor<Float>(), index = 0, 0f) } returns Unit
      encodeElement(serialDescriptor<Float>(), index = 0, 0f)
      verify(exactly = 1) { encodeFloatElement(serialDescriptor<Float>(), index = 0, 0f) }
    }
  }

  @Test
  fun encodesHeaderValueParamElement() {
    mockk<CompositeEncoder> {
      every {
        encodeSerializableElement(
          HeaderValueParamKSerializer.descriptor,
          index = 0,
          HeaderValueParamKSerializer,
          HeaderValueParam("charset", "utf-8")
        )
      } returns Unit
      encodeElement(
        HeaderValueParamKSerializer.descriptor,
        index = 0,
        HeaderValueParam("charset", "utf-8")
      )
      verify(exactly = 1) {
        encodeSerializableElement(
          HeaderValueParamKSerializer.descriptor,
          index = 0,
          HeaderValueParamKSerializer,
          HeaderValueParam("charset", "utf-8")
        )
      }
    }
  }

  @Test
  fun encodesInputElement() {
    mockk<CompositeEncoder> {
      every {
        encodeSerializableElement(
          Input.serializer().descriptor,
          index = 0,
          Input.serializer(),
          defaultInputRule.defaultInput
        )
      } returns Unit
      encodeElement(Input.serializer().descriptor, index = 0, defaultInputRule.defaultInput)
      verify(exactly = 1) {
        encodeSerializableElement(
          Input.serializer().descriptor,
          index = 0,
          Input.serializer(),
          defaultInputRule.defaultInput
        )
      }
    }
  }

  @Test
  fun encodesIntElement() {
    mockk<CompositeEncoder> {
      every { encodeIntElement(serialDescriptor<Int>(), index = 0, 0) } returns Unit
      encodeElement(serialDescriptor<Int>(), index = 0, 0)
      verify(exactly = 1) { encodeIntElement(serialDescriptor<Int>(), index = 0, 0) }
    }
  }

  @Test
  fun encodesMemoryElement() {
    mockk<CompositeEncoder> {
      every {
        encodeSerializableElement(
          Memory.serializer().descriptor,
          index = 0,
          Memory.serializer(),
          Memory.Empty
        )
      } returns Unit
      encodeElement(Memory.serializer().descriptor, index = 0, Memory.Empty)
      verify(exactly = 1) {
        encodeSerializableElement(
          Memory.serializer().descriptor,
          index = 0,
          Memory.serializer(),
          Memory.Empty
        )
      }
    }
  }

  @Test
  fun encodesLongElement() {
    mockk<CompositeEncoder> {
      every { encodeLongElement(serialDescriptor<Long>(), index = 0, 0) } returns Unit
      encodeElement(serialDescriptor<Long>(), index = 0, 0L)
      verify(exactly = 1) { encodeLongElement(serialDescriptor<Long>(), index = 0, 0L) }
    }
  }

  @Test
  fun encodesShortElement() {
    mockk<CompositeEncoder> {
      every { encodeShortElement(serialDescriptor<Short>(), index = 0, 0) } returns Unit
      encodeElement(serialDescriptor<Short>(), index = 0, 0.toShort())
      verify(exactly = 1) { encodeShortElement(serialDescriptor<Short>(), index = 0, 0) }
    }
  }

  @Test
  fun encodesStringValuesElement() {
    mockk<CompositeEncoder> {
      every {
        encodeSerializableElement(
          StringValues.serializer().descriptor,
          index = 0,
          StringValues.serializer(),
          StringValues.Empty
        )
      } returns Unit
      encodeElement(StringValues.serializer().descriptor, index = 0, StringValues.Empty)
      verify(exactly = 1) {
        encodeSerializableElement(
          StringValues.serializer().descriptor,
          index = 0,
          StringValues.serializer(),
          StringValues.Empty
        )
      }
    }
  }
}
