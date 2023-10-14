package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.account.at
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.sample.SampleCoreModule
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.image.AuthorImageSource
import com.jeanbarrossilva.orca.core.sample.imageLoaderProvider
import com.jeanbarrossilva.orca.std.injector.Injector
import java.net.URL
import java.util.UUID

/** [Author] returned by [rambo]. */
internal val ramboAuthor =
  Author(
    "${UUID.randomUUID()}",
    Injector.from<SampleCoreModule>().imageLoaderProvider().provide(AuthorImageSource.Rambo),
    name = "Guilherme Rambo",
    account = "_inside" at "mastodon.social",
    profileURL = URL("https://mastodon.social/@_inside")
  )

/** [Author] returned by [sample]'s getter. */
private val sampleAuthor =
  Author(
    Actor.Authenticated.sample.id,
    Injector.from<SampleCoreModule>().imageLoaderProvider().provide(AuthorImageSource.Default),
    name = "Jean Silva",
    Account.sample,
    profileURL = URL("https://mastodon.social/@jeanbarrossilva")
  )

/** [Author] based on [@_inside@mastodon.social](https://mastodon.social/@_inside). */
internal val Author.Companion.rambo
  get() = ramboAuthor

/** A sample [Author]. */
val Author.Companion.sample
  get() = sampleAuthor
