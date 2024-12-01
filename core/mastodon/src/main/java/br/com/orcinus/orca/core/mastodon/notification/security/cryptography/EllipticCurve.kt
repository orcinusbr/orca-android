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

package br.com.orcinus.orca.core.mastodon.notification.security.cryptography

import java.security.KeyPairGenerator
import java.security.spec.ECGenParameterSpec

/** Utilities for elliptic-curve-based cryptography. */
internal object EllipticCurve {
  /**
   * Name of the algorithm as specified by the
   * [Java Security Standard Algorithm Names](https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names.html)
   * document.
   */
  const val NAME = "EC"

  /** [KeyPairGenerator] that generates a pair of secp256r1 (P-256) keys. */
  @JvmStatic
  val secp256r1KeyPairGenerator =
    KeyPairGenerator.getInstance(NAME).apply<KeyPairGenerator> {
      initialize(ECGenParameterSpec("secp256r1"))
    }
}
