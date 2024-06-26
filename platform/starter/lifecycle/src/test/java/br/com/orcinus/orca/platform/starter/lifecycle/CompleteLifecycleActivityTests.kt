/*
 * Copyright © 2023-2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.platform.starter.lifecycle

import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.platform.starter.lifecycle.state.CompleteLifecycleState
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
internal class CompleteLifecycleActivityTests {
  @Test
  fun completeLifecycleStateIsCreatedWhenActivityIsCreated() {
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.onActivity { activity ->
        try {
          activity.onCreate(null, null)
        } catch (_: IllegalStateException) {}
        assertThat(activity.completeLifecycleState).isEqualTo(CompleteLifecycleState.CREATED)
      }
    }
  }

  @Test
  fun completeLifecycleStateIsStartedWhenActivityIsStarted() {
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.callOnStart()
        assertThat(activity.completeLifecycleState).isEqualTo(CompleteLifecycleState.STARTED)
      }
    }
  }

  @Test
  fun completeLifecycleStateIsResumedWhenActivityIsResumed() {
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.moveToState(Lifecycle.State.RESUMED)
      scenario.onActivity { activity ->
        assertThat(activity.completeLifecycleState).isEqualTo(CompleteLifecycleState.RESUMED)
      }
    }
  }

  @Test
  fun completeLifecycleStateIsPausedWhenActivityIsPaused() {
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.callOnPause()
        assertThat(activity.completeLifecycleState).isEqualTo(CompleteLifecycleState.PAUSED)
      }
    }
  }

  @Test
  fun completeLifecycleStateIsStoppedWhenActivityIsStopped() {
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.callOnStop()
        assertThat(activity.completeLifecycleState).isEqualTo(CompleteLifecycleState.STOPPED)
      }
    }
  }

  @Config(sdk = [Build.VERSION_CODES.Q])
  @Test
  fun completeLifecycleStateIsDestroyedWhenActivityIsDestroyed() {
    var state: CompleteLifecycleState? = null
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.onActivity { activity -> activity.doOnDestroy { state = completeLifecycleState } }
      scenario.moveToState(Lifecycle.State.DESTROYED)
    }
    assertThat(state).isEqualTo(CompleteLifecycleState.DESTROYED)
  }

  @Test
  fun destroysActivityWhenItIsFinished() {
    launchActivity<CompleteLifecycleActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.finish()
        assertThat(activity.completeLifecycleState).isEqualTo(CompleteLifecycleState.DESTROYED)
      }
    }
  }
}
