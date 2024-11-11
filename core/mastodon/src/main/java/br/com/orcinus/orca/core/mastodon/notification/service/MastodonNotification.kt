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

package br.com.orcinus.orca.core.mastodon.notification.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.R
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import br.com.orcinus.orca.core.mastodon.notification.InternalNotificationApi
import br.com.orcinus.orca.core.mastodon.notification.service.MastodonNotification.Type
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API when the user is notified of an occurrence related to them.
 *
 * @property id Unique identifier in the database.
 * @property type Determines what this [MastodonNotification] is about.
 * @property createdAt ISO-8601-formatted moment in time at which the notification was delivered.
 * @property account [MastodonAccount] responsible for the event that resulted in this
 *   [MastodonNotification] being sent.
 * @property status [MastodonStatus] to which this [MastodonNotification] is related. Only present
 *   when the [type] is [Type.FAVOURITE], [Type.MENTION], [Type.POLL], [Type.REBLOG], [Type.STATUS]
 *   or [Type.UPDATE].
 */
@InternalNotificationApi
@Serializable
internal data class MastodonNotification(
  val id: String,
  val type: Type,
  val createdAt: String,
  val account: MastodonAccount?,
  val status: MastodonStatus?
) {
  /** Denotes the topic of a [MastodonNotification]. */
  enum class Type {
    /** Someone has favorited a [MastodonStatus] authored by the user. */
    FAVOURITE {
      override val channelID = "favoriting"
      override val channelNameResourceID = R.string.favoriting_notification_channel
      override val channelDescriptionResourceID =
        R.string.favoriting_notification_channel_description

      override suspend fun getContentTitle(
        context: Context,
        authenticationLock: SomeAuthenticationLock,
        parent: MastodonNotification
      ) =
        context.getString(
          R.string.someone_has_favorited_your_post,
          parent.account?.acct,
          parent.status?.summarizedContent
        )
    },

    /** Someone has followed the user. */
    FOLLOW {
      override val channelID = "follows"
      override val channelNameResourceID = R.string.follows_notification_channel
      override val channelDescriptionResourceID = R.string.follows_notification_channel_description

      override suspend fun getContentTitle(
        context: Context,
        authenticationLock: SomeAuthenticationLock,
        parent: MastodonNotification
      ) = context.getString(R.string.someone_has_followed_you, parent.account?.acct)
    },

    /** Someone has requested to follow the user. */
    FOLLOW_REQUEST {
      override val channelID = "requests"
      override val channelNameResourceID = R.string.follow_requests_notification_channel
      override val channelDescriptionResourceID =
        R.string.follow_requests_notification_channel_description

      override suspend fun getContentTitle(
        context: Context,
        authenticationLock: SomeAuthenticationLock,
        parent: MastodonNotification
      ) = context.getString(R.string.someone_has_requested_to_follow_you, parent.account?.acct)
    },

    /** Someone has mentioned the user. */
    MENTION {
      override val channelID = "mentions"
      override val channelNameResourceID = R.string.mentions_notification_channel
      override val channelDescriptionResourceID = R.string.mentions_notification_channel_description

      override suspend fun getContentTitle(
        context: Context,
        authenticationLock: SomeAuthenticationLock,
        parent: MastodonNotification
      ) =
        context.getString(
          R.string.someone_has_mentioned_you,
          parent.account?.acct,
          parent.status?.summarizedContent
        )
    },

    /** A poll that has either been created or voted in by the user has come to an end. */
    POLL {
      override val channelID = "polling"
      override val channelNameResourceID = R.string.polling_notification_channel
      override val channelDescriptionResourceID = R.string.polling_notification_channel_description

      override suspend fun getContentTitle(
        context: Context,
        authenticationLock: SomeAuthenticationLock,
        parent: MastodonNotification
      ) =
        if (parent.account != null && parent.account.isOwned(authenticationLock)) {
          context.getString(
            R.string.poll_created_by_you_has_ended,
            parent.status?.summarizedContent
          )
        } else {
          context.getString(
            R.string.poll_created_by_someone_has_ended,
            parent.account?.acct,
            parent.status?.summarizedContent
          )
        }
    },

    /** Someone has reposted a [MastodonStatus] authored by the user. */
    REBLOG {
      override val channelID = "reposting"
      override val channelNameResourceID = R.string.reposting_notification_channel
      override val channelDescriptionResourceID =
        R.string.reposting_notification_channel_description

      override suspend fun getContentTitle(
        context: Context,
        authenticationLock: SomeAuthenticationLock,
        parent: MastodonNotification
      ) =
        context.getString(
          R.string.someone_has_reposted_your_post,
          parent.account?.acct,
          parent.status?.summarizedContent
        )
    },

    /** Someone the user follows has been severed, either because of moderation or blocking. */
    SEVERED_RELATIONSHIPS {
      override val channelID = "moderation"
      override val channelNameResourceID = R.string.moderation_notification_channel
      override val channelDescriptionResourceID =
        R.string.moderation_notification_channel_description

      override suspend fun getContentTitle(
        context: Context,
        authenticationLock: SomeAuthenticationLock,
        parent: MastodonNotification
      ) = context.getString(R.string.relationship_has_been_severed)
    },

    /** Someone whose [MastodonStatus]es the user chose to be notified about has published. */
    STATUS {
      override val channelID = "subscriptions"
      override val channelNameResourceID = R.string.subscriptions_notification_channel
      override val channelDescriptionResourceID =
        R.string.subscriptions_notification_channel_description

      override suspend fun getContentTitle(
        context: Context,
        authenticationLock: SomeAuthenticationLock,
        parent: MastodonNotification
      ) =
        context.getString(
          R.string.someone_has_published,
          parent.account?.acct,
          parent.status?.summarizedContent
        )
    },

    /** A [MastodonStatus] with which the user has interacted has been edited. */
    UPDATE {
      override val channelID = "editing"
      override val channelNameResourceID = R.string.editing_notification_channel
      override val channelDescriptionResourceID = R.string.editing_notification_channel_description

      override suspend fun getContentTitle(
        context: Context,
        authenticationLock: SomeAuthenticationLock,
        parent: MastodonNotification
      ) =
        context.getString(
          R.string.someone_has_edited,
          parent.account?.acct,
          parent.status?.summarizedContent
        )
    };

    /**
     * ID of the [NotificationChannel] into which this [Type] can be converted.
     *
     * @see NotificationChannel.getId
     * @see Type.toNotificationChannel
     */
    abstract val channelID: String

    /**
     * [String] resource ID of the name of the [NotificationChannel] into which this [Type] can be
     * converted.
     *
     * @see NotificationChannel.getName
     * @see Type.toNotificationChannel
     */
    @get:StringRes protected abstract val channelNameResourceID: Int

    /**
     * Resource ID of the [String] that explains the kind of notifications that are sent to the
     * [NotificationChannel] into which this [Type] can be converted.
     *
     * @see NotificationChannel.getDescription
     * @see Type.toNotificationChannel
     */
    @get:StringRes protected abstract val channelDescriptionResourceID: Int

    /**
     * Converts this [Type] into a [NotificationChannel].
     *
     * @param context [Context] from which the name and the description of the [NotificationChannel]
     *   will be obtained.
     * @see NotificationChannel.getName
     * @see NotificationChannel.getDescription
     * @see Type.getChannelName
     * @see Type.getChannelDescription
     */
    fun toNotificationChannel(context: Context) =
      NotificationChannel(
          channelID,
          getChannelName(context),
          NotificationManager.IMPORTANCE_DEFAULT
        )
        .apply { description = getChannelDescription(context) }

    /**
     * Obtains the name of the [NotificationChannel] into which this [Type] can be converted.
     *
     * @see Type.channelID
     * @see NotificationChannel.getName
     * @see Type.toNotificationChannel
     */
    @VisibleForTesting
    fun getChannelName(context: Context) = context.getString(channelNameResourceID)

    /**
     * Obtains the description of the [NotificationChannel] into which this [Type] can be converted.
     *
     * @see Type.channelDescriptionResourceID
     * @see NotificationChannel.getDescription
     * @see Type.toNotificationChannel
     */
    @VisibleForTesting
    fun getChannelDescription(context: Context) = context.getString(channelDescriptionResourceID)

    /**
     * Obtains the title of the content displayed by a [Notification] is computed.
     *
     * @param context [Context] from which the [String] will be retrieved given its resource ID.
     * @param authenticationLock [AuthenticationLock] for checking whether the related
     *   [MastodonAccount] is owned by the current [Actor].
     * @param parent [MastodonNotification] to which this [Type] belongs.
     * @see toNotification
     * @see account
     * @see MastodonAccount.isOwned
     */
    @VisibleForTesting
    abstract suspend fun getContentTitle(
      context: Context,
      authenticationLock: SomeAuthenticationLock,
      parent: MastodonNotification
    ): String
  }

  /**
   * Creates a version of the [id] suited for a notification to be sent to the device. Given that
   * the original one is a [String], it is converted into an integer in case it contains only
   * digits; otherwise, its hash code is returned.
   *
   * @return [id] as an integer (if it is digit-only) or its hash code.
   */
  fun generateSystemNotificationID() =
    if (id.startsWith("0") || id.any { !it.isDigit() }) {
      id.hashCode()
    } else {
      id.toInt()
    }

  /**
   * Converts this [MastodonNotification] into a [Notification].
   *
   * @param context [Context] with which the underlying [Notification.Builder] will be instantiated.
   * @param authenticationLock [AuthenticationLock] for checking whether the [account] is owned by
   *   the current [Actor].
   * @see Type.getContentTitle
   * @see account
   * @see MastodonAccount.isOwned
   */
  suspend fun toNotification(context: Context, authenticationLock: SomeAuthenticationLock) =
    Notification.Builder(context, type.channelID)
      .setAutoCancel(true)
      .setContentTitle(type.getContentTitle(context, authenticationLock, parent = this))
      .setShowWhen(true)
      .setWhen(Instant.parse(createdAt).toEpochMilli())
      .build()

  companion object {
    /**
     * Converts a [ZonedDateTime] into a [createdAt][createdAt] [String].
     *
     * @param zonedDateTime [ZonedDateTime] to be converted.
     */
    @JvmStatic
    @VisibleForTesting
    fun createdAt(zonedDateTime: ZonedDateTime): String =
      zonedDateTime.format(DateTimeFormatter.ISO_INSTANT)
  }
}
