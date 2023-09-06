package com.jeanbarrossilva.orca.platform.ui.core.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state.CompleteLifecycleState
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.test.doOnDestroy
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.test.onStart
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible
import org.junit.Assert.assertEquals
import org.junit.Test

internal class LifecycleActivityTests {
    @Test
    fun completeLifecycleStateIsCreatedWhenActivityIsCreated() {
        launchActivity<CompleteLifecycleActivity>().use { scenario ->
            scenario.onActivity { activity ->
                try { activity.onCreate(null, null) } catch (_: IllegalStateException) { }
                assertEquals(CompleteLifecycleState.CREATED, activity.completeLifecycleState)
            }
        }
    }

    @Test
    fun completeLifecycleStateIsStartedWhenActivityIsStarted() {
        launchActivity<CompleteLifecycleActivity>().use { scenario ->
            scenario.onActivity { activity ->
                activity.onStart()
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
            scenario.moveToState(Lifecycle.State.STARTED)
            scenario.onActivity { activity ->
                CompleteLifecycleActivity::class
                    .declaredMemberFunctions
                    .first { it.name == "onPause" }
                    .apply { isAccessible = true }
                    .call(activity)
                assertEquals(CompleteLifecycleState.PAUSED, activity.completeLifecycleState)
            }
        }
    }

    @Test
    fun completeLifecycleStateIsStoppedWhenActivityIsStopped() {
        launchActivity<CompleteLifecycleActivity>().use { scenario ->
            scenario.moveToState(Lifecycle.State.CREATED)
            scenario.onActivity { activity ->
                CompleteLifecycleActivity::class
                    .declaredMemberFunctions
                    .first { it.name == "onStop" }
                    .apply { isAccessible = true }
                    .call(activity)
                assertEquals(CompleteLifecycleState.STOPPED, activity.completeLifecycleState)
            }
        }
    }

    @Test
    fun completeLifecycleStateIsDestroyedWhenActivityIsDestroyed() {
        var state: CompleteLifecycleState? = null
        launchActivity<CompleteLifecycleActivity>().use { scenario ->
            scenario.onActivity { activity ->
                activity.doOnDestroy {
                    state = completeLifecycleState
                }
            }
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
