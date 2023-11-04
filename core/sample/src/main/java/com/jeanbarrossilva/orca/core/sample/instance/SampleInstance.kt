package com.jeanbarrossilva.orca.core.sample.instance

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.auth.SampleAuthenticator
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.feed.SampleFeedProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.search.SampleProfileSearcher
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootWriter
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * [Instance] made out of sample underlying core structures.
 *
 * @param defaultToots [Toot]s that are provided by default by the [tootProvider].
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [SampleImageSource].
 */
class SampleInstance
internal constructor(
  defaultToots: List<Toot>,
  internal val imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
) : Instance<Authenticator>() {
  override val domain = Domain.sample
  override val authenticator: Authenticator = SampleAuthenticator
  override val authenticationLock = AuthenticationLock(authenticator, ActorProvider.sample)
  override val tootProvider = SampleTootProvider(defaultToots)
  override val feedProvider: FeedProvider = SampleFeedProvider(imageLoaderProvider, tootProvider)
  override val profileProvider: ProfileProvider =
    SampleProfileProvider(tootProvider, imageLoaderProvider)
  override val profileSearcher: ProfileSearcher =
    SampleProfileSearcher(profileProvider as SampleProfileProvider)

  /** [SampleProfileWriter] for performing write operations on the [profileProvider]. */
  val profileWriter = SampleProfileWriter(profileProvider as SampleProfileProvider)

  /** [SampleTootWriter] for performing write operations on the [tootProvider]. */
  val tootWriter = SampleTootWriter(tootProvider)
}
