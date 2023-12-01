/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable

import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
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
