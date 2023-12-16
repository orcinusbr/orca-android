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

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.jeanbarrossilva.orca.platform.ui.core.activity.StartableActivity
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state.CompleteLifecycleState
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state.compareTo
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state.next

/**
 * [StartableActivity] that provides a complete version of [Lifecycle.State], containing missing
 * states such as the [paused][CompleteLifecycleState.PAUSED] and
 * [stopped][CompleteLifecycleState.STOPPED] ones.
 *
 * @see CompleteLifecycleState
 */
open class CompleteLifecycleActivity : StartableActivity() {
  /** [CompleteLifecycleState] in which this [CompleteLifecycleActivity] currently is. */
  var completeLifecycleState: CompleteLifecycleState? = null
    private set

  override fun onCreate(savedInstanceState: Bundle?) {
    completeLifecycleState = CompleteLifecycleState.CREATED
    super.onCreate(savedInstanceState)
  }

  override fun onStart() {
    completeLifecycleState = CompleteLifecycleState.STARTED
    super.onStart()
  }

  override fun onResume() {
    completeLifecycleState = CompleteLifecycleState.RESUMED
    super.onResume()
  }

  override fun finish() {
    sideEffectlesslyMoveTo(CompleteLifecycleState.STOPPED)
    super.finish()
    completeLifecycleState = CompleteLifecycleState.DESTROYED
  }

  override fun onPause() {
    completeLifecycleState = CompleteLifecycleState.PAUSED
    super.onPause()
  }

  override fun onStop() {
    completeLifecycleState = CompleteLifecycleState.STOPPED
    super.onStop()
  }

  override fun onDestroy() {
    completeLifecycleState = CompleteLifecycleState.DESTROYED
    super.onDestroy()
  }

  /** Calls [onStart]. */
  internal fun callOnStart() {
    onStart()
  }

  /** Calls [onPause]. */
  internal fun callOnPause() {
    onPause()
  }

  /** Calls [onStop]. */
  internal fun callOnStop() {
    onStop()
  }

  /**
   * Iteratively moves [completeLifecycleState] to the given [state].
   *
   * **NOTE**: Currently only works if we're moving _toward_ the [state]; if [state] is a
   * [CompleteLifecycleState] before [completeLifecycleState], nothing will be done.
   *
   * @param state [CompleteLifecycleState] to which [completeLifecycleState] will be moved.
   */
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
