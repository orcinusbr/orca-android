/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.composite.timeline.post.conversion

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PostToPostPreviewConversionScopeTests {
  @Test
  fun resetsAfterTest() {
    lateinit var scope: PostToPostPreviewConversionScope
    runPostToPostPreviewConversionTest {
      scope = this
      post.favorite.toggle()
    }
    assertThat(scope.post).prop(Post::favorite).all {
      val createdPost = scope.createPost()
      prop(ToggleableStat<Profile>::isEnabled).isEqualTo(createdPost.favorite.isEnabled)
      prop(ToggleableStat<Profile>::count).isEqualTo(createdPost.favorite.count)
    }
  }
}
