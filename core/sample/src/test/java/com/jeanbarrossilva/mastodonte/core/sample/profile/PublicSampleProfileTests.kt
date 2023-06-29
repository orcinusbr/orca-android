package com.jeanbarrossilva.mastodonte.core.sample.profile

import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow
import com.jeanbarrossilva.mastodonte.core.sample.test.assertTogglingEquals
import kotlin.test.AfterTest
import kotlin.test.Test

internal class PublicSampleProfileTests {
    @AfterTest
    fun tearDown() {
        SampleProfileDao.clear()
    }

    @Test
    fun `GIVEN a public unfollowed profile WHEN toggling its follow status THEN it's followed`() { // ktlint-disable max-line-length
        assertTogglingEquals(Follow.Public.unfollowed(), Follow.Public.following())
    }

    @Test
    fun `GIVEN a public followed profile WHEN toggling its follow status THEN it's not followed`() {
        assertTogglingEquals(Follow.Public.following(), Follow.Public.unfollowed())
    }

    @Test
    fun `GIVEN a private unfollowed profile WHEN toggling its follow status THEN it's requested`() {
        assertTogglingEquals(Follow.Private.unfollowed(), Follow.Private.requested())
    }

    @Test
    fun `GIVEN a private requested profile WHEN toggling its follow status THEN it's unfollowed`() {
        assertTogglingEquals(Follow.Private.requested(), Follow.Private.unfollowed())
    }

    @Test
    fun `GIVEN a private followed profile WHEN toggling its follow status THEN it's unfollowed`() {
        assertTogglingEquals(Follow.Private.requested(), Follow.Private.unfollowed())
    }
}
