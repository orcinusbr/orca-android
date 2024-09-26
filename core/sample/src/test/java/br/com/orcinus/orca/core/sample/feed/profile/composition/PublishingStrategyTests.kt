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

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfile
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composers.compose
import br.com.orcinus.orca.core.sample.feed.profile.composition.TimedComposition.PublishingStrategy.asOwned
import br.com.orcinus.orca.core.sample.feed.profile.composition.TimedComposition.PublishingStrategy.asRepostFrom
import br.com.orcinus.orca.core.sample.feed.profile.post.createChristianSample
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.core.sample.test.feed.profile.post.sample
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.std.markdown.Markdown
import java.time.ZonedDateTime
import kotlin.test.Test

internal class PublishingStrategyTests {
  @Test
  fun independentPublishingCreatesAPostComposedByTheComposer() {
    val profileDelegate = Author.sample
    val timedComposition =
      object : SampleProfile(delegate = profileDelegate) {
          override val bio = Markdown.empty
          override val followerCount = 0
          override val followingCount = 0
        }
        .compose(Content.empty)
        .on(ZonedDateTime.now())
    assertThat(asOwned().createPost(timedComposition)).prop(Post::author).isEqualTo(profileDelegate)
  }

  @Test
  fun repostPublishingCreatesARepost() {
    val author = Author.createChristianSample(NoOpSampleImageLoader.Provider)
    val reposterDelegate = Author.sample
    val timedComposition =
      object : SampleProfile(delegate = reposterDelegate) {
          override val bio = Markdown.empty
          override val followerCount = 0
          override val followingCount = 0
        }
        .compose(Content.empty)
        .on(ZonedDateTime.now())
    assertThat(asRepostFrom(author).createPost(timedComposition)).isInstanceOf<Repost>().all {
      prop(Repost::author).isEqualTo(author)
      prop(Repost::reposter).isEqualTo(reposterDelegate)
    }
  }
}
