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

package br.com.orcinus.orca.core.feed.profile.type.followable

import assertk.assertThat
import assertk.assertions.isSameAs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class FollowTests {
  @Test
  fun `GIVEN a blank string WHEN parsing it into a follow status THEN it throws`() {
    assertFailsWith<Follow.Companion.BlankStringException> { Follow.of(" ") }
  }

  @Test
  fun `GIVEN an invalid string WHEN parsing it into a follow status THEN it throws`() {
    assertFailsWith<Follow.Companion.InvalidFollowString> { Follow.of("🥸") }
  }

  @Test
  fun `GIVEN a public unfollowed status string WHEN parsing it THEN it returns the status`() {
    assertEquals(Follow.Public.unfollowed(), Follow.of("${Follow.Public.unfollowed()}"))
  }

  @Test
  fun `GIVEN a public following status string WHEN parsing it THEN it returns the status`() {
    assertEquals(Follow.Public.following(), Follow.of("${Follow.Public.following()}"))
  }

  @Test
  fun parsesPublicSubscribedStatusString() {
    assertThat(Follow.of("${Follow.Public.subscribed()}")).isSameAs(Follow.Public.subscribed())
  }

  @Test
  fun `GIVEN a private unfollowed status string WHEN parsing it THEN it returns the status`() {
    assertEquals(Follow.Private.unfollowed(), Follow.of("${Follow.Private.unfollowed()}"))
  }

  @Test
  fun `GIVEN a private requested status string WHEN parsing it THEN it returns the status`() {
    assertEquals(Follow.Private.requested(), Follow.of("${Follow.Private.requested()}"))
  }

  @Test
  fun `GIVEN a private following status string WHEN parsing it THEN it returns the status`() {
    assertEquals(Follow.Private.following(), Follow.of("${Follow.Private.following()}"))
  }

  @Test
  fun parsesPrivateSubscribedStatusString() {
    assertThat(Follow.of("${Follow.Private.subscribed()}")).isSameAs(Follow.Private.subscribed())
  }
}
