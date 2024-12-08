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

package br.com.orcinus.orca.core.mastodon.notification.push.security.encoding

/**
 * Encode a [ByteArray] as a [String].
 *
 * @throws UnsatisfiedLinkError If the native library is unloaded.
 * @see String.decodeFromZ85
 */
@Throws(UnsatisfiedLinkError::class) external fun ByteArray.encodeToZ85(): String

/**
 * Decode an encoded [String] into a [ByteArray]; size of array will be `this.length` * 4 / 5.
 *
 * @throws UnsatisfiedLinkError If the native library is unloaded.
 * @see ByteArray.encodeToZ85
 */
@Throws(UnsatisfiedLinkError::class) external fun String.decodeFromZ85(): ByteArray
