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

package com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state

import android.app.Activity
import androidx.lifecycle.Lifecycle

/** Complete analogue of [Lifecycle.State]. */
enum class CompleteLifecycleState {
  /** Equivalent to [Lifecycle.State.CREATED]. */
  CREATED,

  /** Equivalent to [Lifecycle.State.STARTED]. */
  STARTED,

  /** Equivalent to [Lifecycle.State.RESUMED]. */
  RESUMED,

  /**
   * State in which an [Activity] is put whenever it's moved to the background while a new one is
   * started.
   */
  PAUSED,

  /** State in which an [Activity] is not visible to the user. */
  STOPPED,

  /** Equivalent to [Lifecycle.State.DESTROYED]. */
  DESTROYED;

  /** Provides the [CompleteLifecycleState] that succeeds this one. */
  internal fun next(): CompleteLifecycleState? {
    val index = values().indexOf(this) + 1
    return values().getOrNull(index)
  }
}
