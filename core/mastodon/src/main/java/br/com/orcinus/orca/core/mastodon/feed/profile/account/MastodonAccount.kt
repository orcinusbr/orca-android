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

package br.com.orcinus.orca.core.mastodon.feed.profile.account

import android.content.Context
import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.composite.timeline.text.annotated.fromHtml
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfile
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.MastodonPost
import br.com.orcinus.orca.core.mastodon.feed.profile.type.editable.MastodonEditableProfile
import br.com.orcinus.orca.core.mastodon.feed.profile.type.followable.MastodonFollowableProfile
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.markdown.Markdown
import io.ktor.client.call.body
import java.net.URI
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API when requesting a user's account.
 *
 * @property id Unique global identifier.
 * @property username Unique identifier within the instance from which this [Account] is.
 * @property acct Relative identifier based on the instance of this [Account].
 * @property uri URI [String] that leads to this [Account] within its instance.
 * @property displayName Name that's publicly displayed.
 * @property locked Whether its contents are private, meaning that they can only be seen by accepted
 *   followers.
 * @property note Description provided by the owner.
 * @property avatar URI [String] that leads to the avatar image.
 * @property followersCount Amount of followers that this [Account] has.
 * @property followingCount Amount of other [Account]s that this one is following.
 */
@Serializable
internal data class MastodonAccount(
  private val id: String,
  private val username: String,
  val acct: String,
  private val uri: String,
  private val displayName: String,
  private val locked: Boolean,
  private val note: String,
  private val avatar: String,
  private val followersCount: Int,
  private val followingCount: Int
) {
  /**
   * Converts this [MastodonAccount] into an [Author].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [Author]'s avatar will be loaded from a [URI].
   */
  fun toAuthor(avatarLoaderProvider: SomeImageLoaderProvider<URI>): Author {
    val avatarURI = URI(avatar)
    val avatarLoader = avatarLoaderProvider.provide(avatarURI)
    val account = toAccount()
    val profileURI = URI(uri)
    return Author(id, avatarLoader, displayName, account, profileURI)
  }

  /**
   * Converts this [MastodonAccount] into a [Profile].
   *
   * @param context [Context] with which the [note] will be converted into [Markdown].
   * @param requester [Requester] by which a request to obtain the follow status for a
   *   [MastodonFollowableProfile] is performed.
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [Profile]'s avatar will be loaded from a [URI].
   * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
   *   [MastodonProfilePostPaginator] for paginating through the resulting [MastodonProfile]'s
   *   [MastodonPost]s will be provided.
   * @see Markdown.Companion.fromHtml
   */
  suspend fun toProfile(
    context: Context,
    requester: Requester,
    avatarLoaderProvider: SomeImageLoaderProvider<URI>,
    postPaginatorProvider: MastodonProfilePostPaginator.Provider
  ): Profile {
    return if (isOwned()) {
      toEditableProfile(context, requester, avatarLoaderProvider, postPaginatorProvider)
    } else {
      toFollowableProfile(context, requester, avatarLoaderProvider, postPaginatorProvider)
    }
  }

  /**
   * Whether the currently authenticated [Actor] is the owner of this [Account].
   *
   * @param authenticationLock [AuthenticationLock] through which the ID of the [Actor] will be
   *   compared to that of this [MastodonAccount].
   */
  suspend fun isOwned(authenticationLock: SomeAuthenticationLock): Boolean {
    return authenticationLock.scheduleUnlock { it.id == id }
  }

  /**
   * Converts this [MastodonAccount] into a [ProfileSearchResult].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [ProfileSearchResult]'s avatar will be loaded from a [URI].
   */
  fun toProfileSearchResult(
    avatarLoaderProvider: SomeImageLoaderProvider<URI>
  ): ProfileSearchResult {
    val account = toAccount()
    val avatarURI = URI(avatar)
    val avatarLoader = avatarLoaderProvider.provide(avatarURI)
    val uri = URI(uri)
    return ProfileSearchResult(id, account, avatarLoader, displayName, uri)
  }

  /** Converts this [MastodonAccount] into an [Account]. */
  private fun toAccount(): Account {
    return Account.of(acct, fallbackDomain = "mastodon.social")
  }

  /**
   * Whether the currently authenticated [Actor] is the owner of this [Account].
   *
   * @throws Injector.ModuleNotRegisteredException If a [CoreModule] is not registered in the
   *   [Injector].
   */
  @Deprecated(
    "This method has a hidden dependency on the `CoreModule` that is expected to have been " +
      "registered in the `Injector`. Prefer calling its overload that explicitly requires an " +
      "`AuthenticationLock`.",
    ReplaceWith("isOwned(authenticationLock)")
  )
  private suspend fun isOwned(): Boolean {
    val authenticationLock =
      Injector.from<CoreModule>().instanceProvider().provide().authenticationLock
    return isOwned(authenticationLock)
  }

  /**
   * Converts this [MastodonAccount] into a [MastodonEditableProfile].
   *
   * @param context [Context] with which the [note] will be converted into [Markdown].
   * @param requester [Requester] by which editing requests are performed.
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [MastodonEditableProfile]'s avatar will be loaded from a [URI].
   * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
   *   [MastodonProfilePostPaginator] for paginating through the resulting
   *   [MastodonEditableProfile]'s [MastodonPost]s will be provided.
   * @see Markdown.Companion.fromHtml
   */
  private fun toEditableProfile(
    context: Context,
    requester: Requester,
    avatarLoaderProvider: SomeImageLoaderProvider<URI>,
    postPaginatorProvider: MastodonProfilePostPaginator.Provider
  ): MastodonEditableProfile {
    val account = toAccount()
    val avatarURI = URI(avatar)
    val avatarLoader = avatarLoaderProvider.provide(avatarURI)
    val bio = Markdown.fromHtml(context, note)
    val uri = URI(uri)
    return MastodonEditableProfile(
      requester,
      postPaginatorProvider,
      id,
      account,
      avatarLoader,
      displayName,
      bio,
      followersCount,
      followingCount,
      uri
    )
  }

  /**
   * Converts this [MastodonAccount] into a [MastodonFollowableProfile].
   *
   * @param context [Context] with which the [note] will be converted into [Markdown].
   * @param requester [Requester] by which a request to obtain the follow status will be performed.
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [MastodonFollowableProfile]'s avatar will be loaded from a [URI].
   * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
   *   [MastodonProfilePostPaginator] for paginating through the resulting
   *   [MastodonFollowableProfile]'s [MastodonPost]s will be provided.
   * @see Markdown.Companion.fromHtml
   */
  private suspend fun toFollowableProfile(
    context: Context,
    requester: Requester,
    avatarLoaderProvider: SomeImageLoaderProvider<URI>,
    postPaginatorProvider: MastodonProfilePostPaginator.Provider
  ): MastodonFollowableProfile<Follow> {
    val account = toAccount()
    val avatarURI = URI(avatar)
    val avatarLoader = avatarLoaderProvider.provide(avatarURI)
    val bio = Markdown.fromHtml(context, note)
    val uri = URI(uri)
    val follow =
      requester
        .authenticated()
        .get({
          path("api")
            .path("v1")
            .path("accounts")
            .path("relationships")
            .query()
            .parameter("id", id)
            .build()
        })
        .body<List<MastodonRelationship>>()
        .first()
        .toFollow(locked)
    return MastodonFollowableProfile(
      postPaginatorProvider,
      id,
      account,
      avatarLoader,
      displayName,
      bio,
      follow,
      followersCount,
      followingCount,
      uri
    )
  }

  companion object {
    /**
     * [MastodonAccount] whose fields are assigned default values (empty [String]s, zeroed [Number]s
     * and `false` [Boolean]s).
     */
    @JvmStatic
    @VisibleForTesting
    val default =
      MastodonAccount(
        id = "0",
        username = "",
        acct = "",
        uri = "",
        displayName = "",
        locked = false,
        note = "",
        avatar = "",
        followersCount = 0,
        followingCount = 0
      )
  }
}
