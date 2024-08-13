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

package br.com.orcinus.orca.core.sample.instance

import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.Authenticator
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.FeedProvider
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.ProfileProvider
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.PostProvider
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Headline
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Highlight
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearcher
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticationLock
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticator
import br.com.orcinus.orca.core.sample.auth.actor.SampleActorProvider
import br.com.orcinus.orca.core.sample.auth.actor.sample
import br.com.orcinus.orca.core.sample.feed.SampleFeedProvider
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePost
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.christianSampleAuthorID
import br.com.orcinus.orca.core.sample.feed.profile.post.content.SampleTermMuter
import br.com.orcinus.orca.core.sample.feed.profile.post.content.highlight.createSample
import br.com.orcinus.orca.core.sample.feed.profile.post.content.sample
import br.com.orcinus.orca.core.sample.feed.profile.post.createChristianSample
import br.com.orcinus.orca.core.sample.feed.profile.post.createRamboSample
import br.com.orcinus.orca.core.sample.feed.profile.post.createSample
import br.com.orcinus.orca.core.sample.feed.profile.post.ramboSampleAuthorID
import br.com.orcinus.orca.core.sample.feed.profile.search.SampleProfileSearcher
import br.com.orcinus.orca.core.sample.feed.profile.type.editable.SampleEditableProfile
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.SampleFollowableProfile
import br.com.orcinus.orca.core.sample.image.CoverImageSource
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import java.time.ZoneId
import java.time.ZonedDateTime

/** [Instance] made out of sample underlying core structures. */
class SampleInstance(
  override val authenticator: SampleAuthenticator,
  override val authenticationLock: SampleAuthenticationLock,
  override val feedProvider: SampleFeedProvider,
  override val profileProvider: SampleProfileProvider,
  override val profileSearcher: SampleProfileSearcher,
  override val postProvider: SamplePostProvider
) : Instance<SampleAuthenticator>() {
  override val domain = Domain.sample

  /**
   * Allows for a [SampleInstance] to be built. Also ensures that each domain structure whose
   * behavior is based on mutability (as is that of a [Profile] or a [Post]) contains requested
   * changes only within the built [SampleInstance] instead of having them be globally accessible
   * objects.
   *
   * @see build
   */
  sealed class Builder {
    /** [Instance]-specific [Authenticator] through which authentication can be done. */
    protected abstract val authenticator: SampleAuthenticator

    /**
     * [Instance]-specific [AuthenticationLock] by which features can be locked or unlocked by an
     * authentication "wall".
     */
    protected abstract val authenticationLock: SampleAuthenticationLock

    /** [Instance]-specific [FeedProvider] that provides the [Post]s in the timeline. */
    protected abstract val feedProvider: SampleFeedProvider

    /** [Instance]-specific [ProfileProvider] for providing [Profile]s. */
    protected abstract val profileProvider: SampleProfileProvider

    /** [Instance]-specific [ProfileSearcher] by which search for [Profile]s can be made. */
    protected abstract val profileSearcher: SampleProfileSearcher

    /** [Instance]-specific [PostProvider] that provides [Post]s. */
    protected abstract val postProvider: SamplePostProvider

    /** Provides the [ImageLoader] by which images are to be loaded from a [SampleImageSource]. */
    protected abstract val imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>

    /** [Builder] without any default [Profile]s or [Post]s. */
    class Empty
    internal constructor(
      override val authenticator: SampleAuthenticator,
      override val authenticationLock: SampleAuthenticationLock,
      override val feedProvider: SampleFeedProvider,
      override val profileProvider: SampleProfileProvider,
      override val profileSearcher: SampleProfileSearcher,
      override val postProvider: SamplePostProvider,
      override val imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
    ) : Builder() {
      /** Denotes that the built [SampleInstance] should include default [Profile]s. */
      fun withDefaultProfiles(): DefaultProfiles {
        return DefaultProfiles(
          authenticator,
          authenticationLock,
          feedProvider,
          profileSearcher,
          profileProvider,
          postProvider,
          imageLoaderProvider
        )
      }
    }

    /**
     * [Builder] that includes default [Profile]s in the built [SampleInstance]. Consequently,
     * allows for default [Post]s to be published in those and for them to also be present when
     * provided by the [SamplePostProvider].
     *
     * @see withDefaultPosts
     */
    class DefaultProfiles
    internal constructor(
      override val authenticator: SampleAuthenticator,
      override val authenticationLock: SampleAuthenticationLock,
      override val feedProvider: SampleFeedProvider,
      override val profileSearcher: SampleProfileSearcher,
      override val profileProvider: SampleProfileProvider,
      override val postProvider: SamplePostProvider,
      override val imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
    ) : Builder() {
      init {
        profileProvider.add(
          SampleEditableProfile(
            profileProvider,
            postProvider,
            Author.createSample(imageLoaderProvider),
            bio = Markdown.unstyled("Founder of Orcinus, software engineer and content creator."),
            followerCount = 1_024,
            followingCount = 64
          ),
          SampleFollowableProfile(
            profileProvider,
            postProvider,
            Author.createChristianSample(imageLoaderProvider),
            bio =
              Markdown.unstyled(
                "iOS developer, creator of Apollo and Pixel Pals. Previously at \uF8FF. he/him. " +
                  "i love animals. 🌱"
              ),
            Follow.Public.following(),
            followerCount = 45_113,
            followingCount = 478
          ),
          SampleFollowableProfile(
            profileProvider,
            postProvider,
            Author.createRamboSample(imageLoaderProvider),
            bio =
              Markdown.unstyled(
                "I know a thing or two about AirPods. App developer, security researcher. 🏳️‍🌈🧩"
              ),
            Follow.Public.following(),
            followerCount = 12_641,
            followingCount = 239
          )
        )
      }

      /** Denotes that the built [SampleInstance] should include default [Post]s. */
      fun withDefaultPosts(): DefaultPosts {
        return DefaultPosts(
          authenticator,
          authenticationLock,
          feedProvider,
          profileSearcher,
          profileProvider,
          postProvider,
          imageLoaderProvider
        )
      }
    }

    /** [Builder] that includes default [Post]s in the built [SampleInstance]. */
    class DefaultPosts
    internal constructor(
      override val authenticator: SampleAuthenticator,
      override val authenticationLock: SampleAuthenticationLock,
      override val feedProvider: SampleFeedProvider,
      override val profileSearcher: SampleProfileSearcher,
      override val profileProvider: SampleProfileProvider,
      override val postProvider: SamplePostProvider,
      override val imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
    ) : Builder() {
      init {
        postProvider.add(
          SamplePost(
            postProvider,
            owner = profileProvider.provideCurrent(Actor.Authenticated.sample.id),
            Content.sample,
            publicationDateTime = ZonedDateTime.of(2_003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3"))
          ),
          Repost(
            SamplePost(
              postProvider,
              owner = profileProvider.provideCurrent(ramboSampleAuthorID),
              Content.from(
                Domain.sample,
                text =
                  Markdown.unstyled(
                    "Programming life hack. Looking for real-world examples of how an API is " +
                      "used? Search for code on GitHub like so: “APINameHere path:*.extension”. " +
                      "Practical example for a MusicKit API in Swift: " +
                      "“MusicCatalogResourceRequest extension:*.swift”. I can usually find lots " +
                      "of examples to help me get things going without having to read the entire " +
                      "documentation for a given API."
                  )
              ) {
                null
              },
              publicationDateTime =
                ZonedDateTime.of(2023, 8, 16, 16, 48, 43, 384, ZoneId.of("GMT-3"))
            ),
            reblogger = Author.createSample(imageLoaderProvider)
          ),
          SamplePost(
            postProvider,
            owner = profileProvider.provideCurrent(Actor.Authenticated.sample.id),
            Content.from(
              Domain.sample,
              text =
                buildMarkdown {
                  +"Great talk about the importance of introverts in an extroversion-seeking world."
                  +"\n".repeat(2)
                  +"https://www.ted.com/talks/susan_cain_the_power_of_introverts"
                }
            ) {
              Headline(
                title = "The power of introverts",
                subtitle =
                  "In a culture where being social and outgoing are prized above all else, it " +
                    "can be difficult, even shameful, to be an introvert. But, as Susan Cain " +
                    "argues in this passionate talk, introverts bring extraordinary talents " +
                    "abilities to the world, and should be encouraged and celebrated.",
                imageLoaderProvider.provide(CoverImageSource.ThePowerOfIntroverts)
              )
            },
            publicationDateTime = ZonedDateTime.of(2_024, 4, 5, 9, 32, 0, 0, ZoneId.of("GMT-3")),
          ),
          SamplePost(
            postProvider,
            profileProvider.provideCurrent(christianSampleAuthorID),
            Content.from(
              Domain.sample,
              text =
                buildMarkdown {
                  +("Also, last day to get Pixel Pals premium at a discount and last day for the " +
                    "lifetime unlock to be available!")
                  +"\n".repeat(2)
                  +Highlight.createSample(imageLoaderProvider).uri.toString()
                }
            ) {
              Headline.createSample(imageLoaderProvider)
            },
            publicationDateTime =
              ZonedDateTime.of(2_023, 11, 27, 18, 26, 0, 0, ZoneId.of("America/Halifax"))
          )
        )
      }
    }

    /** Builds a [SampleInstance]. */
    fun build(): SampleInstance {
      return SampleInstance(
        authenticator,
        authenticationLock,
        feedProvider,
        profileProvider,
        profileSearcher,
        postProvider
      )
    }

    companion object {
      /**
       * Creates a [Builder].
       *
       * @param imageLoaderProvider Provides the [ImageLoader] by which images are to be loaded from
       *   a [SampleImageSource].
       */
      fun create(imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>): Empty {
        val authenticator = SampleAuthenticator()
        val actorProvider = SampleActorProvider()
        val authenticationLock = SampleAuthenticationLock(authenticator, actorProvider)
        val profileProvider = SampleProfileProvider()
        val profileSearcher = SampleProfileSearcher(profileProvider)
        val postProvider = SamplePostProvider(authenticationLock)
        val termMuter = SampleTermMuter()
        val feedProvider =
          SampleFeedProvider(profileProvider, postProvider, termMuter, imageLoaderProvider)
        return create(
          authenticator,
          authenticationLock,
          feedProvider,
          profileProvider,
          profileSearcher,
          postProvider,
          imageLoaderProvider
        )
      }

      /**
       * Creates a [Builder].
       *
       * @param authenticator [Instance]-specific [Authenticator] through which authentication can
       *   be done.
       * @param authenticationLock [Instance]-specific [AuthenticationLock] by which features can be
       *   locked or unlocked by an authentication "wall".
       * @param feedProvider [Instance]-specific [FeedProvider] that provides the [Post]s in the
       *   timeline.
       * @param profileProvider [Instance]-specific [ProfileProvider] for providing [Profile]s.
       * @param profileSearcher [Instance]-specific [ProfileSearcher] by which search for [Profile]s
       *   can be made.
       * @param postProvider [Instance]-specific [PostProvider] that provides [Post]s.
       * @param imageLoaderProvider Provides the [ImageLoader] by which images are to be loaded from
       *   a [SampleImageSource].
       */
      fun create(
        authenticator: SampleAuthenticator,
        authenticationLock: SampleAuthenticationLock,
        feedProvider: SampleFeedProvider,
        profileProvider: SampleProfileProvider,
        profileSearcher: SampleProfileSearcher,
        postProvider: SamplePostProvider,
        imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
      ): Empty {
        return Empty(
          authenticator,
          authenticationLock,
          feedProvider,
          profileProvider,
          profileSearcher,
          postProvider,
          imageLoaderProvider
        )
      }
    }
  }
}
