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

package br.com.orcinus.orca.core.sample.feed.profile.post

import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.account.at
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.sample.auth.actor.sample
import br.com.orcinus.orca.core.sample.feed.profile.account.sample
import br.com.orcinus.orca.core.sample.image.AuthorImageSource
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.ext.uri.URIBuilder
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import java.util.UUID

/** ID of an [Author] created by [createChristianSample]. */
private val christianSampleAuthorID = UUID.randomUUID().toString()

/** ID of an [Author] created by [createRamboSample]. */
private val ramboSampleAuthorID = UUID.randomUUID().toString()

/**
 * Creates sample [Author]s.
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Author]s' avatars will be loaded from a [SampleImageSource].
 */
fun Author.Companion.createSamples(
  avatarLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): List<Author> {
  return listOf(
    createSample(avatarLoaderProvider),
    createChristianSample(avatarLoaderProvider),
    createRamboSample(avatarLoaderProvider)
  )
}

/**
 * Creates a sample [Author].
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Author]'s avatar will be loaded from a [SampleImageSource].
 */
fun Author.Companion.createSample(
  avatarLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): Author {
  return Author(
    Actor.Authenticated.sample.id,
    avatarLoaderProvider.provide(AuthorImageSource.Default),
    name = "Jean Silva",
    Account.sample,
    profileURI =
      URIBuilder.url().scheme("https").host("mastodon.social").path("@jeanbarrossilva").build()
  )
}

/**
 * Creates a sample author based on
 * [@christianselig@mastodon.social](https://mastodon.social/@christianselig).
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Author]'s avatar will be loaded from a [SampleImageSource].
 */
internal fun Author.Companion.createChristianSample(
  avatarLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): Author {
  return Author(
    christianSampleAuthorID,
    avatarLoaderProvider.provide(AuthorImageSource.Christian),
    name = "Christian Selig",
    account = "christianselig" at "mastodon.social",
    profileURI =
      URIBuilder.url().scheme("https").host("mastodon.social").path("@christianselig").build()
  )
}

/**
 * Creates a sample author based on [@_inside@mastodon.social](https://mastodon.social/@_inside).
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Author]'s avatar will be loaded from a [SampleImageSource].
 */
internal fun Author.Companion.createRamboSample(
  avatarLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): Author {
  return Author(
    ramboSampleAuthorID,
    avatarLoaderProvider.provide(AuthorImageSource.Rambo),
    name = "Guilherme Rambo",
    account = "_inside" at "mastodon.social",
    profileURI = URIBuilder.url().scheme("https").host("mastodon.social").path("@_inside").build()
  )
}
