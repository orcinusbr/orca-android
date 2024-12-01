/*
 * Copyright (c) 2010-2013 iMatix Corporation and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: The above copyright
 * notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * ================================================================================================
 *
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see https://www.gnu.org/licenses.
 */

#include <assert.h>
#include <jni.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
 * Reference implementation for https://rfc.zeromq.org/spec:32/Z85.
 *
 * This implementation provides a Z85 codec as an easy-to-reuse C class designed to be easy to port
 * into other languages.
 */

/*
 * Orca-specific changes:
 *
 * -    Corrected comments' grammar and turned consecutive into block ones;
 * -    Reformatted with clang-format;
 * -    Removed main method (containing an encoding and a decoding demo);
 * -    Removed streq macro;
 * -    Turn Z85_encode(byte *, size_t) and Z85_decode(char *) into JNI methods.
 */

// Basic language taken from CZMQ's prelude.
typedef unsigned char byte;

// Maps Base256 to Base85.
static char encoder[85 + 1] = {
  "0123456789"
  "abcdefghij"
  "klmnopqrst"
  "uvwxyzABCD"
  "EFGHIJKLMN"
  "OPQRSTUVWX"
  "YZ.-:+=^!/"
  "*?&<>()[]{"
  "}@%$#"
};

/*
 * Maps Base85 to Base256.
 * We chop off lower 32 and higher 128 ranges.
 */
static byte decoder[96] = {
  0x00, 0x44, 0x00, 0x54, 0x53, 0x52, 0x48, 0x00, 0x4B, 0x4C, 0x46, 0x41, 0x00, 0x3F, 0x3E, 0x45,
  0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x40, 0x00, 0x49, 0x42, 0x4A, 0x47,
  0x51, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x30, 0x31, 0x32,
  0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x4D, 0x00, 0x4E, 0x43, 0x00,
  0x00, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18,
  0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x20, 0x21, 0x22, 0x23, 0x4F, 0x00, 0x50, 0x00, 0x00
};

// Encode a byte array as a string.
JNIEXPORT jstring JNICALL
Java_br_com_orcinus_orca_core_mastodon_notification_security_encoding_Z85Kt_encodeToZ85(
  __attribute__((unused)) JNIEnv *env,
  __attribute__((unused)) jclass clazz,
  jbyteArray _receiver
) {
  jsize size = (*env)->GetArrayLength(env, _receiver);
  jbyte *data = (*env)->GetByteArrayElements(env, _receiver, NULL);

  // Accepts only byte arrays bounded to 4 bytes
  if (size % 4) return NULL;

  jsize encoded_size = size * 5 / 4;
  jchar *encoded = malloc(encoded_size + 1);
  uint char_nbr = 0;
  uint byte_nbr = 0;
  uint32_t value = 0;
  while (byte_nbr < size) {
    // Accumulate value in Base356 (binary).
    value = value * 256 + data[byte_nbr++];
    if (byte_nbr % 4 == 0) {
      // Output value in Base85.
      uint divisor = 85 * 85 * 85 * 85;
      while (divisor) {
        encoded[char_nbr++] = (jchar)encoder[value / divisor % 85];
        divisor /= 85;
      }
      value = 0;
    }
  }
  assert(char_nbr == encoded_size);
  encoded[char_nbr] = 0;
  (*env)->ReleaseByteArrayElements(env, _receiver, data, 0);
  return (*env)->NewString(env, encoded, encoded_size);
}

// Decode an encoded string into a byte array; size of array will be strlen (string) * 4 / 5.
JNIEXPORT jbyteArray JNICALL
Java_br_com_orcinus_orca_core_mastodon_notification_security_encoding_Z85Kt_decodeFromZ85(
  __attribute__((unused)) JNIEnv *env,
  __attribute__((unused)) jclass clazz,
  jstring _receiver
) {
  const char *string = (*env)->GetStringUTFChars(env, _receiver, NULL);
  jsize string_length = (*env)->GetStringLength(env, _receiver);

  // Accepts only strings bounded to 5 bytes.
  if (string_length % 5) return NULL;

  jsize decoded_size = string_length * 4 / 5;
  jbyteArray j_decoded = (*env)->NewByteArray(env, decoded_size);
  jbyte *decoded = (*env)->GetByteArrayElements(env, j_decoded, NULL);
  uint byte_nbr = 0;
  uint char_nbr = 0;
  uint32_t value = 0;
  while (char_nbr < strlen(string)) {
    // Accumulate value in Base85.
    value = value * 85 + decoder[(byte)string[char_nbr++] - 32];
    if (char_nbr % 5 == 0) {
      // Output value in Base256.
      uint divisor = 256 * 256 * 256;
      while (divisor) {
        decoded[byte_nbr++] = (jbyte)(value / divisor % 256);
        divisor /= 256;
      }
      value = 0;
    }
  }
  assert(byte_nbr == decoded_size);
  (*env)->ReleaseByteArrayElements(env, j_decoded, decoded, 0);
  (*env)->ReleaseStringUTFChars(env, _receiver, string);
  return j_decoded;
}
