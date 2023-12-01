/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage

import androidx.annotation.IntDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.editable.MastodonEditableProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.followable.MastodonFollowableProfile
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.style.Style
import java.net.URL

/**
 * Primitive information to be persisted about a [MastodonProfile].
 *
 * @param id Unique identifier.
 * @param account [String] representation of the [MastodonProfile]'s [account][Profile.account].
 * @param avatarURL URL [String] that leads to the avatar image.
 * @param bio Describes who the owner is and/or provides information regarding the
 *   [MastodonProfile].
 * @param type Determines whether the [MastodonProfile] is an [MastodonEditableProfile] or an
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
  @ColumnInfo(name = "avatar_url") internal val avatarURL: String,
  internal val name: String,
  internal val bio: String,
  @Type internal val type: Int,
  internal val follow: String?,
  @ColumnInfo(name = "follower_count") internal val followerCount: Int,
  @ColumnInfo(name = "following_count") internal val followingCount: Int,
  internal val url: String
) {
  /** Constrains the [type] to the known ones. */
  @IntDef(EDITABLE_TYPE, FOLLOWABLE_TYPE) internal annotation class Type

  /**
   * Converts this [MastodonProfileEntity] into a [Profile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [Profile]'s avatar will be loaded from a [URL].
   * @param dao [MastodonProfileEntityDao] that will select the persisted
   *   [Mastodon style entities][MastodonStyleEntity].
   * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
   *   [MastodonProfilePostPaginator] for paginating through the resulting [MastodonProfile]'s
   *   [Post]s will be provided.
   * @throws IllegalStateException If the [type] is unknown.
   */
  @Throws(IllegalStateException::class)
  internal suspend fun toProfile(
    avatarLoaderProvider: ImageLoader.Provider<URL>,
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
   * Converts this [MastodonProfileEntity] into an [MastodonEditableProfile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [MastodonEditableProfile]'s avatar will be loaded from a [URL].
   * @param dao [MastodonProfileEntityDao] that will select the persisted
   *   [Mastodon style entities][MastodonStyleEntity] applied to the [bio].
   * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
   *   [MastodonProfilePostPaginator] for paginating through the resulting
   *   [MastodonEditableProfile]'s [Post]s will be provided.
   */
  private suspend fun toMastodonEditableProfile(
    avatarLoaderProvider: ImageLoader.Provider<URL>,
    dao: MastodonProfileEntityDao,
    postPaginatorProvider: MastodonProfilePostPaginator.Provider
  ): MastodonEditableProfile {
    val account = Account.of(account)
    val avatarURL = URL(avatarURL)
    val avatarLoader = avatarLoaderProvider.provide(avatarURL)
    val bio = getBioAsStyledString(dao)
    val url = URL(url)
    return MastodonEditableProfile(
      postPaginatorProvider,
      id,
      account,
      avatarLoader,
      name,
      bio,
      followerCount,
      followingCount,
      url
    )
  }

  /**
   * Converts this [MastodonProfileEntity] into an [MastodonFollowableProfile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [MastodonFollowableProfile]'s avatar will be loaded from a [URL].
   * @param dao [MastodonProfileEntityDao] that will select the persisted
   *   [Mastodon style entities][MastodonStyleEntity].
   * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
   *   [MastodonProfilePostPaginator] for paginating through the resulting
   *   [MastodonFollowableProfile]'s [Post]s will be provided.
   */
  private suspend fun toMastodonFollowableProfile(
    avatarLoaderProvider: ImageLoader.Provider<URL>,
    dao: MastodonProfileEntityDao,
    postPaginatorProvider: MastodonProfilePostPaginator.Provider
  ): MastodonFollowableProfile<Follow> {
    val account = Account.of(account)
    val avatarURL = URL(avatarURL)
    val avatarLoader = avatarLoaderProvider.provide(avatarURL)
    val bio = getBioAsStyledString(dao)
    val follow = Follow.of(checkNotNull(follow))
    val url = URL(url)
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
      url
    )
  }

  /**
   * Gets the [bio] as a [StyledString], with its [Style]s applied to it.
   *
   * @param dao [MastodonProfileEntityDao] that will select the persisted
   *   [Mastodon style entities][MastodonStyleEntity].
   */
  private suspend fun getBioAsStyledString(dao: MastodonProfileEntityDao): StyledString {
    val styles = dao.selectWithBioStylesByID(id).styles.map(MastodonStyleEntity::toStyle)
    return StyledString(bio, styles)
  }

  companion object {
    /** [Int] that represents an [MastodonEditableProfile]. */
    internal const val EDITABLE_TYPE = 0

    /** [Int] that represents an [MastodonFollowableProfile]. */
    internal const val FOLLOWABLE_TYPE = 1
  }
}
