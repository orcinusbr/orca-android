/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.cache.storage

import androidx.annotation.IntDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfile
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntity
import br.com.orcinus.orca.core.mastodon.feed.profile.type.editable.MastodonEditableProfile
import br.com.orcinus.orca.core.mastodon.feed.profile.type.followable.MastodonFollowableProfile
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style
import java.net.URI

/**
 * Primitive information to be persisted about a [MastodonProfile].
 *
 * @param id Unique identifier.
 * @param account [String] representation of the [MastodonProfile]'s [account][Profile.account].
 * @param avatarURI URI [String] that leads to the avatar image.
 * @param bio Describes who the owner is and/or provides information regarding the
 *   [MastodonProfile].
 * @param type Determines whether the [MastodonProfile] is A [MastodonEditableProfile] or an
 *   [MastodonFollowableProfile].
 * @param follow [String] version of the [MastodonFollowableProfile]'s
 *   [follow][MastodonFollowableProfile.follow] or `null` if the [MastodonProfile] is of a different
 *   type.
 * @param followerCount Amount of followers the [MastodonProfile] has.
 * @param followingCount Amount of other [MastodonProfile]s the one this [MastodonProfileEntity]
 *   refers to follows.
 */
@Entity(tableName = "profiles")
data class MastodonProfileEntity
internal constructor(
  @PrimaryKey internal val id: String,
  internal val account: String,
  @ColumnInfo(name = "avatar_uri") internal val avatarURI: String,
  internal val name: String,
  internal val bio: String,
  @Type internal val type: Int,
  internal val follow: String?,
  @ColumnInfo(name = "follower_count") internal val followerCount: Int,
  @ColumnInfo(name = "following_count") internal val followingCount: Int,
  internal val uri: String
) {
  /** Constrains the [type] to the known ones. */
  @IntDef(EDITABLE_TYPE, FOLLOWABLE_TYPE) internal annotation class Type

  /**
   * Converts this [MastodonProfileEntity] into a [Profile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [Profile]'s avatar will be loaded from a [URI].
   * @param dao [MastodonProfileEntityDao] that will select the persisted
   *   [Mastodon style entities][MastodonStyleEntity].
   * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
   *   [MastodonProfilePostPaginator] for paginating through the resulting [MastodonProfile]'s
   *   [Post]s will be provided.
   * @throws IllegalStateException If the [type] is unknown.
   */
  @Throws(IllegalStateException::class)
  internal suspend fun toProfile(
    avatarLoaderProvider: SomeImageLoaderProvider<URI>,
    dao: MastodonProfileEntityDao,
    postPaginatorProvider: MastodonProfilePostPaginator.Provider
  ): Profile {
    return when (type) {
      EDITABLE_TYPE -> toMastodonEditableProfile(avatarLoaderProvider, dao, postPaginatorProvider)
      FOLLOWABLE_TYPE ->
        toMastodonFollowableProfile(avatarLoaderProvider, dao, postPaginatorProvider)
      else -> throw IllegalStateException("Unknown profile entity type: $type.")
    }
  }

  /**
   * Converts this [MastodonProfileEntity] into A [MastodonEditableProfile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [MastodonEditableProfile]'s avatar will be loaded from a [URI].
   * @param dao [MastodonProfileEntityDao] that will select the persisted
   *   [Mastodon style entities][MastodonStyleEntity] applied to the [bio].
   * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
   *   [MastodonProfilePostPaginator] for paginating through the resulting
   *   [MastodonEditableProfile]'s [Post]s will be provided.
   */
  private suspend fun toMastodonEditableProfile(
    avatarLoaderProvider: SomeImageLoaderProvider<URI>,
    dao: MastodonProfileEntityDao,
    postPaginatorProvider: MastodonProfilePostPaginator.Provider
  ): MastodonEditableProfile {
    val account = Account.of(account)
    val avatarURI = URI(avatarURI)
    val avatarLoader = avatarLoaderProvider.provide(avatarURI)
    val bio = getBioAsMarkdown(dao)
    val uri = URI(uri)
    return MastodonEditableProfile(
      postPaginatorProvider,
      id,
      account,
      avatarLoader,
      name,
      bio,
      followerCount,
      followingCount,
      uri
    )
  }

  /**
   * Converts this [MastodonProfileEntity] into A [MastodonFollowableProfile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [MastodonFollowableProfile]'s avatar will be loaded from a [URI].
   * @param dao [MastodonProfileEntityDao] that will select the persisted
   *   [Mastodon style entities][MastodonStyleEntity].
   * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
   *   [MastodonProfilePostPaginator] for paginating through the resulting
   *   [MastodonFollowableProfile]'s [Post]s will be provided.
   */
  private suspend fun toMastodonFollowableProfile(
    avatarLoaderProvider: SomeImageLoaderProvider<URI>,
    dao: MastodonProfileEntityDao,
    postPaginatorProvider: MastodonProfilePostPaginator.Provider
  ): MastodonFollowableProfile<Follow> {
    val account = Account.of(account)
    val avatarURI = URI(avatarURI)
    val avatarLoader = avatarLoaderProvider.provide(avatarURI)
    val bio = getBioAsMarkdown(dao)
    val follow = Follow.of(checkNotNull(follow))
    val uri = URI(uri)
    return MastodonFollowableProfile(
      postPaginatorProvider,
      id,
      account,
      avatarLoader,
      name,
      bio,
      follow,
      followerCount,
      followingCount,
      uri
    )
  }

  /**
   * Gets the [bio] as [Markdown], with its [Style]s applied to it.
   *
   * @param dao [MastodonProfileEntityDao] that will select the persisted
   *   [Mastodon style entities][MastodonStyleEntity].
   */
  private suspend fun getBioAsMarkdown(dao: MastodonProfileEntityDao): Markdown {
    val styles = dao.selectWithBioStylesByID(id).styles.map(MastodonStyleEntity::toStyle)
    return Markdown(bio, styles)
  }

  companion object {
    /** [Int] that represents A [MastodonEditableProfile]. */
    internal const val EDITABLE_TYPE = 0

    /** [Int] that represents A [MastodonFollowableProfile]. */
    internal const val FOLLOWABLE_TYPE = 1
  }
}
