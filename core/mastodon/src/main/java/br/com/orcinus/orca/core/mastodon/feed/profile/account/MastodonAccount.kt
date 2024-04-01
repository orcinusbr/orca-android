/*
 * Copyright © 2023-2024 Orca
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

import br.com.orcinus.orca.composite.timeline.text.annotated.fromHtml
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.mastodon.client.authenticateAndGet
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfile
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.MastodonPost
import br.com.orcinus.orca.core.mastodon.feed.profile.type.editable.MastodonEditableProfile
import br.com.orcinus.orca.core.mastodon.feed.profile.type.followable.MastodonFollowableProfile
import br.com.orcinus.orca.core.mastodon.instance.SomeMastodonInstance
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.styledstring.StyledString
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
internal data class MastodonAccount(
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
   * Converts this [MastodonAccount] into an [Author].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [Author]'s avatar will be loaded from a [URL].
   */
  fun toAuthor(avatarLoaderProvider: SomeImageLoaderProvider<URL>): Author {
    val avatarURL = URL(avatar)
    val avatarLoader = avatarLoaderProvider.provide(avatarURL)
    val account = toAccount()
    val profileURL = URL(url)
    return Author(id, avatarLoader, displayName, account, profileURL)
  }

  /**
   * Converts this [MastodonAccount] into a [Profile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [Profile]'s avatar will be loaded from a [URL].
   * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
   *   [MastodonProfilePostPaginator] for paginating through the resulting [MastodonProfile]'s
   *   [MastodonPost]s will be provided.
   */
  suspend fun toProfile(
    avatarLoaderProvider: SomeImageLoaderProvider<URL>,
    postPaginatorProvider: MastodonProfilePostPaginator.Provider
  ): Profile {
    return if (isOwner()) {
      toEditableProfile(avatarLoaderProvider, postPaginatorProvider)
    } else {
      toFollowableProfile(avatarLoaderProvider, postPaginatorProvider)
    }
  }

  /** Converts this [MastodonAccount] into an [Account]. */
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
      .scheduleUnlock { it.id == id }
  }

  /**
   * Converts this [MastodonAccount] into A [MastodonEditableProfile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [MastodonEditableProfile]'s avatar will be loaded from a [URL].
   * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
   *   [MastodonProfilePostPaginator] for paginating through the resulting
   *   [MastodonEditableProfile]'s [MastodonPost]s will be provided.
   */
  private fun toEditableProfile(
    avatarLoaderProvider: SomeImageLoaderProvider<URL>,
    postPaginatorProvider: MastodonProfilePostPaginator.Provider
  ): MastodonEditableProfile {
    val account = toAccount()
    val avatarURL = URL(avatar)
    val avatarLoader = avatarLoaderProvider.provide(avatarURL)
    val bio = StyledString.fromHtml(note)
    val url = URL(url)
    return MastodonEditableProfile(
      postPaginatorProvider,
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
   * Converts this [MastodonAccount] into A [MastodonFollowableProfile].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [MastodonFollowableProfile]'s avatar will be loaded from a [URL].
   * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
   *   [MastodonProfilePostPaginator] for paginating through the resulting
   *   [MastodonFollowableProfile]'s [MastodonPost]s will be provided.
   */
  private suspend fun toFollowableProfile(
    avatarLoaderProvider: SomeImageLoaderProvider<URL>,
    postPaginatorProvider: MastodonProfilePostPaginator.Provider
  ): MastodonFollowableProfile<Follow> {
    val account = toAccount()
    val avatarURL = URL(avatar)
    val avatarLoader = avatarLoaderProvider.provide(avatarURL)
    val bio = StyledString.fromHtml(note)
    val url = URL(url)
    val follow =
      (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
        .client
        .authenticateAndGet("/api/v1/accounts/relationships") { parameter("id", id) }
        .body<List<MastodonRelationship>>()
        .first()
        .toFollow(this)
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
      url
    )
  }
}
