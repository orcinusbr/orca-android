/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.platform.starter.lifecycle.state

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
