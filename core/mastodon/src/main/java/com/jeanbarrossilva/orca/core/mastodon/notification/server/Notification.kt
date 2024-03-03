/*
 * Copyright © 2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.mastodon.notification.server

import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API when the user is notified of an occurrence related to them.
 *
 * @param id Unique identifier in the database.
 * @param type Defines what it is about.
 * @param createdAt Moment in time at which notification has been performed.
 * @param account [MastodonAccount] responsible for the event that resulted in this [Notification]
 *   being sent.
 * @param status [MastodonStatus] to which this [Notification] is related. Only present when the
 *   [type] is [Type.FAVOURITE], [Type.MENTION], [Type.POLL], [Type.REBLOG], [Type.STATUS] or
 *   [Type.UPDATE].
 */
@Serializable
internal data class Notification(
  val id: String,
  val type: Type,
  val createdAt: String,
  val account: MastodonAccount,
  val status: MastodonStatus?
) {
  /** Denotes the topic of a [Notification]. */
  enum class Type {
    /** Someone has favorited a [MastodonStatus] authored by the user. */
    FAVOURITE,

    /** Someone has followed the user. */
    FOLLOW,

    /** Someone has requested to follow the user. */
    FOLLOW_REQUEST,

    /** Someone has mentioned the user. */
    MENTION,

    /** A poll that has either been created or voted in by the user has come to an end. */
    POLL,

    /** Some has reblogged a [MastodonStatus] authored by the user. */
    REBLOG,

    /** Someone the user follows has been severed, either because of moderation or blocking. */
    SEVERED_RELATIONSHIPS,

    /** Someone for whose [MastodonStatus]es the user chose to be notified about has posted. */
    STATUS,

    /** A [MastodonStatus] with which the user has interacted has been edited. */
    UPDATE
  }
}
