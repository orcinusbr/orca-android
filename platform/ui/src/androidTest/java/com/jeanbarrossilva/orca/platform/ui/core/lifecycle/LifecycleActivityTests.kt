/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.ui.core.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state.CompleteLifecycleState
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.test.doOnDestroy
import org.junit.Assert.assertEquals
import org.junit.Test

internal class LifecycleActivityTests {
  @Test
  fun completeLifecycleStateIsCreatedWhenActivityIsCreated() {
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.onActivity { activity ->
        try {
          activity.onCreate(null, null)
        } catch (_: IllegalStateException) {}
        assertEquals(CompleteLifecycleState.CREATED, activity.completeLifecycleState)
      }
    }
  }

  @Test
  fun completeLifecycleStateIsStartedWhenActivityIsStarted() {
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.callOnStart()
        assertEquals(CompleteLifecycleState.STARTED, activity.completeLifecycleState)
      }
    }
  }

  @Test
  fun completeLifecycleStateIsResumedWhenActivityIsResumed() {
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.moveToState(Lifecycle.State.RESUMED)
      scenario.onActivity { activity ->
        assertEquals(CompleteLifecycleState.RESUMED, activity.completeLifecycleState)
      }
    }
  }

  @Test
  fun completeLifecycleStateIsPausedWhenActivityIsPaused() {
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.callOnPause()
        assertEquals(CompleteLifecycleState.PAUSED, activity.completeLifecycleState)
      }
    }
  }

  @Test
  fun completeLifecycleStateIsStoppedWhenActivityIsStopped() {
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.callOnStop()
        assertEquals(CompleteLifecycleState.STOPPED, activity.completeLifecycleState)
      }
    }
  }

  @Test
  fun completeLifecycleStateIsDestroyedWhenActivityIsDestroyed() {
    var state: CompleteLifecycleState? = null
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.onActivity { activity -> activity.doOnDestroy { state = completeLifecycleState } }
      scenario.moveToState(Lifecycle.State.DESTROYED)
    }
    assertEquals(CompleteLifecycleState.DESTROYED, state)
  }

  @Test
  fun destroysActivityWhenItIsFinished() {
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.finish()
        assertEquals(CompleteLifecycleState.DESTROYED, activity.completeLifecycleState)
      }
    }
  }
}
