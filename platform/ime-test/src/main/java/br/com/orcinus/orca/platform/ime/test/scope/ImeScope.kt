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

package br.com.orcinus.orca.platform.ime.test.scope

import android.annotation.SuppressLint
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.test.core.app.launchActivity
import br.com.orcinus.orca.platform.ime.Ime
import br.com.orcinus.orca.platform.ime.test.scope.animation.ImeAnimationCallback
import br.com.orcinus.orca.platform.ime.test.scope.animation.stage.Stage
import br.com.orcinus.orca.platform.testing.activity.scenario.activity
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest

/**
 * Scope in which [Ime] tests can be run.
 *
 * @param delegate [CoroutineScope] to which [CoroutineScope]-like behavior is delegated.
 * @property activity [ImeScopeActivity] from which the IME will be toggled.
 * @property onVisibilityChangeListener [CapturingOnImeVisibilityChangeListener] by which the
 *   current visibility of the IME is captured and provided.
 * @property animationCallback [ImeAnimationCallback] that suspends this [ImeScope] while animations
 *   are ongoing.
 * @property view [View] to which the [ImeAnimationCallback] will be added.
 * @see Ime.Visibility
 * @see ImeScope.visibility
 * @see Stage.ongoing
 */
class ImeScope
@InternalScopeApi
@PublishedApi
internal constructor(
  private val activity: ImeScopeActivity,
  private val onVisibilityChangeListener: CapturingOnImeVisibilityChangeListener,
  internal val animationCallback: ImeAnimationCallback,
  val view: View,
  delegate: CoroutineScope,
) : CoroutineScope by delegate {
  /**
   * Gets the latest visibility to which the IME has changed.
   *
   * @see Ime.Visibility
   */
  @PublishedApi
  internal val visibility: Int
    get() = onVisibilityChangeListener.visibility

  /**
   * Latest visibility to which the IME has changed.
   *
   * @throws IllegalStateException If the current IME visibility is invalid.
   * @see Ime
   */
  inline val ime
    @Throws(IllegalStateException::class)
    get() =
      when (visibility) {
        Ime.Visibility.UNKNOWN -> Ime.Unknown
        Ime.Visibility.CLOSED -> Ime.Closed
        Ime.Visibility.OPEN -> Ime.Open
        else -> throw IllegalStateException("\"$visibility\" isn't a valid IME visibility.")
      }

  /** Opens the IME. */
  @SuppressLint("WrongConstant")
  suspend fun open() {
    runAndAwaitAnimation { it.show(Ime.type) }
  }

  /** Closes the IME. */
  @SuppressLint("WrongConstant")
  suspend fun close() {
    runAndAwaitAnimation { it.hide(Ime.type) }
  }

  /**
   * Performs the given action on the [View]'s [WindowInsetsControllerCompat] and awaits the end of
   * the IME animation.
   *
   * @param action Operation to be executed.
   * @see ImeScope.view
   * @see WindowCompat.getInsetsController
   */
  private suspend fun runAndAwaitAnimation(action: (WindowInsetsControllerCompat) -> Unit) {
    val window = activity.window
    val view = view
    val windowInsetsControllerCompat = WindowCompat.getInsetsController(window, view)
    activity.runOnUiThread { action(windowInsetsControllerCompat) }
    animationCallback.awaitAnimation()
  }
}

/**
 * Creates an environment for [Ime]-specific tests.
 *
 * Ultimately, facilitates the process of controlling the visibility of the IME and, most
 * importantly, suspends the execution flow while doing so (that is, until the animation ends). This
 * specific behavior prevents the outdated state in which the IME previously *was* to be obtained as
 * being the current one, which is what would most likely occur if, for example, the following case
 * was to be tested:
 * ```kotlin
 * @Test
 * fun opensIme(windowInsets: WindowInsets, windowInsetsController: WindowInsetsController) {
 *   windowInsetsController.show(Ime.type)
 *   assertThat(windowInsets.isVisible(Ime.type)).isTrue()
 * }
 * ```
 *
 * With the [ImeScope] that is provided to the [body] of this method, calls to both [ImeScope.open]
 * and [ImeScope.close] gracefully wait for the animations to end and provide the most up-to-date
 * visibility when it is later retrieved. What was done in the previous example could successfully
 * be achieved as:
 * ```kotlin
 * @Test
 * fun opensIme() {
 *   runImeTest {
 *     open()
 *     assertThat(ime.isOpen).isTrue()
 *   }
 * }
 * ```
 *
 * @param body Testing to be performed.
 * @see WindowInsets
 * @see WindowInsetsController
 * @see Ime.type
 * @see WindowInsetsController.show
 * @see WindowInsets.isVisible
 * @see ImeScope.ime
 * @see Ime.isOpen
 */
@OptIn(ExperimentalContracts::class)
@Suppress("NOTHING_TO_INLINE")
inline fun runImeTest(noinline body: suspend ImeScope.() -> Unit) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  val scenario = launchActivity<ImeScopeActivity>()
  val activity = checkNotNull(scenario.activity)
  val view = checkNotNull(activity.window?.decorView)
  val windowInsetsControllerCompat = WindowCompat.getInsetsController(activity.window, view)
  val onVisibilityChangeListener = CapturingOnImeVisibilityChangeListener(view)
  val animationCallback = ImeAnimationCallback(view)
  runTest {
    windowInsetsControllerCompat.addOnControllableInsetsChangedListener(onVisibilityChangeListener)
    ViewCompat.setWindowInsetsAnimationCallback(view, animationCallback)
    try {
      ImeScope(activity, onVisibilityChangeListener, animationCallback, view, this).body()
    } finally {
      ViewCompat.setWindowInsetsAnimationCallback(view, null)
      windowInsetsControllerCompat.removeOnControllableInsetsChangedListener(
        onVisibilityChangeListener
      )
      scenario.close()
    }
  }
}
