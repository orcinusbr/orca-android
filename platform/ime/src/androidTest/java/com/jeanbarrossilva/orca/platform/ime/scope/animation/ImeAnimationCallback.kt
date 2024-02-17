/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.platform.ime.scope.animation

import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import com.jeanbarrossilva.orca.ext.coroutines.await
import com.jeanbarrossilva.orca.platform.ime.scope.animation.stage.Stage
import java.time.Duration
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * [WindowInsetsAnimation.Callback] that responds only to IME-specific [WindowInsetsAnimation]s.
 *
 * @param view [View] to which this [ImeAnimationCallback] will be set.
 * @see View.setWindowInsetsAnimationCallback
 */
internal class ImeAnimationCallback(private val view: View) :
  WindowInsetsAnimation.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE) {
  /**
   * [MutableStateFlow] to which the current [Stage] of the ongoing [WindowInsetsAnimation] is
   * emitted.
   *
   * @see stage
   */
  private val stageFlow = MutableStateFlow(Stage.idle())

  /** Whether this [WindowInsetsAnimation] is the IME's. */
  private val WindowInsetsAnimation.isOfIme
    get() = typeMask and type == type

  /** Current [Stage] of the ongoing [WindowInsetsAnimation]. */
  var stage
    get() = stageFlow.value
    private set(stage) {
      stageFlow.value = stage
    }

  init {
    stage = Stage.idle()
  }

  override fun onPrepare(animation: WindowInsetsAnimation) {
    if (animation.isOfIme) {
      val duration = Duration.ofMillis(animation.durationMillis)
      stage += Stage.prepared(duration)
    }
  }

  override fun onStart(
    animation: WindowInsetsAnimation,
    bounds: WindowInsetsAnimation.Bounds
  ): WindowInsetsAnimation.Bounds {
    if (animation.isOfIme) {
      val duration = Duration.ofMillis(animation.durationMillis)
      stage += Stage.started(duration)
    }
    return super.onStart(animation, bounds)
  }

  override fun onProgress(
    insets: WindowInsets,
    runningAnimations: MutableList<WindowInsetsAnimation>
  ): WindowInsets {
    val ongoing = runningAnimations.filter { it.isOfIme }
    val hasOngoing = ongoing.isNotEmpty()
    if (hasOngoing) {
      val durationInMilliseconds = ongoing.sumOf(WindowInsetsAnimation::getDurationMillis)
      val duration = Duration.ofMillis(durationInMilliseconds)
      stage += Stage.ongoing(duration)
    }
    return insets
  }

  override fun onEnd(animation: WindowInsetsAnimation) {
    if (animation.isOfIme) {
      stage += Stage.ended()
    }
  }

  /**
   * Suspends until the IME [WindowInsetsAnimation] has ended.
   *
   * @see Stage.ended
   */
  suspend fun awaitAnimation() {
    stageFlow.await()
    while (!stage.isEnded) {
      awaitAnimation()
    }
  }

  companion object {
    /**
     * Constant from [WindowInsets.Type] that is used for determining whether a
     * [WindowInsetsAnimation] belongs to the IME.
     */
    @JvmField val type = WindowInsets.Type.ime()
  }
}
