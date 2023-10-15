package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable

import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.sample.rule.SampleCoreTestRule
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

internal class FollowExtensionsTests {
  @get:Rule
  val sampleCoreRule = SampleCoreTestRule()

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
