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
import assertk.assertions.isSameAs
import assertk.assertions.prop
import assertk.assertions.single
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composers.compose
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composers.toAuthor
import br.com.orcinus.orca.core.sample.feed.profile.composition.TimedComposition.PublishingStrategy.asOwned
import br.com.orcinus.orca.core.sample.feed.profile.composition.TimedComposition.PublishingStrategy.asRepostFrom
import br.com.orcinus.orca.core.sample.feed.profile.post.createChristianSample
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.std.markdown.Markdown
import java.time.ZonedDateTime
import kotlin.test.Test

internal class TimedCompositionTests {
  @Test
  fun posts() {
    val composer = HardcodedComposer()
    val content = Content.from(Domain.sample, text = Markdown.empty) { null }
    val dateTime = ZonedDateTime.now()
    composer.compose(content).on(dateTime).publish(asOwned())
    assertThat(composer.postsFlow.value).single().all {
      prop(Post::content).isSameAs(content)
      prop(Post::publicationDateTime).isSameAs(dateTime)
    }
  }

  @Test
  fun reposts() {
    val composer = HardcodedComposer()
    val content = Content.from(Domain.sample, text = Markdown.empty) { null }
    val dateTime = ZonedDateTime.now()
    composer
      .compose(content)
      .on(dateTime)
      .publish(asRepostFrom(Author.createChristianSample(NoOpSampleImageLoader.Provider)))
    assertThat(composer.postsFlow.value).single().isInstanceOf<Repost>().all {
      prop(Repost::content).isSameAs(content)
      prop(Repost::publicationDateTime).isSameAs(dateTime)
      prop(Repost::reposter).isEqualTo(composer.toAuthor())
    }
  }
}
