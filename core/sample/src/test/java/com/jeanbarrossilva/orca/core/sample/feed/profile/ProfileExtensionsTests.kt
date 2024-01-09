/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.Posts
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.withSamples
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

internal class ProfileExtensionsTests {
  @Test
  fun `GIVEN a sample profile WHEN getting its posts THEN they are the sample ones`() {
    runTest {
      assertContentEquals(
        Posts.withSamples.filter { it.author == Author.sample }.take(SampleProfile.POSTS_PER_PAGE),
        Profile.sample.getPosts(0).first()
      )
    }
  }
}
