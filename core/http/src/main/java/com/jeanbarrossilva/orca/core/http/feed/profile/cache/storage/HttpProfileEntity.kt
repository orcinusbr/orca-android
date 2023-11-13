package com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage

import androidx.annotation.IntDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.http.feed.profile.HttpProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.ProfileTootPaginator
import com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.style.HttpStyleEntity
import com.jeanbarrossilva.orca.core.http.feed.profile.type.editable.HttpEditableProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.type.followable.HttpFollowableProfile
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.style.Style
import java.net.URL

/**
 * Primitive information to be persisted about an [HttpProfile].
 *
 * @param id Unique identifier.
 * @param account [String] representation of the [HttpProfile]'s [account][Profile.account].
 * @param avatarURL URL [String] that leads to the avatar image.
 * @param bio Describes who the owner is and/or provides information regarding the [HttpProfile].
 * @param type Determines whether the [HttpProfile] is an [HttpEditableProfile] or an
 *   [HttpFollowableProfile].
 * @param follow [String] version of the [HttpFollowableProfile]'s
 *   [follow][HttpFollowableProfile.follow] or `null` if the [HttpProfile] is of a different type.
 * @param followerCount Amount of followers the [HttpProfile] has.
 * @param followingCount Amount of other [HttpProfile]s the one this [HttpProfileEntity] refers to
 *   follows.
 */
@Entity(tableName = "profiles")
data class HttpProfileEntity
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
   * Converts this [HttpProfileEntity] into a [Profile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [Profile]'s avatar will be loaded from a [URL].
   * @param dao [HttpProfileEntityDao] that will select the persisted
   *   [HTTP style entities][HttpStyleEntity].
   * @param tootPaginatorProvider [ProfileTootPaginator.Provider] by which a [ProfileTootPaginator]
   *   for paginating through the resulting [HttpProfile]'s [Toot]s will be provided.
   * @throws IllegalStateException If the [type] is unknown.
   */
  @Throws(IllegalStateException::class)
  internal suspend fun toProfile(
    avatarLoaderProvider: ImageLoader.Provider<URL>,
    dao: HttpProfileEntityDao,
    tootPaginatorProvider: ProfileTootPaginator.Provider
  ): Profile {
    return when (type) {
      EDITABLE_TYPE -> toMastodonEditableProfile(avatarLoaderProvider, dao, tootPaginatorProvider)
      FOLLOWABLE_TYPE ->
        toMastodonFollowableProfile(avatarLoaderProvider, dao, tootPaginatorProvider)
      else -> throw IllegalStateException("Unknown profile entity type: $type.")
    }
  }

  /**
   * Converts this [HttpProfileEntity] into an [HttpEditableProfile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [HttpEditableProfile]'s avatar will be loaded from a [URL].
   * @param dao [HttpProfileEntityDao] that will select the persisted
   *   [HTTP style entities][HttpStyleEntity] applied to the [bio].
   * @param tootPaginatorProvider [ProfileTootPaginator.Provider] by which a [ProfileTootPaginator]
   *   for paginating through the resulting [HttpEditableProfile]'s [Toot]s will be provided.
   */
  private suspend fun toMastodonEditableProfile(
    avatarLoaderProvider: ImageLoader.Provider<URL>,
    dao: HttpProfileEntityDao,
    tootPaginatorProvider: ProfileTootPaginator.Provider
  ): HttpEditableProfile {
    val account = Account.of(account)
    val avatarURL = URL(avatarURL)
    val avatarLoader = avatarLoaderProvider.provide(avatarURL)
    val bio = getBioAsStyledString(dao)
    val url = URL(url)
    return HttpEditableProfile(
      tootPaginatorProvider,
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
   * Converts this [HttpProfileEntity] into an [HttpFollowableProfile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [HttpFollowableProfile]'s avatar will be loaded from a [URL].
   * @param dao [HttpProfileEntityDao] that will select the persisted
   *   [HTTP style entities][HttpStyleEntity].
   * @param tootPaginatorProvider [ProfileTootPaginator.Provider] by which a [ProfileTootPaginator]
   *   for paginating through the resulting [HttpFollowableProfile]'s [Toot]s will be provided.
   */
  private suspend fun toMastodonFollowableProfile(
    avatarLoaderProvider: ImageLoader.Provider<URL>,
    dao: HttpProfileEntityDao,
    tootPaginatorProvider: ProfileTootPaginator.Provider
  ): HttpFollowableProfile<Follow> {
    val account = Account.of(account)
    val avatarURL = URL(avatarURL)
    val avatarLoader = avatarLoaderProvider.provide(avatarURL)
    val bio = getBioAsStyledString(dao)
    val follow = Follow.of(checkNotNull(follow))
    val url = URL(url)
    return HttpFollowableProfile(
      tootPaginatorProvider,
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
   * @param dao [HttpProfileEntityDao] that will select the persisted
   *   [HTTP style entities][HttpStyleEntity].
   */
  private suspend fun getBioAsStyledString(dao: HttpProfileEntityDao): StyledString {
    val styles = dao.selectWithBioStylesByID(id).styles.map(HttpStyleEntity::toStyle)
    return StyledString(bio, styles)
  }

  companion object {
    /** [Int] that represents an [HttpEditableProfile]. */
    internal const val EDITABLE_TYPE = 0

    /** [Int] that represents an [HttpFollowableProfile]. */
    internal const val FOLLOWABLE_TYPE = 1
  }
}
