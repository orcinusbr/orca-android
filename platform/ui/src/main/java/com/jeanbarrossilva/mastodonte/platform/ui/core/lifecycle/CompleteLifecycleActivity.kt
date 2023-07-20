package com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import com.jeanbarrossilva.mastodonte.platform.ui.core.composable.ComposableActivity
import com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle.state.CompleteLifecycleState
import com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle.state.compareTo
import com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle.state.next

/**
 * [ComposableActivity] that provides a complete version of [Lifecycle.State], containing missing
 * states such as the [paused][CompleteLifecycleState.PAUSED] and
 * [stopped][CompleteLifecycleState.STOPPED] ones.
 *
 * @see CompleteLifecycleState
 **/
abstract class CompleteLifecycleActivity : ComponentActivity() {
    /** [CompleteLifecycleState] in which this [CompleteLifecycleActivity] currently is. **/
    var completeLifecycleState: CompleteLifecycleState? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        completeLifecycleState = CompleteLifecycleState.CREATED
    }

    override fun onStart() {
        super.onStart()
        completeLifecycleState = CompleteLifecycleState.STARTED
    }

    override fun onResume() {
        super.onResume()
        completeLifecycleState = CompleteLifecycleState.RESUMED
    }

    override fun finish() {
        sideEffectlesslyMoveTo(CompleteLifecycleState.STOPPED)
        super.finish()
        completeLifecycleState = CompleteLifecycleState.DESTROYED
    }

    override fun onPause() {
        super.onPause()
        completeLifecycleState = CompleteLifecycleState.PAUSED
    }

    override fun onStop() {
        super.onStop()
        completeLifecycleState = CompleteLifecycleState.STOPPED
    }

    override fun onDestroy() {
        super.onDestroy()
        completeLifecycleState = CompleteLifecycleState.DESTROYED
    }

    /**
     * Iteratively moves [completeLifecycleState] to the given [state].
     *
     * @param state [CompleteLifecycleState] to which [completeLifecycleState] will be moved.
     **/
    private fun sideEffectlesslyMoveTo(
        @Suppress("SameParameterValue") state: CompleteLifecycleState
    ) {
        var current = completeLifecycleState
        if (current < state) {
            while (current != state) {
                /*
                 * The only scenario in which this call would return null is when both the
                 * completeLifecycleState and the state we are moving to are
                 * CompleteLifecycleState.DESTROYED.
                 *
                 * Since we've already ensured that the current one and the one we want to move to
                 * are not the same, it is safe to null-assert.
                 */
                current = current.next()!!
            }
        }
    }
}
