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
