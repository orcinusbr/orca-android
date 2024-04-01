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

package br.com.orcinus.orca.feature.profiledetails.conversion.converter.followable

import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.feature.profiledetails.ProfileDetails
import org.junit.Assert.assertEquals
import org.junit.Test

internal class FollowExtensionsTests {
  @Test
  fun `GIVEN a public unfollowed Follow WHEN converting it into a Status THEN it's UNFOLLOWED`() {
    assertEquals(ProfileDetails.Followable.Status.UNFOLLOWED, Follow.Public.unfollowed().toStatus())
  }

  @Test
  fun `GIVEN a private unfollowed Follow WHEN converting it into a Status THEN it's UNFOLLOWED`() {
    assertEquals(
      ProfileDetails.Followable.Status.UNFOLLOWED,
      Follow.Private.unfollowed().toStatus()
    )
  }

  @Test
  fun `GIVEN a requested Follow WHEN converting it into a Status THEN it's REQUESTED`() {
    assertEquals(ProfileDetails.Followable.Status.REQUESTED, Follow.Private.requested().toStatus())
  }

  @Test
  fun `GIVEN a public following Follow WHEN converting it into a Status THEN it's FOLLOWING`() {
    assertEquals(ProfileDetails.Followable.Status.FOLLOWING, Follow.Public.following().toStatus())
  }

  @Test
  fun `GIVEN a private following Follow WHEN converting it into a Status THEN it's FOLLOWING`() {
    assertEquals(ProfileDetails.Followable.Status.FOLLOWING, Follow.Private.following().toStatus())
  }
}
