package com.jeanbarrossilva.orca.core.http.feed.profile.account

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.http.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.http.feed.profile.HttpProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.core.http.feed.profile.type.editable.HttpEditableProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.type.followable.HttpFollowableProfile
import com.jeanbarrossilva.orca.core.http.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.platform.ui.core.style.fromHtml
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.injector.Injector
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import java.net.URL
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API when requesting a user's account.
 *
 * @param id Unique global identifier.
 * @param username Unique identifier within the instance from which this [Account] is.
 * @param acct Relative identifier based on the instance of this [Account].
 * @param url URL [String] that leads to this [Account] within its instance.
 * @param displayName Name that's publicly displayed.
 * @param locked Whether its contents are private, meaning that they can only be seen by accepted
 *   followers.
 * @param note Description provided by the owner.
 * @param avatar URL [String] that leads to the avatar image.
 * @param followersCount Amount of followers that this [Account] has.
 * @param followingCount Amount of other [Account]s that this one is following.
 */
@Serializable
internal data class HttpAccount(
  val id: String,
  val username: String,
  val acct: String,
  val url: String,
  val displayName: String,
  val locked: Boolean,
  val note: String,
  val avatar: String,
  val followersCount: Int,
  val followingCount: Int
) {
  /**
   * Converts this [HttpAccount] into an [Author].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [Author]'s avatar will be loaded from a [URL].
   */
  fun toAuthor(avatarLoaderProvider: ImageLoader.Provider<URL>): Author {
    val avatarURL = URL(avatar)
    val avatarLoader = avatarLoaderProvider.provide(avatarURL)
    val account = toAccount()
    val profileURL = URL(url)
    return Author(id, avatarLoader, displayName, account, profileURL)
  }

  /**
   * Converts this [HttpAccount] into a [Profile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [Profile]'s avatar will be loaded from a [URL].
   * @param tootPaginateSourceProvider [ProfileTootPaginateSource.Provider] by which a
   *   [ProfileTootPaginateSource] for paginating through the resulting [HttpProfile]'s [HttpToot]s
   *   will be provided.
   */
  suspend fun toProfile(
    avatarLoaderProvider: ImageLoader.Provider<URL>,
    tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
  ): Profile {
    return if (isOwner()) {
      toEditableProfile(avatarLoaderProvider, tootPaginateSourceProvider)
    } else {
      toFollowableProfile(avatarLoaderProvider, tootPaginateSourceProvider)
    }
  }

  /** Converts this [HttpAccount] into an [Account]. */
  private fun toAccount(): Account {
    return Account.of(acct, fallbackDomain = "mastodon.social")
  }

  /**
   * Whether the currently [authenticated][Actor.Authenticated] [Actor] is the owner of this
   * [Account].
   */
  private suspend fun isOwner(): Boolean {
    return Injector.from<CoreModule>()
      .instanceProvider()
      .provide()
      .authenticationLock
      .requestUnlock { it.id == id }
  }

  /**
   * Converts this [HttpAccount] into an [HttpEditableProfile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [HttpEditableProfile]'s avatar will be loaded from a [URL].
   * @param tootPaginateSourceProvider [ProfileTootPaginateSource.Provider] by which a
   *   [ProfileTootPaginateSource] for paginating through the resulting [HttpEditableProfile]'s
   *   [HttpToot]s will be provided.
   */
  private fun toEditableProfile(
    avatarLoaderProvider: ImageLoader.Provider<URL>,
    tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
  ): HttpEditableProfile {
    val account = toAccount()
    val avatarURL = URL(avatar)
    val avatarLoader = avatarLoaderProvider.provide(avatarURL)
    val bio = StyledString.fromHtml(note)
    val url = URL(url)
    return HttpEditableProfile(
      tootPaginateSourceProvider,
      id,
      account,
      avatarLoader,
      displayName,
      bio,
      followersCount,
      followingCount,
      url
    )
  }

  /**
   * Converts this [HttpAccount] into an [HttpFollowableProfile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [HttpFollowableProfile]'s avatar will be loaded from a [URL].
   * @param tootPaginateSourceProvider [ProfileTootPaginateSource.Provider] by which a
   *   [ProfileTootPaginateSource] for paginating through the resulting [HttpFollowableProfile]'s
   *   [HttpToot]s will be provided.
   */
  private suspend fun toFollowableProfile(
    avatarLoaderProvider: ImageLoader.Provider<URL>,
    tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
  ): HttpFollowableProfile<Follow> {
    val account = toAccount()
    val avatarURL = URL(avatar)
    val avatarLoader = avatarLoaderProvider.provide(avatarURL)
    val bio = StyledString.fromHtml(note)
    val url = URL(url)
    val follow =
      (Injector.from<CoreModule>().instanceProvider().provide() as SomeHttpInstance)
        .client
        .authenticateAndGet("/api/v1/accounts/relationships") { parameter("id", id) }
        .body<List<HttpRelationship>>()
        .first()
        .toFollow(this)
    return HttpFollowableProfile(
      tootPaginateSourceProvider,
      id,
      account,
      avatarLoader,
      displayName,
      bio,
      follow,
      followersCount,
      followingCount,
      url
    )
  }
}
