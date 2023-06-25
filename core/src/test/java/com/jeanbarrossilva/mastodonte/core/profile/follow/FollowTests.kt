package com.jeanbarrossilva.mastodonte.core.profile.follow

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class FollowTests {
    @Test
    fun `GIVEN a public unfollowed status WHEN toggling it THEN it's followed`() {
        assertEquals(Follow.Public.following(), Follow.Public.unfollowed().toggled())
    }

    @Test
    fun `GIVEN a public followed status WHEN toggling it THEN it's unfollowed`() {
        assertEquals(Follow.Public.unfollowed(), Follow.Public.following().toggled())
    }

    @Test
    fun `GIVEN a private unfollowed status WHEN toggling it THEN it's requested`() {
        assertEquals(Follow.Private.requested(), Follow.Private.unfollowed().toggled())
    }

    @Test
    fun `GIVEN a private requested status WHEN toggling it THEN it's unfollowed`() {
        assertEquals(Follow.Private.unfollowed(), Follow.Private.requested().toggled())
    }

    @Test
    fun `GIVEN a cohesive status WHEN requiring it to be cohesive THEN it is`() {
        Follow.requireVisibilityMatch(Follow.Public.unfollowed(), Follow.Public.following())
    }

    @Test
    fun `GIVEN a non-cohesive status WHEN requiring it to be cohesive THEN it throws`() {
        assertFailsWith<IllegalArgumentException> {
            Follow.requireVisibilityMatch(Follow.Public.unfollowed(), Follow.Private.unfollowed())
        }
    }
}
