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

package br.com.orcinus.orca.core.mastodon.notification.service.security.encoding

import br.com.orcinus.orca.core.mastodon.notification.InternalNotificationApi
import java.io.ByteArrayOutputStream

/**
 * Code that denotes that a character is not Base85-decodable.
 *
 * @see Char.code
 */
private const val UNDECODABLE = 0xFF.toByte()

/** Amount of bytes contained in a Base85 block. */
private const val BLOCK_SIZE = 4.toByte()

/**
 * Codes of ASCII printable characters. "Constrained" because Mastodon's official app's Base85
 * decoding considers only the first 85 ones.
 *
 * @see Char.code
 */
private val constrainedPrintableAsciiCharacterCodes = 0x20.toByte()..0x7F.toShort()

/**
 * Codes of the characters used for Base85 decoding, based on the
 * [one from the official Mastodon Android app](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java#L60).
 *
 * @see Char.code
 * @see String.decodeFromBase85
 */
private val table =
  byteArrayOf(
    UNDECODABLE,
    0x44,
    UNDECODABLE,
    0x54,
    0x53,
    0x52,
    0x48,
    UNDECODABLE,
    0x4B,
    0x4C,
    0x46,
    0x41,
    UNDECODABLE,
    0x3F,
    0x3E,
    0x45,
    0x00,
    0x01,
    0x02,
    0x03,
    0x04,
    0x05,
    0x06,
    0x07,
    0x08,
    0x09,
    0x40,
    UNDECODABLE,
    0x49,
    0x42,
    0x4A,
    0x47,
    0x51,
    0x24,
    0x25,
    0x26,
    0x27,
    0x28,
    0x29,
    0x2A,
    0x2B,
    0x2C,
    0x2D,
    0x2E,
    0x2F,
    0x30,
    0x31,
    0x32,
    0x33,
    0x34,
    0x35,
    0x36,
    0x37,
    0x38,
    0x39,
    0x3A,
    0x3B,
    0x3C,
    0x3D,
    0x4D,
    UNDECODABLE,
    0x4E,
    0x43,
    UNDECODABLE,
    UNDECODABLE,
    0x0A,
    0x0B,
    0x0C,
    0x0D,
    0x0E,
    0x0F,
    0x10,
    0x11,
    0x12,
    0x13,
    0x14,
    0x15,
    0x16,
    0x17,
    0x18,
    0x19,
    0x1A,
    0x1B,
    0x1C,
    0x1D,
    0x1E,
    0x1F,
    0x20,
    0x21,
    0x22,
    0x23,
    0x4F,
    UNDECODABLE,
    0x50,
    UNDECODABLE,
    UNDECODABLE
  )

/**
 * Returns whether this character has an equivalent code on the Mastodon-specific Base85 [table].
 *
 * @see Char.code
 */
private val Char.isDecodable
  get() = isConstrainedPrintableAscii && decode() != UNDECODABLE

/**
 * Returns whether this is a constrained printable ASCII character.
 *
 * For an explanation on the constraining aspect, refer to
 * [constrainedPrintableAsciiCharacterCodes]'s and [CharSequence.decodeFromBase85]'s documentation.
 */
private val Char.isConstrainedPrintableAscii
  get() = code in constrainedPrintableAsciiCharacterCodes

/**
 * Decodes this Base85-encoded [CharSequence].
 *
 * As per [Wikipedia](https://en.wikipedia.org/wiki/Ascii85):
 *
 * "Ascii85, also called Base85, is a form of binary-to-text encoding developed by Paul E. Rutter
 * for the btoa utility. By using five ASCII characters to represent four bytes of binary data
 * (making the encoded size ¼ larger than the original, assuming eight bits per ASCII character), it
 * is more efficient than uuencode or Base64, which use four characters to represent three bytes of
 * data (⅓ increase, assuming eight bits per ASCII character)."
 *
 * Note that the performed Base85 decoding is Mastodon-specific, since it is based on
 * [the one in the official Android app](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java#L251),
 * which iterates from characters 0x00 (NULL) to 0x7F (DEL) instead of from 0x21 (!) to 0x75 (u).
 *
 * @see table
 */
@InternalNotificationApi
internal fun CharSequence.decodeFromBase85(): ByteArray {
  val outputStream = ByteArrayOutputStream(/* size = */ BLOCK_SIZE * (length / BLOCK_SIZE.dec()))
  var block = 0
  var blockSize = 0
  for (decodableChar in filter(Char::isDecodable)) {
    block = block * 85 + decodableChar.decode()
    blockSize++
    if (blockSize > BLOCK_SIZE) {
      repeat(BLOCK_SIZE.toInt()) { outputStream.write(block shr (8 * (3 - it))) }
      block = 0
      blockSize = 0
    }
  }
  if (blockSize >= BLOCK_SIZE / 2) {
    repeat((BLOCK_SIZE / 2).inc()) { outputStream.write(block shr 8 * (2 - it)) }
  }
  return outputStream.toByteArray()
}

/**
 * Decodes this Mastodon-specific Base85 (described in [CharSequence.decodeFromBase85]) character.
 */
private fun Char.decode(): Byte {
  return table[code - constrainedPrintableAsciiCharacterCodes.first]
}
