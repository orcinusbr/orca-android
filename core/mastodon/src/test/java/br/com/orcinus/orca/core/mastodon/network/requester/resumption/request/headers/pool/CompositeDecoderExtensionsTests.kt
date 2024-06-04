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

package br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.pool

import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.HeaderValueParamKSerializer
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.form.item.BinaryChannelItemKSerializer
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.form.item.BinaryItemKSerializer
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.form.item.FileItemKSerializer
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.form.item.FormItemKSerializer
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.memory.ByteBufferKSerializer
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.memory.serializer
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.serializer
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.strings.serializer
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
import kotlinx.serialization.encoding.CompositeDecoder
import org.mockito.BDDMockito.then
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

internal class CompositeDecoderExtensionsTests {
  private object Unknown

  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenEncodingAnUnknownElement() {
    mockk<CompositeDecoder> {
      decodeElement<Unknown>(buildClassSerialDescriptor(Unknown::class.java.name), index = 0)
    }
  }

  @Test
  fun decodesBooleanElement() {
    mockk<CompositeDecoder> {
      every { decodeBooleanElement(serialDescriptor<Boolean>(), index = 0) } returns false
      decodeElement<Boolean>(serialDescriptor<Boolean>(), index = 0)
      verify(exactly = 1) { decodeBooleanElement(serialDescriptor<Boolean>(), index = 0) }
    }
  }

  @Test
  fun decodesByteElement() {
    mockk<CompositeDecoder> {
      every { decodeByteElement(serialDescriptor<Byte>(), index = 0) } returns 0b00000001
      decodeElement<Byte>(serialDescriptor<Byte>(), index = 0)
      verify(exactly = 1) { decodeByteElement(serialDescriptor<Byte>(), index = 0) }
    }
  }

  @Test
  fun decodesByteBufferElement() {
    mockk<CompositeDecoder> {
      every {
        decodeSerializableElement(
          ByteBufferKSerializer.descriptor,
          index = 0,
          ByteBufferKSerializer
        )
      } returns ByteBuffer.wrap(ByteArray(size = 1) { 0b00000001 })
      decodeElement<ByteBuffer>(ByteBufferKSerializer.descriptor, index = 0)
      verify(exactly = 1) {
        decodeSerializableElement(
          ByteBufferKSerializer.descriptor,
          index = 0,
          ByteBufferKSerializer
        )
      }
    }
  }

  @Test
  fun decodesByteReadChannelElement() {
    mockk<CompositeDecoder> {
      every {
        decodeSerializableElement(
          ByteReadChannel.serializer().descriptor,
          index = 0,
          ByteReadChannel.serializer()
        )
      } returns ByteReadChannel(ByteArray(size = 1) { 0b00000001 })
      decodeElement<ByteReadChannel>(ByteReadChannel.serializer().descriptor, index = 0)
      verify(exactly = 1) {
        decodeSerializableElement(
          ByteReadChannel.serializer().descriptor,
          index = 0,
          ByteReadChannel.serializer()
        )
      }
    }
  }

  @Test
  fun decodesCharElement() {
    mockk<CompositeDecoder> {
      every { decodeCharElement(serialDescriptor<Char>(), index = 0) } returns ' '
      decodeElement<Char>(serialDescriptor<Char>(), index = 0)
      verify(exactly = 1) { decodeCharElement(serialDescriptor<Char>(), index = 0) }
    }
  }

  @Test
  fun decodesChunkBufferElement() {
    mockk<CompositeDecoder> {
      every {
        decodeSerializableElement(
          ChunkBuffer.serializer().descriptor,
          index = 0,
          ChunkBuffer.serializer()
        )
      } returns ChunkBuffer.Empty
      decodeElement<ChunkBuffer>(ChunkBuffer.serializer().descriptor, index = 0)
      verify(exactly = 1) {
        decodeSerializableElement(
          ChunkBuffer.serializer().descriptor,
          index = 0,
          ChunkBuffer.serializer()
        )
      }
    }
  }

  @Test
  fun decodesContentDispositionElement() {
    mockk<CompositeDecoder> {
      every {
        decodeSerializableElement(
          ContentDisposition.serializer().descriptor,
          index = 0,
          ContentDisposition.serializer()
        )
      } returns ContentDisposition.File
      decodeElement<ContentDisposition>(ContentDisposition.serializer().descriptor, index = 0)
      verify(exactly = 1) {
        decodeSerializableElement(
          ContentDisposition.serializer().descriptor,
          index = 0,
          ContentDisposition.serializer()
        )
      }
    }
  }

  @Test
  fun decodesContentTypeElement() {
    mockk<CompositeDecoder> {
      every {
        decodeSerializableElement(
          ContentType.serializer().descriptor,
          index = 0,
          ContentType.serializer()
        )
      } returns ContentType.Any
      decodeElement<ContentType>(ContentType.serializer().descriptor, index = 0)
      verify(exactly = 1) {
        decodeSerializableElement(
          ContentType.serializer().descriptor,
          index = 0,
          ContentType.serializer()
        )
      }
    }
  }

  @Test
  fun decodesFloatElement() {
    mockk<CompositeDecoder> {
      every { decodeFloatElement(serialDescriptor<Float>(), index = 0) } returns 0f
      decodeElement<Float>(serialDescriptor<Float>(), index = 0)
      verify(exactly = 1) { decodeFloatElement(serialDescriptor<Float>(), index = 0) }
    }
  }

  @Test
  fun decodesHeaderValueParamElement() {
    mockk<CompositeDecoder> {
      every {
        decodeSerializableElement(
          HeaderValueParamKSerializer.descriptor,
          index = 0,
          HeaderValueParamKSerializer
        )
      } returns HeaderValueParam("charset", "utf-8")
      decodeElement<HeaderValueParam>(HeaderValueParamKSerializer.descriptor, index = 0)
      verify(exactly = 1) {
        decodeSerializableElement(
          HeaderValueParamKSerializer.descriptor,
          index = 0,
          HeaderValueParamKSerializer
        )
      }
    }
  }

  @Test
  fun decodesInputElement() {
    mockk<CompositeDecoder> {
      every {
        decodeSerializableElement(Input.serializer().descriptor, index = 0, Input.serializer())
      } returns ByteArray(size = 0).inputStream().asInput()
      decodeElement<Input>(Input.serializer().descriptor, index = 0)
      verify(exactly = 1) {
        decodeSerializableElement(Input.serializer().descriptor, index = 0, Input.serializer())
      }
    }
  }

  @Test
  fun decodesIntElement() {
    mockk<CompositeDecoder> {
      every { decodeIntElement(serialDescriptor<Int>(), index = 0) } returns 0
      decodeElement<Int>(serialDescriptor<Int>(), index = 0)
      verify(exactly = 1) { decodeIntElement(serialDescriptor<Int>(), index = 0) }
    }
  }

  @Test
  fun decodesLongElement() {
    mockk<CompositeDecoder> {
      every { decodeLongElement(serialDescriptor<Long>(), index = 0) } returns 0
      decodeElement<Long>(serialDescriptor<Long>(), index = 0)
      verify(exactly = 1) { decodeLongElement(serialDescriptor<Long>(), index = 0) }
    }
  }

  @Test
  fun decodesMemoryElement() {
    val decoder = mock(CompositeDecoder::class.java)
    val serializer = Memory.serializer()
    val descriptor = serializer.descriptor
    `when`(decoder.decodeSerializableElement(descriptor, index = 0, serializer))
      .thenReturn(Memory.Empty)
    decoder.decodeElement<Memory>(descriptor, index = 0)
    then(decoder).should().decodeSerializableElement(descriptor, index = 0, serializer)
  }

  @Test
  fun encodesBinaryChannelItemElement() {
    mockk<CompositeDecoder> {
      every {
        decodeSerializableElement(
          BinaryChannelItemKSerializer.descriptor,
          index = 0,
          BinaryChannelItemKSerializer
        )
      } returns
        PartData.BinaryChannelItem(
          { byteArrayOf(0).inputStream().toByteReadChannel() },
          Headers.Empty
        )
      decodeElement<PartData.BinaryChannelItem>(BinaryChannelItemKSerializer.descriptor, index = 0)
      verify(exactly = 1) {
        decodeSerializableElement(
          BinaryChannelItemKSerializer.descriptor,
          index = 0,
          BinaryChannelItemKSerializer
        )
      }
    }
  }

  @Test
  fun encodesBinaryItemElement() {
    mockk<CompositeDecoder> {
      every {
        decodeSerializableElement(
          BinaryItemKSerializer.descriptor,
          index = 0,
          BinaryItemKSerializer
        )
      } returns
        byteArrayOf(0).inputStream().asInput().use {
          PartData.BinaryItem({ it }, dispose = it::close, Headers.Empty)
        }
      decodeElement<PartData.BinaryItem>(BinaryItemKSerializer.descriptor, index = 0)
      verify(exactly = 1) {
        decodeSerializableElement(
          BinaryItemKSerializer.descriptor,
          index = 0,
          BinaryItemKSerializer
        )
      }
    }
  }

  @Test
  fun encodesFileItemElement() {
    mockk<CompositeDecoder> {
      every {
        decodeSerializableElement(FileItemKSerializer.descriptor, index = 0, FileItemKSerializer)
      } returns
        byteArrayOf(0).inputStream().asInput().use {
          PartData.FileItem({ it }, dispose = it::close, Headers.Empty)
        }
      decodeElement<PartData.FileItem>(FileItemKSerializer.descriptor, index = 0)
      verify(exactly = 1) {
        decodeSerializableElement(FileItemKSerializer.descriptor, index = 0, FileItemKSerializer)
      }
    }
  }

  @Test
  fun encodesFormItemElement() {
    mockk<CompositeDecoder> {
      every {
        decodeSerializableElement(FormItemKSerializer.descriptor, index = 0, FormItemKSerializer)
      } returns
        PartData.FormItem(
          "${HeaderValueParam("filename", "file.png")}",
          dispose = {},
          Headers.Empty
        )
      decodeElement<PartData.FormItem>(FormItemKSerializer.descriptor, index = 0)
      verify(exactly = 1) {
        decodeSerializableElement(FormItemKSerializer.descriptor, index = 0, FormItemKSerializer)
      }
    }
  }

  @Test
  fun decodesShortElement() {
    mockk<CompositeDecoder> {
      every { decodeShortElement(serialDescriptor<Short>(), index = 0) } returns 0
      decodeElement<Short>(serialDescriptor<Short>(), index = 0)
      verify(exactly = 1) { decodeShortElement(serialDescriptor<Short>(), index = 0) }
    }
  }

  @Test
  fun decodesStringValuesElement() {
    mockk<CompositeDecoder> {
      every {
        decodeSerializableElement(
          StringValues.serializer().descriptor,
          index = 0,
          StringValues.serializer()
        )
      } returns StringValues.Empty
      decodeElement<StringValues>(StringValues.serializer().descriptor, index = 0)
      verify(exactly = 1) {
        decodeSerializableElement(
          StringValues.serializer().descriptor,
          index = 0,
          StringValues.serializer()
        )
      }
    }
  }
}
