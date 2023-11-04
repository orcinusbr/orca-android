package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.account.at
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL
import java.util.UUID

/** ID of an [Author] created by [createRamboSample]. */
private val ramboSampleAuthorID = UUID.randomUUID().toString()

/**
 * Creates a sample [Author].
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Author]'s avatar will be loaded from a [SampleImageSource].
 */
fun Author.Companion.createSample(
  avatarLoaderProvider: ImageLoader.Provider<SampleImageSource>
): Author {
  return Author(
    Actor.Authenticated.sample.id,
    avatarLoaderProvider.provide(AuthorImageSource.Default),
    name = "Jean Silva",
    Account.sample,
    profileURL = URL("https://mastodon.social/@jeanbarrossilva")
  )
}

/**
 * Creates a sample author based on [@_inside@mastodon.social](https://mastodon.social/@_inside).
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Author]'s avatar will be loaded from a [SampleImageSource].
 */
fun Author.Companion.createRamboSample(
  avatarLoaderProvider: ImageLoader.Provider<SampleImageSource>
): Author {
  return Author(
    ramboSampleAuthorID,
    avatarLoaderProvider.provide(AuthorImageSource.Rambo),
    name = "Guilherme Rambo",
    account = "_inside" at "mastodon.social",
    profileURL = URL("https://mastodon.social/@_inside")
  )
}
