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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state.CompleteLifecycleState
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state.compareTo
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state.next

/**
 * [ComponentActivity] that provides a complete version of [Lifecycle.State], containing missing
 * states such as the [paused][CompleteLifecycleState.PAUSED] and
 * [stopped][CompleteLifecycleState.STOPPED] ones.
 *
 * @see CompleteLifecycleState
 */
open class CompleteLifecycleActivity : ComponentActivity() {
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
