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

package br.com.orcinus.orca.core.mastodon.network.request.headers.pool

import br.com.orcinus.orca.core.mastodon.network.request.headers.HeaderValueParamKSerializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.form.item.BinaryChannelItemKSerializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.form.item.BinaryItemKSerializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.form.item.FileItemKSerializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.form.item.FormItemKSerializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.memory.ByteBufferKSerializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.memory.serializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.serializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.strings.serializer
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HeaderValueParam
import io.ktor.http.Headers
import io.ktor.http.content.PartData
import io.ktor.util.StringValues
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.bits.Memory
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.core.internal.ChunkBuffer
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import io.ktor.utils.io.streams.asInput
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder

internal class CompositeEncoderExtensionsTests {
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
      byteArrayOf(0).inputStream().asInput().use {
        every {
          encodeSerializableElement(
            Input.serializer().descriptor,
            index = 0,
            Input.serializer(),
            it
          )
        } returns Unit
        encodeElement(Input.serializer().descriptor, index = 0, it)
        verify(exactly = 1) {
          encodeSerializableElement(
            Input.serializer().descriptor,
            index = 0,
            Input.serializer(),
            it
          )
        }
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
  fun encodesBinaryChannelItemElement() {
    val binaryChannel = byteArrayOf(0).inputStream().toByteReadChannel()
    val binaryChannelItem = PartData.BinaryChannelItem({ binaryChannel }, Headers.Empty)
    mockk<CompositeEncoder> {
      every {
        encodeSerializableElement(
          BinaryChannelItemKSerializer.descriptor,
          index = 0,
          BinaryChannelItemKSerializer,
          binaryChannelItem
        )
      } returns Unit
      encodeElement(BinaryChannelItemKSerializer.descriptor, index = 0, binaryChannelItem)
      verify(exactly = 1) {
        encodeSerializableElement(
          BinaryChannelItemKSerializer.descriptor,
          index = 0,
          BinaryChannelItemKSerializer,
          binaryChannelItem
        )
      }
    }
  }

  @Test
  fun encodesBinaryItemElement() {
    val binary = byteArrayOf(0).inputStream().asInput()
    val binaryItem = PartData.BinaryItem({ binary }, dispose = binary::close, Headers.Empty)
    mockk<CompositeEncoder> {
      every {
        encodeSerializableElement(
          BinaryItemKSerializer.descriptor,
          index = 0,
          BinaryItemKSerializer,
          binaryItem
        )
      } returns Unit
      encodeElement(BinaryItemKSerializer.descriptor, index = 0, binaryItem)
      verify(exactly = 1) {
        encodeSerializableElement(
          BinaryItemKSerializer.descriptor,
          index = 0,
          BinaryItemKSerializer,
          binaryItem
        )
      }
    }
    binaryItem.dispose()
  }

  @Test
  fun encodesFileItemElement() {
    val file = byteArrayOf(0).inputStream().asInput()
    val fileItem = PartData.FileItem({ file }, dispose = file::close, Headers.Empty)
    mockk<CompositeEncoder> {
      every {
        encodeSerializableElement(
          FileItemKSerializer.descriptor,
          index = 0,
          FileItemKSerializer,
          fileItem
        )
      } returns Unit
      encodeElement(FileItemKSerializer.descriptor, index = 0, fileItem)
      verify(exactly = 1) {
        encodeSerializableElement(
          FileItemKSerializer.descriptor,
          index = 0,
          FileItemKSerializer,
          fileItem
        )
      }
    }
    fileItem.dispose()
  }

  @Test
  fun encodesFormItemElement() {
    val formItem =
      PartData.FormItem("${HeaderValueParam("filename", "file.png")}", dispose = {}, Headers.Empty)
    mockk<CompositeEncoder> {
      every {
        encodeSerializableElement(
          FormItemKSerializer.descriptor,
          index = 0,
          FormItemKSerializer,
          formItem
        )
      } returns Unit
      encodeElement(FormItemKSerializer.descriptor, index = 0, formItem)
      verify(exactly = 1) {
        encodeSerializableElement(
          FormItemKSerializer.descriptor,
          index = 0,
          FormItemKSerializer,
          formItem
        )
      }
    }
    formItem.dispose()
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
