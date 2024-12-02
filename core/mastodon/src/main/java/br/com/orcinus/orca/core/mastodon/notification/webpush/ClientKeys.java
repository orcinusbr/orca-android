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

package br.com.orcinus.orca.core.mastodon.notification.webpush;

import androidx.annotation.NonNull;
import br.com.orcinus.orca.core.mastodon.notification.security.cryptography.EllipticCurve;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;

/** Manages generation and refresh of client-side keys. */
class ClientKeys {
  /** 128-bit random key for identifying a request. */
  private byte[] authentication;

  /** P-256 elliptic curve private key. */
  private ECPrivateKey privateKey;

  /** P-256 elliptic curve public key. */
  private ECPublicKey publicKey;

  /** Obtains an 128-bit random key for identifying a request. */
  @NonNull
  byte[] getAuthentication() {
    return authentication;
  }

  /** Obtains a P-256 elliptic curve private key. */
  @NonNull
  ECPrivateKey getPrivate() {
    return privateKey;
  }

  /** Obtains a P-256 elliptic curve public key. */
  @NonNull
  ECPublicKey getPublic() {
    return publicKey;
  }

  /** Manages generation and refresh of client-side keys. */
  ClientKeys() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
    generate();
  }

  /** Generates authentication and public keys distinct from the current ones. */
  void generate() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
    final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(EllipticCurve.NAME);
    keyPairGenerator.initialize(new ECGenParameterSpec("secp256r1"));
    final KeyPair keyPair = keyPairGenerator.generateKeyPair();
    authentication = new byte[16];
    new SecureRandom().nextBytes(authentication);
    privateKey = (ECPrivateKey) keyPair.getPrivate();
    publicKey = (ECPublicKey) keyPair.getPublic();
  }
}
