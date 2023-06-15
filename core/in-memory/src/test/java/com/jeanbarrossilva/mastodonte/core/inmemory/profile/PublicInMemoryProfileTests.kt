package com.jeanbarrossilva.mastodonte.core.inmemory.profile

import com.jeanbarrossilva.mastodonte.core.inmemory.test.assertTogglingEquals
import com.jeanbarrossilva.mastodonte.core.profile.Follow
import kotlin.test.Test

internal class PublicInMemoryProfileTests {
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
