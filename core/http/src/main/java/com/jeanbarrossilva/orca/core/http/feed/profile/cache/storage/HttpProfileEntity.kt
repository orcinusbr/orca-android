package com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage

import androidx.annotation.IntDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.http.feed.profile.HttpProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.http.feed.profile.type.editable.HttpEditableProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.type.followable.HttpFollowableProfile
import com.jeanbarrossilva.orca.core.http.imageLoaderProvider
import com.jeanbarrossilva.orca.std.injector.Injector
import com.jeanbarrossilva.orca.std.styledstring.toStyledString
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
   * @param tootPaginateSourceProvider [ProfileTootPaginateSource.Provider] by which a
   *   [ProfileTootPaginateSource] for paginating through the resulting [HttpProfile]'s [Toot]s will
   *   be provided.
   * @throws IllegalStateException If the [type] is unknown.
   */
  @Throws(IllegalStateException::class)
  internal fun toProfile(tootPaginateSourceProvider: ProfileTootPaginateSource.Provider): Profile {
    return when (type) {
      EDITABLE_TYPE -> toMastodonEditableProfile(tootPaginateSourceProvider)
      FOLLOWABLE_TYPE -> toMastodonFollowableProfile(tootPaginateSourceProvider)
      else -> throw IllegalStateException("Unknown profile entity type: $type.")
    }
  }

  /**
   * Converts this [HttpProfileEntity] into an [HttpEditableProfile].
   *
   * @param tootPaginateSourceProvider [ProfileTootPaginateSource.Provider] by which a
   *   [ProfileTootPaginateSource] for paginating through the resulting [HttpEditableProfile]'s
   *   [Toot]s will be provided.
   */
  private fun toMastodonEditableProfile(
    tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
  ): HttpEditableProfile {
    val account = Account.of(account)
    val avatarURL = URL(avatarURL)
    val avatarLoader = Injector.from<HttpModule>().imageLoaderProvider().provide(avatarURL)
    val bio = bio.toStyledString()
    val url = URL(url)
    return HttpEditableProfile(
      tootPaginateSourceProvider,
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
   * @param tootPaginateSourceProvider [ProfileTootPaginateSource.Provider] by which a
   *   [ProfileTootPaginateSource] for paginating through the resulting [HttpFollowableProfile]'s
   *   [Toot]s will be provided.
   */
  private fun toMastodonFollowableProfile(
    tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
  ): HttpFollowableProfile<Follow> {
    val account = Account.of(account)
    val avatarURL = URL(avatarURL)
    val avatarLoader = Injector.from<HttpModule>().imageLoaderProvider().provide(avatarURL)
    val bio = bio.toStyledString()
    val follow = Follow.of(checkNotNull(follow))
    val url = URL(url)
    return HttpFollowableProfile(
      tootPaginateSourceProvider,
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

  companion object {
    /** [Int] that represents an [HttpEditableProfile]. */
    internal const val EDITABLE_TYPE = 0

    /** [Int] that represents an [HttpFollowableProfile]. */
    internal const val FOLLOWABLE_TYPE = 1
  }
}
