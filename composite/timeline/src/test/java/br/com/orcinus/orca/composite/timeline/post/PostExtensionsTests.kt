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

package br.com.orcinus.orca.composite.timeline.post

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.composite.timeline.post.figure.Figure
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.platform.core.withSample
import java.net.URI
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PostExtensionsTests {
  @Test
  fun convertsIntoPostPreview() {
    val post = Posts.withSample.single()
    val onLinkClick = { _: URI -> }
    assertThat(post.toPostPreview(Colors.LIGHT, onLinkClick))
      .isEqualTo(
        PostPreview.getSample(Colors.LIGHT)
          .copy(figure = Figure.of(post.id, post.author.name, post.content, onLinkClick))
      )
  }
}
