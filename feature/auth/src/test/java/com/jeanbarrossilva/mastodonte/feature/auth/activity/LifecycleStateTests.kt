package com.jeanbarrossilva.mastodonte.feature.auth.activity

import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test

internal class LifecycleStateTests {
    @Test
    fun `GIVEN a null state WHEN checking if it's at least created THEN it isn't`() {
        assertFalse(
            (null as AuthActivity.LifecycleState?).isAtLeast(AuthActivity.LifecycleState.CREATED)
        )
    }

    @Test
    fun `GIVEN a null state WHEN checking if it's at least started THEN it isn't`() {
        assertFalse(
            (null as AuthActivity.LifecycleState?).isAtLeast(AuthActivity.LifecycleState.STARTED)
        )
    }

    @Test
    fun `GIVEN a null state WHEN checking if it's at least resumed THEN it isn't`() {
        assertFalse(
            (null as AuthActivity.LifecycleState?).isAtLeast(AuthActivity.LifecycleState.RESUMED)
        )
    }

    @Test
    fun `GIVEN a null state WHEN checking if it's at least paused THEN it isn't`() {
        assertFalse(
            (null as AuthActivity.LifecycleState?).isAtLeast(AuthActivity.LifecycleState.PAUSED)
        )
    }

    @Test
    fun `GIVEN a null state WHEN checking if it's at least stopped THEN it isn't`() {
        assertFalse(
            (null as AuthActivity.LifecycleState?).isAtLeast(AuthActivity.LifecycleState.STOPPED)
        )
    }

    @Test
    fun `GIVEN a null state WHEN checking if it's at least destroyed THEN it isn't`() {
        assertFalse(
            (null as AuthActivity.LifecycleState?).isAtLeast(AuthActivity.LifecycleState.DESTROYED)
        )
    }

    @Test
    fun `GIVEN a created state WHEN checking if it's at least created THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.CREATED.isAtLeast(AuthActivity.LifecycleState.CREATED)
        )
    }

    @Test
    fun `GIVEN a created state WHEN checking if it's at least started THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.CREATED.isAtLeast(AuthActivity.LifecycleState.STARTED)
        )
    }

    @Test
    fun `GIVEN a created state WHEN checking if it's at least resumed THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.CREATED.isAtLeast(AuthActivity.LifecycleState.RESUMED)
        )
    }

    @Test
    fun `GIVEN a created state WHEN checking if it's at least paused THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.CREATED.isAtLeast(AuthActivity.LifecycleState.PAUSED)
        )
    }

    @Test
    fun `GIVEN a created state WHEN checking if it's at least stopped THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.CREATED.isAtLeast(AuthActivity.LifecycleState.STOPPED)
        )
    }

    @Test
    fun `GIVEN a created state WHEN checking if it's at least destroyed THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.CREATED.isAtLeast(AuthActivity.LifecycleState.DESTROYED)
        )
    }

    @Test
    fun `GIVEN a started state WHEN checking if it's at least created THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.STARTED.isAtLeast(AuthActivity.LifecycleState.CREATED)
        )
    }

    @Test
    fun `GIVEN a started state WHEN checking if it's at least started THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.STARTED.isAtLeast(AuthActivity.LifecycleState.STARTED)
        )
    }

    @Test
    fun `GIVEN a started state WHEN checking if it's at least resumed THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.STARTED.isAtLeast(AuthActivity.LifecycleState.RESUMED)
        )
    }

    @Test
    fun `GIVEN a started state WHEN checking if it's at least paused THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.STARTED.isAtLeast(AuthActivity.LifecycleState.PAUSED)
        )
    }

    @Test
    fun `GIVEN a started state WHEN checking if it's at least stopped THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.STARTED.isAtLeast(AuthActivity.LifecycleState.STOPPED)
        )
    }

    @Test
    fun `GIVEN a started state WHEN checking if it's at least destroyed THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.STARTED.isAtLeast(AuthActivity.LifecycleState.DESTROYED)
        )
    }

    @Test
    fun `GIVEN a resumed state WHEN checking if it's at least created THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.RESUMED.isAtLeast(AuthActivity.LifecycleState.CREATED)
        )
    }

    @Test
    fun `GIVEN a resumed state WHEN checking if it's at least started THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.RESUMED.isAtLeast(AuthActivity.LifecycleState.STARTED)
        )
    }

    @Test
    fun `GIVEN a resumed state WHEN checking if it's at least resumed THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.RESUMED.isAtLeast(AuthActivity.LifecycleState.RESUMED)
        )
    }

    @Test
    fun `GIVEN a resumed state WHEN checking if it's at least paused THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.RESUMED.isAtLeast(AuthActivity.LifecycleState.PAUSED)
        )
    }

    @Test
    fun `GIVEN a resumed state WHEN checking if it's at least stopped THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.RESUMED.isAtLeast(AuthActivity.LifecycleState.STOPPED)
        )
    }

    @Test
    fun `GIVEN a resumed state WHEN checking if it's at least destroyed THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.RESUMED.isAtLeast(AuthActivity.LifecycleState.DESTROYED)
        )
    }

    @Test
    fun `GIVEN a paused state WHEN checking if it's at least created THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.PAUSED.isAtLeast(AuthActivity.LifecycleState.CREATED)
        )
    }

    @Test
    fun `GIVEN a paused state WHEN checking if it's at least started THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.PAUSED.isAtLeast(AuthActivity.LifecycleState.STARTED)
        )
    }

    @Test
    fun `GIVEN a paused state WHEN checking if it's at least resumed THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.PAUSED.isAtLeast(AuthActivity.LifecycleState.RESUMED)
        )
    }

    @Test
    fun `GIVEN a paused state WHEN checking if it's at least paused THEN it is`() {
        assertTrue(AuthActivity.LifecycleState.PAUSED.isAtLeast(AuthActivity.LifecycleState.PAUSED))
    }

    @Test
    fun `GIVEN a paused state WHEN checking if it's at least stopped THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.PAUSED.isAtLeast(AuthActivity.LifecycleState.STOPPED)
        )
    }

    @Test
    fun `GIVEN a paused state WHEN checking if it's at least destroyed THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.PAUSED.isAtLeast(AuthActivity.LifecycleState.DESTROYED)
        )
    }

    @Test
    fun `GIVEN a stopped state WHEN checking if it's at least created THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.STOPPED.isAtLeast(AuthActivity.LifecycleState.CREATED)
        )
    }

    @Test
    fun `GIVEN a stopped state WHEN checking if it's at least started THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.STOPPED.isAtLeast(AuthActivity.LifecycleState.STARTED)
        )
    }

    @Test
    fun `GIVEN a stopped state WHEN checking if it's at least resumed THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.STOPPED.isAtLeast(AuthActivity.LifecycleState.RESUMED)
        )
    }

    @Test
    fun `GIVEN a stopped state WHEN checking if it's at least paused THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.STOPPED.isAtLeast(AuthActivity.LifecycleState.PAUSED)
        )
    }

    @Test
    fun `GIVEN a stopped state WHEN checking if it's at least stopped THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.STOPPED.isAtLeast(AuthActivity.LifecycleState.STOPPED)
        )
    }

    @Test
    fun `GIVEN a stopped state WHEN checking if it's at least destroyed THEN it isn't`() {
        assertFalse(
            AuthActivity.LifecycleState.STOPPED.isAtLeast(AuthActivity.LifecycleState.DESTROYED)
        )
    }

    @Test
    fun `GIVEN a destroyed state WHEN checking if it's at least created THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.DESTROYED.isAtLeast(AuthActivity.LifecycleState.CREATED)
        )
    }

    @Test
    fun `GIVEN a destroyed state WHEN checking if it's at least started THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.DESTROYED.isAtLeast(AuthActivity.LifecycleState.STARTED)
        )
    }

    @Test
    fun `GIVEN a destroyed state WHEN checking if it's at least resumed THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.DESTROYED.isAtLeast(AuthActivity.LifecycleState.RESUMED)
        )
    }

    @Test
    fun `GIVEN a destroyed state WHEN checking if it's at least paused THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.DESTROYED.isAtLeast(AuthActivity.LifecycleState.PAUSED)
        )
    }

    @Test
    fun `GIVEN a destroyed state WHEN checking if it's at least stopped THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.DESTROYED.isAtLeast(AuthActivity.LifecycleState.STOPPED)
        )
    }

    @Test
    fun `GIVEN a destroyed state WHEN checking if it's at least destroyed THEN it is`() {
        assertTrue(
            AuthActivity.LifecycleState.DESTROYED.isAtLeast(AuthActivity.LifecycleState.DESTROYED)
        )
    }
}
