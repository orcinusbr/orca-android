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
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.ECPoint
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.KeyAgreement

/**
 * Generator of a public and an authentication key alongside a shared secret with which sent push
 * subscriptions can be encrypted and received updates from the Mastodon server can be decrypted,
 * based on the implementation of the
 * [`PushNotificationManager`](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java)
 * (PNM) of the official Android app.
 */
internal class Locksmith {
  /** Generated p256v1 (or secp256r1 — its alias) elliptic curve pair of keys. */
  private val keyPair by lazy<KeyPair>(keyPairGenerator::generateKeyPair)

  /**
   * [Key] provided by the Mastodon server the last time a shared secret was generated.
   *
   * @see sharedSecret
   * @see generateSharedSecret
   */
  private lateinit var serverKey: Key

  /**
   * Shared secret that has already been generated from the private elliptic curve key and one
   * provided by the Mastodon server. Is stored so that it can be later returned by
   * [generateSharedSecret] in case it gets called multiple times, preventing the key from being
   * generated more than once.
   */
  private lateinit var sharedSecret: ByteArray

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
      .map { it.toByteArray().sizeAsPnmEllipticCurveKeyAffineCoordinate() }
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

  /**
   * Generates a key from the client-side, private one and another one provided by the Mastodon
   * server. In essence, the first is multiplied by the second, which results in a point on the
   * elliptic curve; the returned [ByteArray] is then derived from that point's coordinates.
   *
   * Calling this method more than once with a [serverKey] that equals to the previous will not
   * generate a distinct key each time; rather, the initial one is guaranteed to be returned.
   *
   * @param serverKey [Key] provided by the Mastodon server.
   */
  fun generateSharedSecret(serverKey: Key): ByteArray {
    return if (hasAlreadyGeneratedSharedSecretFor(serverKey)) {
      sharedSecret
    } else {
      this.serverKey = serverKey
      sharedSecret = generateSharedSecretEagerly(serverKey)
      sharedSecret
    }
  }

  /**
   * Determines whether the previous request for generating a shared secret is equal to the current
   * one and, thus, can be responded to with the key that has already been generated. Returns
   * `false` if this is the first time the request is performed.
   *
   * @param serverKey [Key] provided by the Mastodon server.
   */
  private fun hasAlreadyGeneratedSharedSecretFor(serverKey: Key): Boolean {
    return ::sharedSecret.isInitialized &&
      /*
       * Locksmith's serverKey is implied to have already been initialized (as it, in fact, has
       * been, by generateSharedSecret(Key)) if this expression is evaluated. Given that it is
       * strictly necessary for it to be storing a value at this point, such "is initialized" check
       * can be safely skipped here.
       */
      this.serverKey.encoded.contentEquals(serverKey.encoded)
  }

  /**
   * Generates a key from the client-side, private one and another one provided by the Mastodon
   * server. In essence, the first is multiplied by the second, which results in a point on the
   * elliptic curve; the returned [ByteArray] is then derived from that point's coordinates.
   *
   * This method differs from [generateSharedSecret] in that multiple calls to it **do** generate
   * keys each time (even if the given [serverKey] equals to the previous one that was provided to
   * it).
   *
   * @param serverKey [Key] provided by the Mastodon server.
   */
  private fun generateSharedSecretEagerly(serverKey: Key): ByteArray {
    return PKCS8EncodedKeySpec((keyPair.private as ECPrivateKey).encoded)
      .let { (KeyFactory.getInstance(ELLIPTIC_CURVE) as KeyFactory).generatePrivate(it) }
      .let { (KeyAgreement.getInstance("ECDH") as KeyAgreement).apply { init(it) } }
      .also { it.doPhase(serverKey, /* lastPhase = */ true) }
      .generateSecret()
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
     * Minimum and maximum amount of bytes in an affine coordinate of a [publicKey], same as the
     * [one defined in the official Mastodon Android app](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java#L236).
     * As the original implementation, the last 32 bytes of the coordinates are encoded into the
     * final [String], and left-zero-padded in case their sizes are lesser than this predefined one.
     *
     * @see ByteArray.padAsPnmEllipticCurveKeyAffineCoordinate
     */
    private const val PUBLIC_KEY_AFFINE_COORDINATE_PNM_SIZE = 32

    /**
     * Standardized by ["SEC 1: Elliptic Curve Cryptography"](https://www.secg.org/sec1-v2.pdf), it
     * is the leading byte of an elliptic curve key which denotes that it is uncompressed — that is,
     * the two bytes that follow are both its x and y affine coordinates.
     */
    @VisibleForTesting const val UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER: Byte = 0x04

    /** [KeyPairGenerator] by which the [keyPair] is generated. */
    @VisibleForTesting
    val keyPairGenerator =
      KeyPairGenerator.getInstance(ELLIPTIC_CURVE).apply<KeyPairGenerator> {
        initialize(ECGenParameterSpec("secp256r1"))
      }

    /**
     * Creates a copy of this array with the size of a [publicKey] affine coordinate or returns
     * itself in case it already is a 32-byte one.
     *
     * @see PUBLIC_KEY_AFFINE_COORDINATE_PNM_SIZE
     */
    @JvmStatic
    private fun ByteArray.sizeAsPnmEllipticCurveKeyAffineCoordinate(): ByteArray {
      return when {
        size == PUBLIC_KEY_AFFINE_COORDINATE_PNM_SIZE -> this
        size < PUBLIC_KEY_AFFINE_COORDINATE_PNM_SIZE -> padAsPnmEllipticCurveKeyAffineCoordinate()
        else -> trimAsPnmEllipticCurveKeyAffineCoordinate()
      }
    }

    /**
     * Creates a copy of this [ByteArray] which consists of an initial padding — whose length is the
     * predefined size for a [publicKey] coordinate minus this one's size — and its content.
     * Essentially, converts it into an array with 32 bytes.
     *
     * @see PUBLIC_KEY_AFFINE_COORDINATE_PNM_SIZE
     */
    @JvmStatic
    private fun ByteArray.padAsPnmEllipticCurveKeyAffineCoordinate(): ByteArray {
      return ByteArray(PUBLIC_KEY_AFFINE_COORDINATE_PNM_SIZE - size) + this
    }

    /**
     * Creates a copy of this [ByteArray] consisting of its last bytes, whose size is that of a
     * [publicKey]'s affine coordinate. Similarly to [padAsPnmEllipticCurveKeyAffineCoordinate],
     * also converts the receiver into an array of 32 bytes.
     *
     * @see PUBLIC_KEY_AFFINE_COORDINATE_PNM_SIZE
     */
    @JvmStatic
    private fun ByteArray.trimAsPnmEllipticCurveKeyAffineCoordinate(): ByteArray {
      return ByteArray(PUBLIC_KEY_AFFINE_COORDINATE_PNM_SIZE) { get(lastIndex - it) }
    }
  }
}
