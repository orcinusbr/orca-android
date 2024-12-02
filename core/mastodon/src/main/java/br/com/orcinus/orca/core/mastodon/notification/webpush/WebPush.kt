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

package br.com.orcinus.orca.core.mastodon.notification.webpush

import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.core.mastodon.notification.InternalNotificationApi
import br.com.orcinus.orca.core.mastodon.notification.security.cryptography.EllipticCurve
import br.com.orcinus.orca.core.mastodon.notification.security.encoding.encodeToBase64
import java.math.BigInteger
import java.security.KeyFactory
import java.security.interfaces.ECPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.Mac
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Generator of a public and an authentication keys and shared secrets with which sent push
 * subscriptions can be encrypted and received updates from the Mastodon server can be decrypted,
 * based on the implementation of the
 * [`PushSubscriptionManager`](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java)
 * (PSM) of the official Android app.
 */
@InternalNotificationApi
internal class WebPush {
  /** Client-generated keys. */
  private val clientKeys = ClientKeys()

  /**
   * Key provided by the Mastodon server the last time a shared secret was generated.
   *
   * @see sharedSecret
   * @see generateSharedSecret
   */
  private lateinit var serverKey: ECPublicKey

  /**
   * Shared secret that has already been generated from the private elliptic curve key and one
   * provided by the Mastodon server. Is stored so that it can be later returned by
   * [generateSharedSecret] in case it gets called multiple times, preventing the key from being
   * generated more than once.
   *
   * @see serverKey
   */
  private lateinit var sharedSecret: ByteArray

  /**
   * Base64-encoded string consisting of three sequences of bytes: an uncompressed marker and the x
   * and y affine coordinates of the generated, uncompressed p256v1 elliptic curve public key. The
   * generation algorithm is based on
   * [that of the official Mastodon Android app](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java#L135-L141).
   *
   * @see ByteArray.encodeToBase64
   * @see UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER
   */
  val base64EncodedClientPublicKey
    get() = clientKeys.public.decompress().encodeToBase64()

  /**
   * Base64-encoded 16-bit- authentication key containing random bytes. Its generation is based on
   * [that of the official Mastodon Android app](https://github.com/mastodon/mastodon-android/blob/1ad2d08e2722dc812320708ddd43738209c12d5f/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java#L135-L144).
   *
   * @see ByteArray.encodeToBase64
   */
  val base64EncodedAuthenticationKey
    get() = clientKeys.authentication.encodeToBase64()

  /**
   * Base class of hash-based key derivation functions (HKDFs) for deriving either an AES/GCM
   * 128-bit key or a nonce; these are then ciphered for decrypting plaintext received from the
   * Mastodon server. Such decryption process occurs from the following derivations in the given
   * order, described as "_initial salt_ and _intermediate salt_ → _derived key_":
   * 1. Authentication key A and shared secret B → AES/GCM 128-bit key Dₐ;
   * 2. Mastodon server key salt C and Dₐ → AES/GCM 128-bit key Dₑ;
   * 3. A and B → 256-bit key Dₕ; and
   * 4. C and Dₕ → AES/GCM 256-bit nonce.
   *
   * @see AesGcm128
   * @see Nonce
   * @see WebPush.decrypt
   * @see Nonce.Auth
   * @see initialSalt
   * @see intermediateSalt
   * @see invoke
   */
  private abstract inner class Hkdf {
    /**
     * Information that serves as a parameter for HKDF and allows for secure reuse of B alongside a
     * salt. For 128-bit AES/GCM key and nonce HKDFs, consists of a Content-Encoding string
     * ("Content-Encoding: " + ID, UTF-8-encoded), the client public key and the Mastodon server
     * one; as for the auth context, only a null-terminated Content-Encoding string is included.
     *
     * Its structure is PSM-specific in non-auth HKDFs and, as such, differs from those described in
     * RFCs [8188](https://www.rfc-editor.org/rfc/rfc8188.html#section-2.2) and
     * [8291](https://datatracker.ietf.org/doc/html/rfc8291#section-3.3): starts with a
     * Content-Encoding string and another one characterizing the elliptic curve algorithm
     * (secp256r1, P-256) — separated by two null bytes from the keys that follow — and prepends to
     * both keys their size. That of these HKDFs **must** be exclusively instantiated by calling
     * [generateKeyedInfo].
     *
     * @see AesGcm128
     * @see Nonce
     * @see Nonce.Auth
     * @see UNCOMPRESSED_PUBLIC_KEY_SIZE
     */
    protected abstract val info: ByteArray

    /**
     * Salt from which an intermediate HMAC key is generated.
     *
     * @see intermediateSalt
     */
    protected abstract val initialSalt: ByteArray

    /**
     * Salt from which an intermediate HMAC key is generated.
     *
     * @see initialSalt
     */
    protected abstract val intermediateSalt: ByteArray

    /**
     * Maximum amount of bytes in a derived key.
     *
     * @see invoke
     */
    protected abstract val maxSize: Int

    /** Performs key derivation. */
    operator fun invoke(): ByteArray {
      val initialKeySpec = SecretKeySpec(initialSalt, mac.algorithm)
      val intermediateKey = mac.doFinal(intermediateSalt)
      val intermediateKeySpec = SecretKeySpec(intermediateKey, mac.algorithm)
      mac.init(initialKeySpec)
      mac.init(intermediateKeySpec)
      mac.update(info)
      var derivedKey = mac.doFinal(byteArrayOf(1))
      if (derivedKey.size > maxSize) {
        derivedKey = derivedKey.copyOf(maxSize)
      }
      mac.reset()
      return derivedKey
    }

    /**
     * Generates an array of bytes containing the data described in [info]'s documentation regarding
     * non-auth HKDFs (i. e., 128-bit AES/GCM and nonce ones). Should be called exclusively by them
     * and the produced value should be assigned to their [info] without any modifications.
     *
     * @param id Identifies the context in the Content-Encoding string.
     * @see Nonce.Auth
     * @see AesGcm128
     * @see Nonce
     */
    protected fun generateKeyedInfo(id: String) =
      byteArrayOf(
        *"Content-Encoding: $id".toByteArray(),
        0,
        *"P-256".toByteArray(),
        0,
        0,
        UNCOMPRESSED_PUBLIC_KEY_SIZE,
        *clientKeys.public.decompress(),
        0,
        UNCOMPRESSED_PUBLIC_KEY_SIZE,
        *serverKey.decompress()
      )
  }

  /**
   * HKDF for deriving both a key with which decryption of a plaintext is to be performed and its
   * respective salt. Is invoked after the one that derives the nonce (the key based on which the
   * previous one is generated).
   *
   * @see invoke
   * @see Nonce
   */
  private inner class AesGcm128(
    override val initialSalt: ByteArray,
    override val intermediateSalt: ByteArray
  ) : Hkdf() {
    override val info = generateKeyedInfo("aesgcm128")
    override val maxSize = 16
  }

  /**
   * HKDF for producing an AES/GCM nonce (a numeric value to be used only once), which prevents
   * replay or reordering attacks by allowing for the provision of a distinct decryption key each
   * time a derivation occurs.
   *
   * @param serverKeySalt Salt from which the [serverKey] was generated.
   */
  private inner class Nonce(serverKeySalt: ByteArray) : Hkdf() {
    /** Non-standard HKDF whose derived key is the [intermediateSalt] of a nonce. */
    private inner class Auth : Hkdf() {
      override val info = "Content-Encoding: auth\u0000".toByteArray()
      override val maxSize = 32
      override val initialSalt = clientKeys.authentication
      override val intermediateSalt = sharedSecret
    }

    override val info = generateKeyedInfo("nonce")
    override val maxSize = 12
    override val initialSalt = serverKeySalt
    override val intermediateSalt = Auth()()
  }

  /**
   * Decrypts an AES/GCM-encrypted plaintext.
   *
   * @param serverKey Key provided by the Mastodon server.
   * @param serverKeySalt Salt from which the [serverKey] was generated.
   * @param plaintext Content to be decrypted, received from the Mastodon server.
   */
  fun decrypt(serverKey: ECPublicKey, serverKeySalt: ByteArray, plaintext: ByteArray): String {
    val sharedSecret = generateSharedSecret(serverKey)
    val decryptionKeyIntermediateSalt = AesGcm128(clientKeys.authentication, sharedSecret)()
    val decryptionKey = AesGcm128(serverKeySalt, decryptionKeyIntermediateSalt)()
    val decryptionKeySpec = SecretKeySpec(decryptionKey, "AES")
    val nonce = Nonce(serverKeySalt)()
    val nonceSpec = GCMParameterSpec(/* tLen = */ 128, nonce)
    cipher.init(Cipher.DECRYPT_MODE, decryptionKeySpec, nonceSpec)
    val decryptedPlaintext = cipher.doFinal(plaintext)
    clientKeys.generate()
    return String(plaintext, offset = 2, length = decryptedPlaintext.size - 2)
  }

  /**
   * Generates a key from the client-side, private one and another one provided by the Mastodon
   * server. In essence, the first is multiplied by the second, which results in a point on the
   * elliptic curve; the returned [ByteArray] is then derived from that point's coordinates.
   *
   * Calling this method more than once with a [serverKey] that equals to the previous will not
   * generate a key each time; rather, the initial one is guaranteed to be returned.
   *
   * @param serverKey Key provided by the Mastodon server.
   */
  private fun generateSharedSecret(serverKey: ECPublicKey): ByteArray {
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
   * one and, thus, can be responded with the key that has already been generated. Returns `false`
   * if this is the first time the request is performed.
   *
   * @param serverKey Key provided by the Mastodon server.
   */
  private fun hasAlreadyGeneratedSharedSecretFor(serverKey: ECPublicKey): Boolean {
    return ::sharedSecret.isInitialized &&
      this.serverKey.encoded?.contentEquals(serverKey.encoded) ?: false
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
   * @param serverKey Key provided by the Mastodon server.
   */
  private fun generateSharedSecretEagerly(serverKey: ECPublicKey): ByteArray {
    return PKCS8EncodedKeySpec(clientKeys.private.encoded)
      .let { KeyFactory.getInstance(EllipticCurve.NAME).generatePrivate(it) }
      .let { KeyAgreement.getInstance("ECDH").apply { init(it) } }
      .also { it.doPhase(serverKey, /* lastPhase = */ true) }
      .generateSecret()
  }

  companion object {
    /**
     * Required amount of bytes in an affine coordinate of an uncompressed public key. As the PSM
     * implementation, the least significant 32 bytes of the coordinates are encoded into the final
     * [String], and left-zero-padded in case their sizes are lesser than this predefined one.
     *
     * @see BigInteger.toPsmEllipticCurveUncompressedPublicKeyAffineCoordinate
     */
    private const val UNCOMPRESSED_PUBLIC_KEY_AFFINE_COORDINATE_SIZE = 32

    /**
     * Length of a public key; includes the leading marker and both affine coordinates' sizes.
     *
     * @see UNCOMPRESSED_PUBLIC_KEY_AFFINE_COORDINATE_SIZE
     * @see UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER
     */
    private const val UNCOMPRESSED_PUBLIC_KEY_SIZE =
      (UNCOMPRESSED_PUBLIC_KEY_AFFINE_COORDINATE_SIZE * 2 + 1).toByte()

    /** [Cipher] by which plaintext decryption is performed. */
    @JvmStatic private val cipher: Cipher = Cipher.getInstance("AES/GCM/NoPadding")

    /**
     * SHA256 hash-based message authentication code (HMAC) generator for HKDFs.
     *
     * @see Hkdf
     */
    @JvmStatic private val mac: Mac = Mac.getInstance("HmacSHA256")

    /**
     * Standardized by ["SEC 1: Elliptic Curve Cryptography"](https://www.secg.org/sec1-v2.pdf), it
     * is the leading byte of an elliptic curve key which denotes that it is uncompressed — that is,
     * the two bytes that follow are both its x and y affine coordinates.
     */
    @VisibleForTesting const val UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER: Byte = 0x04

    /**
     * Decompresses this key by creating an array of bytes with a marker and both affine
     * coordinates' least significant 32 bits.
     *
     * @see UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER
     * @see BigInteger.toPsmEllipticCurveUncompressedPublicKeyAffineCoordinate
     */
    @JvmStatic
    private fun ECPublicKey.decompress() =
      byteArrayOf(
        UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER,
        *(w?.affineX?.toPsmEllipticCurveUncompressedPublicKeyAffineCoordinate() ?: byteArrayOf()),
        *(w?.affineY?.toPsmEllipticCurveUncompressedPublicKeyAffineCoordinate() ?: byteArrayOf())
      )

    /** Converts this [BigInteger] into a 32-bit uncompressed public key affine coordinate. */
    @JvmStatic
    private fun BigInteger.toPsmEllipticCurveUncompressedPublicKeyAffineCoordinate(): ByteArray {
      val source = toByteArray()
      val destination = ByteArray(UNCOMPRESSED_PUBLIC_KEY_AFFINE_COORDINATE_SIZE)
      val sourceOffset = maxOf(0, source.size - destination.size)
      val destinationOffset = maxOf(0, destination.size - source.size)
      val size = minOf(destination.size, source.size)
      System.arraycopy(source, sourceOffset, destination, destinationOffset, size)
      return destination
    }
  }
}
