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
