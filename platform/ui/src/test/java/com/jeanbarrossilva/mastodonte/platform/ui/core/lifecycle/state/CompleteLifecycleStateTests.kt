package com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle.state

import org.junit.Assert.assertEquals
import org.junit.Test

internal class CompleteLifecycleStateTests {
    @Test
    fun `GIVEN a created state WHEN getting the next one THEN it's started`() {
        assertEquals(CompleteLifecycleState.STARTED, CompleteLifecycleState.CREATED.next())
    }

    @Test
    fun `GIVEN a started state WHEN getting the next one THEN it's resumed`() {
        assertEquals(CompleteLifecycleState.RESUMED, CompleteLifecycleState.STARTED.next())
    }

    @Test
    fun `GIVEN a resumed state WHEN getting the next one THEN it's paused`() {
        assertEquals(CompleteLifecycleState.PAUSED, CompleteLifecycleState.RESUMED.next())
    }

    @Test
    fun `GIVEN a paused state WHEN getting the next one THEN it's stopped`() {
        assertEquals(CompleteLifecycleState.STOPPED, CompleteLifecycleState.PAUSED.next())
    }

    @Test
    fun `GIVEN a stopped state WHEN getting the next one THEN it's destroyed`() {
        assertEquals(CompleteLifecycleState.DESTROYED, CompleteLifecycleState.STOPPED.next())
    }
}
