package com.jeanbarrossilva.mastodonte.feature.profiledetails.conversion.converter.followable

import com.jeanbarrossilva.mastodonte.core.profile.type.follow.Follow
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetails
import org.junit.Assert.assertEquals
import org.junit.Test

internal class FollowExtensionsTests {
    @Test
    fun `GIVEN a public unfollowed Follow WHEN converting it into a Status THEN it's UNFOLLOWED`() {
        assertEquals(
            ProfileDetails.Followable.Status.UNFOLLOWED,
            Follow.Public.unfollowed().toStatus()
        )
    }

    @Test
    fun `GIVEN a private unfollowed Follow WHEN converting it into a Status THEN it's UNFOLLOWED`() { // ktlint-disable max-line-length
        assertEquals(
            ProfileDetails.Followable.Status.UNFOLLOWED,
            Follow.Private.unfollowed().toStatus()
        )
    }

    @Test
    fun `GIVEN a requested Follow WHEN converting it into a Status THEN it's REQUESTED`() {
        assertEquals(
            ProfileDetails.Followable.Status.REQUESTED,
            Follow.Private.requested().toStatus()
        )
    }

    @Test
    fun `GIVEN a public following Follow WHEN converting it into a Status THEN it's FOLLOWING`() {
        assertEquals(
            ProfileDetails.Followable.Status.FOLLOWING,
            Follow.Public.following().toStatus()
        )
    }

    @Test
    fun `GIVEN a private following Follow WHEN converting it into a Status THEN it's FOLLOWING`() {
        assertEquals(
            ProfileDetails.Followable.Status.FOLLOWING,
            Follow.Private.following().toStatus()
        )
    }
}
