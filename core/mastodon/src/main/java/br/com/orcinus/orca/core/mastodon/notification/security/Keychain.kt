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

package br.com.orcinus.orca.core.mastodon.notification.security

import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.core.mastodon.notification.security.encoding.encodeToBase64
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.ECPoint

/**
 * Generator of a public-private key pair and an authentication key with which sent push
 * subscriptions are encrypted and received updates from the Mastodon server are decrypted, based on
 * the implementation of both in the
 * [`PushNotificationManager`](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java)
 * of the official Android app.
 */
internal class Keychain {
  /**
   * Base64-encoded key consisting of three sequences of bytes: an
   * [UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER] and the x and y affine coordinates of the generated
   * p256v1 (or secp256r1 — its alias) elliptic curve. The underlying algorithm is based on
   * [that of the official Mastodon Android app](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java#L135-L141).
   *
   * @see ByteArray.encodeToBase64
   */
  val publicKey by lazy {
    KeyPairGenerator.getInstance("EC")
      .apply { initialize(ECGenParameterSpec("secp256r1")) }
      .generateKeyPair()
      .public
      .let { (it as ECPublicKey).w as ECPoint }
      .let { arrayOf<BigInteger>(it.affineX, it.affineY) }
      .map { it.toByteArray().size() }
      .let { (x, y) -> byteArrayOf(UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER, *x, *y) }
      .encodeToBase64()
  }

  /**
   * Key that identifies this [Keychain]. Its generation is based on
   * [that of the official Mastodon Android app](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java#L135-L144),
   * with the instantiation of a random 16-byte array encoded to a Base64 [String].
   *
   * @see ByteArray.encodeToBase64
   */
  val authenticationKey by lazy {
    ByteArray(size = 16).apply(SecureRandom()::nextBytes).encodeToBase64()
  }

  companion object {
    /**
     * Minimum and maximum amount of bytes in an affine coordinate of a [publicKey], same as the
     * [one defined in the official Mastodon Android app](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java#L236).
     * As the original implementation, the last 32 bytes of the coordinates are encoded into the
     * final [String], and left-zero-padded in case their sizes are lesser than this predefined one.
     *
     * @see ByteArray.pad
     */
    private const val PUBLIC_KEY_AFFINE_COORDINATE_SIZE = 32

    /**
     * Standardized by ["SEC 1: Elliptic Curve Cryptography"](https://www.secg.org/sec1-v2.pdf), it
     * is the leading byte of an elliptic curve key which denotes that it is uncompressed — that is,
     * the two bytes that follow are both its x and y affine coordinates.
     */
    @VisibleForTesting const val UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER: Byte = 0x04

    /**
     * Creates a copy of this array with the size of a [publicKey] affine coordinate or returns
     * itself in case it is already a 32-byte one.
     *
     * @see PUBLIC_KEY_AFFINE_COORDINATE_SIZE
     */
    private fun ByteArray.size(): ByteArray {
      return when {
        size == PUBLIC_KEY_AFFINE_COORDINATE_SIZE -> this
        size < PUBLIC_KEY_AFFINE_COORDINATE_SIZE -> pad()
        else -> trim()
      }
    }

    /**
     * Creates a copy of this [ByteArray] which consists of an initial padding — whose length is the
     * predefined size for a [publicKey] coordinate minus this one's size — and its content.
     * Essentially, converts it into an array with 32 bytes.
     *
     * @see PUBLIC_KEY_AFFINE_COORDINATE_SIZE
     */
    @JvmStatic
    private fun ByteArray.pad(): ByteArray {
      return ByteArray(PUBLIC_KEY_AFFINE_COORDINATE_SIZE - size) + this
    }

    /**
     * Creates a copy of this [ByteArray] consisting of its last bytes, whose size is that of a
     * [publicKey]'s affine coordinate. Similarly to [pad], also converts the receiver into an array
     * of 32 bytes.
     *
     * @see PUBLIC_KEY_AFFINE_COORDINATE_SIZE
     */
    @JvmStatic
    private fun ByteArray.trim(): ByteArray {
      return ByteArray(PUBLIC_KEY_AFFINE_COORDINATE_SIZE) { get(lastIndex - it) }
    }
  }
}
