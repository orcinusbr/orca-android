package com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle.state

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class CompleteLifecycleStateExtensionsTests {
    @Test
    fun `GIVEN a null state WHEN comparing it to the created state THEN it's less`() {
        assertTrue(null compareTo CompleteLifecycleState.CREATED < 0)
    }

    @Test
    fun `GIVEN a null state WHEN comparing it to the started state THEN it's less`() {
        assertTrue(null compareTo CompleteLifecycleState.STARTED < 0)
    }

    @Test
    fun `GIVEN a null state WHEN comparing it to the resumed state THEN it's less`() {
        assertTrue(null compareTo CompleteLifecycleState.RESUMED < 0)
    }

    @Test
    fun `GIVEN a null state WHEN comparing it to the paused state THEN it's less`() {
        assertTrue(null compareTo CompleteLifecycleState.PAUSED < 0)
    }

    @Test
    fun `GIVEN a null state WHEN comparing it to the stopped state THEN it's less`() {
        assertTrue(null compareTo CompleteLifecycleState.STOPPED < 0)
    }

    @Test
    fun `GIVEN a null state WHEN comparing it to the destroyed state THEN it's less`() {
        assertTrue(null compareTo CompleteLifecycleState.DESTROYED < 0)
    }

    @Test
    fun `GIVEN a null state WHEN checking if it's at least created THEN it isn't`() {
        assertFalse((null as CompleteLifecycleState?).isAtLeast(CompleteLifecycleState.CREATED))
    }

    @Test
    fun `GIVEN a null state WHEN getting the next one THEN it's created`() {
        assertEquals(CompleteLifecycleState.CREATED, null.next())
    }

    @Test
    fun `GIVEN a null state WHEN checking if it's at least started THEN it isn't`() {
        assertFalse((null as CompleteLifecycleState?).isAtLeast(CompleteLifecycleState.STARTED))
    }

    @Test
    fun `GIVEN a null state WHEN checking if it's at least resumed THEN it isn't`() {
        assertFalse((null as CompleteLifecycleState?).isAtLeast(CompleteLifecycleState.RESUMED))
    }

    @Test
    fun `GIVEN a null state WHEN checking if it's at least paused THEN it isn't`() {
        assertFalse((null as CompleteLifecycleState?).isAtLeast(CompleteLifecycleState.PAUSED))
    }

    @Test
    fun `GIVEN a null state WHEN checking if it's at least stopped THEN it isn't`() {
        assertFalse((null as CompleteLifecycleState?).isAtLeast(CompleteLifecycleState.STOPPED))
    }

    @Test
    fun `GIVEN a null state WHEN checking if it's at least destroyed THEN it isn't`() {
        assertFalse((null as CompleteLifecycleState?).isAtLeast(CompleteLifecycleState.DESTROYED))
    }

    @Test
    fun `GIVEN a created state WHEN checking if it's at least created THEN it is`() {
        assertTrue(CompleteLifecycleState.CREATED.isAtLeast(CompleteLifecycleState.CREATED))
    }

    @Test
    fun `GIVEN a created state WHEN checking if it's at least started THEN it isn't`() {
        assertFalse(CompleteLifecycleState.CREATED.isAtLeast(CompleteLifecycleState.STARTED))
    }

    @Test
    fun `GIVEN a created state WHEN checking if it's at least resumed THEN it isn't`() {
        assertFalse(CompleteLifecycleState.CREATED.isAtLeast(CompleteLifecycleState.RESUMED))
    }

    @Test
    fun `GIVEN a created state WHEN checking if it's at least paused THEN it isn't`() {
        assertFalse(CompleteLifecycleState.CREATED.isAtLeast(CompleteLifecycleState.PAUSED))
    }

    @Test
    fun `GIVEN a created state WHEN checking if it's at least stopped THEN it isn't`() {
        assertFalse(CompleteLifecycleState.CREATED.isAtLeast(CompleteLifecycleState.STOPPED))
    }

    @Test
    fun `GIVEN a created state WHEN checking if it's at least destroyed THEN it isn't`() {
        assertFalse(CompleteLifecycleState.CREATED.isAtLeast(CompleteLifecycleState.DESTROYED))
    }

    @Test
    fun `GIVEN a started state WHEN checking if it's at least created THEN it is`() {
        assertTrue(CompleteLifecycleState.STARTED.isAtLeast(CompleteLifecycleState.CREATED))
    }

    @Test
    fun `GIVEN a started state WHEN checking if it's at least started THEN it is`() {
        assertTrue(CompleteLifecycleState.STARTED.isAtLeast(CompleteLifecycleState.STARTED))
    }

    @Test
    fun `GIVEN a started state WHEN checking if it's at least resumed THEN it isn't`() {
        assertFalse(CompleteLifecycleState.STARTED.isAtLeast(CompleteLifecycleState.RESUMED))
    }

    @Test
    fun `GIVEN a started state WHEN checking if it's at least paused THEN it isn't`() {
        assertFalse(CompleteLifecycleState.STARTED.isAtLeast(CompleteLifecycleState.PAUSED))
    }

    @Test
    fun `GIVEN a started state WHEN checking if it's at least stopped THEN it isn't`() {
        assertFalse(CompleteLifecycleState.STARTED.isAtLeast(CompleteLifecycleState.STOPPED))
    }

    @Test
    fun `GIVEN a started state WHEN checking if it's at least destroyed THEN it isn't`() {
        assertFalse(CompleteLifecycleState.STARTED.isAtLeast(CompleteLifecycleState.DESTROYED))
    }

    @Test
    fun `GIVEN a resumed state WHEN checking if it's at least created THEN it is`() {
        assertTrue(CompleteLifecycleState.RESUMED.isAtLeast(CompleteLifecycleState.CREATED))
    }

    @Test
    fun `GIVEN a resumed state WHEN checking if it's at least started THEN it is`() {
        assertTrue(CompleteLifecycleState.RESUMED.isAtLeast(CompleteLifecycleState.STARTED))
    }

    @Test
    fun `GIVEN a resumed state WHEN checking if it's at least resumed THEN it is`() {
        assertTrue(CompleteLifecycleState.RESUMED.isAtLeast(CompleteLifecycleState.RESUMED))
    }

    @Test
    fun `GIVEN a resumed state WHEN checking if it's at least paused THEN it isn't`() {
        assertFalse(CompleteLifecycleState.RESUMED.isAtLeast(CompleteLifecycleState.PAUSED))
    }

    @Test
    fun `GIVEN a resumed state WHEN checking if it's at least stopped THEN it isn't`() {
        assertFalse(CompleteLifecycleState.RESUMED.isAtLeast(CompleteLifecycleState.STOPPED))
    }

    @Test
    fun `GIVEN a resumed state WHEN checking if it's at least destroyed THEN it isn't`() {
        assertFalse(CompleteLifecycleState.RESUMED.isAtLeast(CompleteLifecycleState.DESTROYED))
    }

    @Test
    fun `GIVEN a paused state WHEN checking if it's at least created THEN it is`() {
        assertTrue(CompleteLifecycleState.PAUSED.isAtLeast(CompleteLifecycleState.CREATED))
    }

    @Test
    fun `GIVEN a paused state WHEN checking if it's at least started THEN it is`() {
        assertTrue(CompleteLifecycleState.PAUSED.isAtLeast(CompleteLifecycleState.STARTED))
    }

    @Test
    fun `GIVEN a paused state WHEN checking if it's at least resumed THEN it is`() {
        assertTrue(CompleteLifecycleState.PAUSED.isAtLeast(CompleteLifecycleState.RESUMED))
    }

    @Test
    fun `GIVEN a paused state WHEN checking if it's at least paused THEN it is`() {
        assertTrue(CompleteLifecycleState.PAUSED.isAtLeast(CompleteLifecycleState.PAUSED))
    }

    @Test
    fun `GIVEN a paused state WHEN checking if it's at least stopped THEN it isn't`() {
        assertFalse(CompleteLifecycleState.PAUSED.isAtLeast(CompleteLifecycleState.STOPPED))
    }

    @Test
    fun `GIVEN a paused state WHEN checking if it's at least destroyed THEN it isn't`() {
        assertFalse(CompleteLifecycleState.PAUSED.isAtLeast(CompleteLifecycleState.DESTROYED))
    }

    @Test
    fun `GIVEN a stopped state WHEN checking if it's at least created THEN it is`() {
        assertTrue(CompleteLifecycleState.STOPPED.isAtLeast(CompleteLifecycleState.CREATED))
    }

    @Test
    fun `GIVEN a stopped state WHEN checking if it's at least started THEN it is`() {
        assertTrue(CompleteLifecycleState.STOPPED.isAtLeast(CompleteLifecycleState.STARTED))
    }

    @Test
    fun `GIVEN a stopped state WHEN checking if it's at least resumed THEN it is`() {
        assertTrue(CompleteLifecycleState.STOPPED.isAtLeast(CompleteLifecycleState.RESUMED))
    }

    @Test
    fun `GIVEN a stopped state WHEN checking if it's at least paused THEN it is`() {
        assertTrue(CompleteLifecycleState.STOPPED.isAtLeast(CompleteLifecycleState.PAUSED))
    }

    @Test
    fun `GIVEN a stopped state WHEN checking if it's at least stopped THEN it is`() {
        assertTrue(CompleteLifecycleState.STOPPED.isAtLeast(CompleteLifecycleState.STOPPED))
    }

    @Test
    fun `GIVEN a stopped state WHEN checking if it's at least destroyed THEN it isn't`() {
        assertFalse(CompleteLifecycleState.STOPPED.isAtLeast(CompleteLifecycleState.DESTROYED))
    }

    @Test
    fun `GIVEN a destroyed state WHEN checking if it's at least created THEN it is`() {
        assertTrue(CompleteLifecycleState.DESTROYED.isAtLeast(CompleteLifecycleState.CREATED))
    }

    @Test
    fun `GIVEN a destroyed state WHEN checking if it's at least started THEN it is`() {
        assertTrue(CompleteLifecycleState.DESTROYED.isAtLeast(CompleteLifecycleState.STARTED))
    }

    @Test
    fun `GIVEN a destroyed state WHEN checking if it's at least resumed THEN it is`() {
        assertTrue(CompleteLifecycleState.DESTROYED.isAtLeast(CompleteLifecycleState.RESUMED))
    }

    @Test
    fun `GIVEN a destroyed state WHEN checking if it's at least paused THEN it is`() {
        assertTrue(CompleteLifecycleState.DESTROYED.isAtLeast(CompleteLifecycleState.PAUSED))
    }

    @Test
    fun `GIVEN a destroyed state WHEN checking if it's at least stopped THEN it is`() {
        assertTrue(CompleteLifecycleState.DESTROYED.isAtLeast(CompleteLifecycleState.STOPPED))
    }

    @Test
    fun `GIVEN a destroyed state WHEN checking if it's at least destroyed THEN it is`() {
        assertTrue(CompleteLifecycleState.DESTROYED.isAtLeast(CompleteLifecycleState.DESTROYED))
    }
}
