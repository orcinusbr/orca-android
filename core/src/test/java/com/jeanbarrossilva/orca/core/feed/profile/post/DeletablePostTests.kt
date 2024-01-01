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

package com.jeanbarrossilva.orca.core.feed.profile.post

import assertk.assertThat
import assertk.assertions.isSameAs
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.Posts
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.withSample
import com.jeanbarrossilva.orca.core.test.TestAuthenticationLock
import com.jeanbarrossilva.testing.hasPropertiesEqualToThoseOf
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class DeletablePostTests {
  @Test
  fun delegatesNonDeletionFunctionalityToDelegate() {
    assertThat(
        object : DeletablePost(Posts.withSample.single()) {
          override suspend fun delete() {}
        }
      )
      .hasPropertiesEqualToThoseOf(Posts.withSample)
  }

  @Test
  fun returnsItselfWhenConvertingItIntoDeletablePost() {
    val authenticationLock = TestAuthenticationLock()
    val post =
      object : DeletablePost(Posts.withSample.single()) {
        override suspend fun delete() {}
      }
    runTest { assertThat(post.asDeletableOrThis(authenticationLock)).isSameAs(post) }
  }

  @Test
  fun isDeleted() {
    var hasBeenDeleted = false
    runTest {
      object : DeletablePost(Posts.withSample.single()) {
          override suspend fun delete() {
            hasBeenDeleted = true
          }
        }
        .delete()
    }
    assertThat(hasBeenDeleted).isTrue()
  }
}
