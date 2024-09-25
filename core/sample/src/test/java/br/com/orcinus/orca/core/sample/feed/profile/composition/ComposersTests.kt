/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.sample.feed.profile.composition

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotInstanceOf
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfile
import br.com.orcinus.orca.core.sample.feed.profile.account.sample
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composers.compose
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composers.getMainPostOrThrow
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composers.posts
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composers.toAuthor
import br.com.orcinus.orca.core.sample.feed.profile.composition.TimedComposition.PublishingStrategy.asOwned
import br.com.orcinus.orca.core.sample.feed.profile.composition.TimedComposition.PublishingStrategy.asRepostFrom
import br.com.orcinus.orca.core.sample.feed.profile.post.content.sample
import br.com.orcinus.orca.core.sample.feed.profile.post.createChristianSample
import br.com.orcinus.orca.core.sample.image.AuthorImageSource
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.core.sample.test.feed.profile.post.sample
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.markdown.Markdown
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.Test

internal class ComposersTests {
  @Test
  fun getsPosts() {
    val publishedPosts = arrayOfNulls<Post>(2)
    assertThat(
        object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          }
          .apply {
            compose(Content.from(Domain.sample, text = Markdown.empty) { null })
              .on(ZonedDateTime.now())
              .publish(asOwned())
              .also { publishedPosts[0] = it.get() }
              .and()
              .compose(Content.sample)
              .on(ZonedDateTime.now())
              .publish(asOwned())
              .also { publishedPosts[1] = it.get() }
          }
          .posts
      )
      .containsExactly(*publishedPosts)
  }

  @Test
  fun composes() {
    val profile =
      object : SampleProfile(delegate = Author.sample) {
        override val bio = Markdown.empty
        override val followerCount = 0
        override val followingCount = 0
      }
    val content = Content.from(Domain.sample, text = Markdown.empty) { null }
    assertThat(profile.compose(content)).isEqualTo(Composition(profile, content))
  }

  @Test
  fun convertsIntoAnAuthor() {
    val delegate = Author.sample
    assertThat(
        object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          }
          .toAuthor()
      )
      .isEqualTo(delegate)
  }

  @Test
  fun composersWithDistinctIDsAreNotEqual() {
    assertThat(
        Composers.equals(
          object : SampleProfile(delegate = Author.sample) {
            override val id = UUID.randomUUID().toString()
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          },
          object : SampleProfile(delegate = Author.sample) {
            override val id = UUID.randomUUID().toString()
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          }
        )
      )
      .isFalse()
  }

  @Test
  fun composersWithDistinctAccountsAreNotEqual() {
    assertThat(
        Composers.equals(
          object : SampleProfile(delegate = Author.sample) {
            override val account = Account.sample
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          },
          object : SampleProfile(delegate = Author.sample) {
            override val account =
              Author.createChristianSample(NoOpSampleImageLoader.Provider).account
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          }
        )
      )
      .isFalse()
  }

  @Test
  fun composersWithDistinctAvatarLoadersAreNotEqual() {
    assertThat(
        Composers.equals(
          object : SampleProfile(delegate = Author.sample) {
            override val avatarLoader =
              NoOpSampleImageLoader.Provider.provide(AuthorImageSource.Default)
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          },
          object : SampleProfile(delegate = Author.sample) {
            override val avatarLoader =
              object : ImageLoader<SampleImageSource, Unit> {
                override val source = AuthorImageSource.Default

                override fun load() {}
              }
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          }
        )
      )
      .isFalse()
  }

  @Test
  fun composersWithDistinctNamesAreNotEqual() {
    assertThat(
        Composers.equals(
          object : SampleProfile(delegate = Author.sample) {
            override val name = Author.sample.name
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          },
          object : SampleProfile(delegate = Author.sample) {
            override val name = Author.createChristianSample(NoOpSampleImageLoader.Provider).name
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          }
        )
      )
      .isFalse()
  }

  @Test
  fun composersWithDistinctBiosAreNotEqual() {
    assertThat(
        Composers.equals(
          object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.unstyled("🥸")
            override val followerCount = 0
            override val followingCount = 0
          },
          object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.unstyled("😀")
            override val followerCount = 0
            override val followingCount = 0
          }
        )
      )
      .isFalse()
  }

  @Test
  fun composersWithDistinctFollowerCountsAreNotEqual() {
    assertThat(
        Composers.equals(
          object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          },
          object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.empty
            override val followerCount = 1
            override val followingCount = 0
          }
        )
      )
      .isFalse()
  }

  @Test
  fun composersWithDistinctFollowingCountsAreNotEqual() {
    assertThat(
        Composers.equals(
          object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          },
          object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 1
          }
        )
      )
      .isFalse()
  }

  @Test
  fun composersWithDistinctUrisAreNotEqual() {
    assertThat(
        Composers.equals(
          object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
            override val uri = Author.sample.profileURI
          },
          object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
            override val uri =
              Author.createChristianSample(NoOpSampleImageLoader.Provider).profileURI
          }
        )
      )
      .isFalse()
  }

  @Test
  fun composersWithDistinctPostsAreNotEqual() {
    assertThat(
        Composers.equals(
          object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          },
          object : SampleProfile(delegate = Author.sample) {
              override val bio = Markdown.empty
              override val followerCount = 0
              override val followingCount = 0
            }
            .apply { compose(Content.sample).on(ZonedDateTime.now()).publish(asOwned()).get() }
        )
      )
      .isFalse()
  }

  @Test
  fun equals() {
    assertThat(
        Composers.equals(
          object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          },
          object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          }
        )
      )
      .isTrue()
  }

  @Test(expected = NoSuchElementException::class)
  fun throwsWhenObtainingMainPostAndItIsNotFound() {
    object : SampleProfile(delegate = Author.sample) {
        override val bio = Markdown.empty
        override val followerCount = 0
        override val followingCount = 0
      }
      .apply {
        compose(Content.from(Domain.sample, text = Markdown.empty) { null })
          .on(ZonedDateTime.now())
          .publish(asRepostFrom(Author.createChristianSample(NoOpSampleImageLoader.Provider)))
      }
      .getMainPostOrThrow()
  }

  @Test
  fun mainPostOfAComposerIsNotARepost() {
    assertThat(
        object : SampleProfile(delegate = Author.sample) {
            override val bio = Markdown.empty
            override val followerCount = 0
            override val followingCount = 0
          }
          .apply {
            compose(Content.from(Domain.sample, text = Markdown.empty) { null })
              .on(ZonedDateTime.now())
              .publish(asRepostFrom(Author.createChristianSample(NoOpSampleImageLoader.Provider)))
              .and()
              .compose(Content.from(Domain.sample, text = Markdown.empty) { null })
              .on(ZonedDateTime.now())
              .publish(asOwned())
              .get()
          }
          .getMainPostOrThrow()
      )
      .isNotInstanceOf<Repost>()
  }
}
