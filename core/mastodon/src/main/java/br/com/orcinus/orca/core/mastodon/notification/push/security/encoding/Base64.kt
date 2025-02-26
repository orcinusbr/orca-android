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

import android.util.Base64
import br.com.orcinus.orca.core.mastodon.notification.InternalNotificationApi

/** Flags for encoding/decoding a non-padded, unwrapped, URL-safe Base64. */
private const val FLAGS = Base64.NO_PADDING or Base64.NO_WRAP or Base64.URL_SAFE

/**
 * Encodes this [ByteArray] to a non-padded, unwrapped, URL-safe Base64 [String] by having each
 * character's code right-shifted two bits, which will produce a value whose decimal is then mapped
 * to the table specified by
 * [RFC 4648 § 4](https://datatracker.ietf.org/doc/html/rfc4648#section-4).
 *
 * @see Char.code
 * @see String.decodeFromBase64
 */
@InternalNotificationApi
internal fun ByteArray.encodeToBase64(): String = Base64.encodeToString(this, FLAGS)

/**
 * Decodes this non-padded, unwrapped, URL-safe Base64 [String].
 *
 * @see ByteArray.encodeToBase64
 */
@InternalNotificationApi
internal fun String.decodeFromBase64(): ByteArray = Base64.decode(this, FLAGS)
