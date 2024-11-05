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

package br.com.orcinus.orca.core.mastodon.notification.service.security

import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.core.mastodon.notification.service.security.encoding.encodeToBase64
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.ECPoint

/**
 * Generator of a public and an authentication key with which sent push subscriptions can be
 * encrypted and received updates from the Mastodon server can be decrypted, based on the
 * implementation of the
 * [`PushSubscriptionManager`](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java)
 * (PSM) of the official Android app.
 */
internal class Locksmith {
  /** Generated p256v1 (or secp256r1 — its alias) elliptic curve pair of keys. */
  private val keyPair by
    lazy<KeyPair> {
      KeyPairGenerator.getInstance(ELLIPTIC_CURVE)
        .apply<KeyPairGenerator> { initialize(ECGenParameterSpec("secp256r1")) }
        .generateKeyPair()
    }

  /**
   * Base64-encoded [String] consisting of three sequences of bytes: an
   * [UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER] and the x and y affine coordinates of the generated
   * p256v1 elliptic curve public key. The underlying algorithm is based on
   * [that of the official Mastodon Android app](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java#L135-L141).
   *
   * @see ByteArray.encodeToBase64
   */
  val publicKey by lazy {
    keyPair.public
      .let { (it as ECPublicKey).w as ECPoint }
      .let { arrayOf<BigInteger>(it.affineX, it.affineY) }
      .map { it.toPsmEllipticCurvePublicKeyAffineCoordinate() }
      .let { (x, y) -> byteArrayOf(UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER, *x, *y) }
      .encodeToBase64()
  }

  /**
   * Key that identifies this [Locksmith]. Its generation is based on
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
     * Name of the elliptic curve algorithm as specified by the
     * [Java Security Standard Algorithm Names](https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names.html)
     * document, used for generating the pair of keys containing both the public and the private
     * one.
     *
     * @see keyPair
     */
    private const val ELLIPTIC_CURVE = "EC"

    /**
     * Required amount of bytes in an affine coordinate of a [publicKey], same as the
     * [one defined in the official Mastodon Android app](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java#L236).
     * As the original implementation, the least significant 32 bytes of the coordinates are encoded
     * into the final [String], and left-zero-padded in case their sizes are lesser than this
     * predefined one.
     *
     * @see BigInteger.toPsmEllipticCurvePublicKeyAffineCoordinate
     */
    private const val PUBLIC_KEY_AFFINE_COORDINATE_PSM_SIZE = 32

    /**
     * Standardized by ["SEC 1: Elliptic Curve Cryptography"](https://www.secg.org/sec1-v2.pdf), it
     * is the leading byte of an elliptic curve key which denotes that it is uncompressed — that is,
     * the two bytes that follow are both its x and y affine coordinates.
     */
    @VisibleForTesting const val UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER: Byte = 0x04

    /** Converts this [BigInteger] into a 32-bit [publicKey] affine coordinate. */
    @JvmStatic
    private fun BigInteger.toPsmEllipticCurvePublicKeyAffineCoordinate(): ByteArray {
      val source = toByteArray()
      val destination = ByteArray(PUBLIC_KEY_AFFINE_COORDINATE_PSM_SIZE)
      val sourceOffset = maxOf(0, source.size - destination.size)
      val destinationOffset = maxOf(0, destination.size - source.size)
      val size = minOf(destination.size, source.size)
      System.arraycopy(source, sourceOffset, destination, destinationOffset, size)
      return destination
    }
  }
}
