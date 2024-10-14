/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.background

import android.app.Service
import android.content.Intent
import androidx.annotation.CallSuper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

/** [Lifecycle] of an [ObservableService]. */
private class ObservableLifecycle(private val owner: LifecycleOwner) : Lifecycle() {
  /**
   * [LifecycleObserver]s that have been added.
   *
   * @see addObserver
   */
  private val observers = mutableSetOf<LifecycleObserver>()

  override lateinit var currentState: State
    private set

  init {
    setCurrentState(State.INITIALIZED)
  }

  override fun addObserver(observer: LifecycleObserver) {
    observers += observer
    notify(observer)
  }

  override fun removeObserver(observer: LifecycleObserver) {
    observers -= observer
  }

  /**
   * Changes the current state and notifies each added [LifecycleObserver].
   *
   * @param currentState State to which the current one will be changed.
   */
  fun setCurrentState(currentState: State) {
    this.currentState = currentState
    observers.forEach(::notify)
  }

  /**
   * Notifies the given [observer] of the current state in case it is either a default or an
   * event-based one; otherwise, this method is a no-op.
   *
   * @param observer [LifecycleObserver] to be notified.
   * @see currentState
   * @see DefaultLifecycleObserver
   * @see LifecycleEventObserver
   */
  private fun notify(observer: LifecycleObserver) {
    when (observer) {
      is DefaultLifecycleObserver -> notify(observer)
      is LifecycleEventObserver -> notify(observer)
    }
  }

  /**
   * Notifies the [observer] by invoking its method that is related to the current state.
   *
   * @param observer [DefaultLifecycleObserver] to be notified.
   * @see currentState
   */
  private fun notify(observer: DefaultLifecycleObserver) {
    when (currentState) {
      State.INITIALIZED -> Unit
      State.CREATED -> observer.onCreate(owner)
      State.STARTED -> observer.onStart(owner)
      State.RESUMED -> observer.onResume(owner)
      State.DESTROYED -> observer.onDestroy(owner)
    }
  }

  /**
   * Notifies the [observer] by invoking its [onStateChanged][LifecycleEventObserver.onStateChanged]
   * method with the event that matches the current state; if it is the initialized one, then _on
   * any_ is passed to it as the `event` parameter.
   *
   * @param observer [LifecycleEventObserver] to be notified.
   * @see currentState
   * @see Lifecycle.Event.ON_ANY
   */
  private fun notify(observer: LifecycleEventObserver) {
    observer.onStateChanged(
      owner,
      when (currentState) {
        State.INITIALIZED -> Event.ON_ANY
        State.CREATED -> Event.ON_CREATE
        State.STARTED -> Event.ON_START
        State.RESUMED -> Event.ON_RESUME
        State.DESTROYED -> Event.ON_DESTROY
      }
    )
  }
}

/** [Service] which is also the owner of a [Lifecycle]. */
internal abstract class ObservableService : Service(), LifecycleOwner {
  /** Backing field of [lifecycle]. */
  private val observableLifecycle by lazy { ObservableLifecycle(this) }

  override val lifecycle by lazy<Lifecycle> { observableLifecycle }

  @CallSuper
  override fun onCreate() {
    super.onCreate()
    observableLifecycle.setCurrentState(Lifecycle.State.CREATED)
  }

  @CallSuper
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    val continuation = super.onStartCommand(intent, flags, startId)
    observableLifecycle.setCurrentState(Lifecycle.State.STARTED)
    return continuation
  }

  @CallSuper
  override fun onDestroy() {
    super.onDestroy()
    observableLifecycle.setCurrentState(Lifecycle.State.DESTROYED)
  }
}
