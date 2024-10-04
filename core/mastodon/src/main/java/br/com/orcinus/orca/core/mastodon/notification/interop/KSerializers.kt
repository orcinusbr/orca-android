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

package br.com.orcinus.orca.core.mastodon.notification.interop

import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer

/**
 * Utility class that provides Java access to [KSerializer]s originally accessible only from Kotlin
 * code.
 */
internal object KSerializers {
  /** Obtains the [KSerializer] for serializing and deserializing a [MastodonAccount]. */
  @JvmStatic
  fun mastodonAccount(): KSerializer<MastodonAccount> {
    return MastodonAccount.serializer()
  }

  /** Obtains the [KSerializer] for serializing and deserializing a [MastodonStatus]. */
  @JvmStatic
  fun mastodonStatus(): KSerializer<MastodonStatus> {
    return MastodonStatus.serializer()
  }

  /** Obtains the [KSerializer] for serializing and deserializing a [String]. */
  @JvmStatic
  fun string(): KSerializer<String> {
    return String.serializer()
  }
}
