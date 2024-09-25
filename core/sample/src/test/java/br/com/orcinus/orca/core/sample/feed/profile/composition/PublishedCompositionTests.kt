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
import assertk.assertions.first
import assertk.assertions.isSameAs
import assertk.assertions.prop
import assertk.assertions.support.appendName
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composers.compose
import br.com.orcinus.orca.core.sample.feed.profile.composition.TimedComposition.PublishingStrategy.asOwned
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.std.markdown.Markdown
import java.time.ZonedDateTime
import kotlin.test.Test

internal class PublishedCompositionTests {
  @Test
  fun concatenates() {
    val composer = HardcodedComposer()
    val firstContent = Content.from(Domain.sample, text = Markdown.unstyled("1")) { null }
    val firstDateTime = ZonedDateTime.now()
    val secondContent = Content.from(Domain.sample, text = Markdown.unstyled("2")) { null }
    val secondDateTime = firstDateTime.plusNanos(1)
    composer
      .compose(firstContent)
      .on(firstDateTime)
      .publish(asOwned())
      .and()
      .compose(secondContent)
      .on(secondDateTime)
      .publish(asOwned())
    assertThat(composer.postsFlow.value).all {
      first().all {
        prop(Post::content).isSameAs(firstContent)
        prop(Post::publicationDateTime).isSameAs(firstDateTime)
      }
      transform(appendName("last", separator = "."), transform = List<Post>::last).all {
        prop(Post::content).isSameAs(secondContent)
        prop(Post::publicationDateTime).isSameAs(secondDateTime)
      }
    }
  }
}
