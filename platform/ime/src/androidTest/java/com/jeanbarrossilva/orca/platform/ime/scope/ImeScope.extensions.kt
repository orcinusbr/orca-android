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

@file:JvmName("ImeScopeExtensions")

package com.jeanbarrossilva.orca.platform.ime.scope

import android.view.WindowInsetsAnimation
import androidx.activity.ComponentActivity
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.platform.ime.Ime
import com.jeanbarrossilva.orca.platform.ime.scope.animation.ImeAnimationCallback
import com.jeanbarrossilva.orca.platform.testing.activity.scenario.activity
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.test.runTest

/**
 * Latest visibility to which the IME has changed.
 *
 * @throws IllegalStateException If this [ImeScope]'s current IME visibility is invalid.
 * @see Ime
 */
internal inline val ImeScope.ime
  @Throws(IllegalStateException::class)
  get() =
    when (visibility) {
      Ime.Visibility.UNKNOWN -> Ime.Unknown
      Ime.Visibility.CLOSED -> Ime.Closed
      Ime.Visibility.OPEN -> Ime.Open
      else -> throw IllegalStateException("\"$visibility\" isn't a valid IME visibility.")
    }

/**
 * Opens the IME and suspends the execution flow until the [WindowInsetsAnimation] ends.
 *
 * @see ImeScope.open
 */
internal suspend fun ImeScope.open() {
  suspendCoroutine(::open)
}

/**
 * Closes the IME and suspends the execution flow until the [WindowInsetsAnimation] ends.
 *
 * @see ImeScope.close
 */
internal suspend fun ImeScope.close() {
  suspendCoroutine(::close)
}

/**
 * Creates an environment for [Ime]-specific tests.
 *
 * @param body Testing to be performed.
 */
@OptIn(ExperimentalContracts::class)
@Suppress("NOTHING_TO_INLINE")
internal inline fun runImeTest(noinline body: suspend ImeScope.() -> Unit) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  val scenario = launchActivity<ComponentActivity>()
  val activity = checkNotNull(scenario.activity)
  val view = checkNotNull(activity.window?.decorView)
  val onVisibilityChangeListener = CapturingOnImeVisibilityChangeListener(activity, view)
  val animationCallback = ImeAnimationCallback(view)
  val windowInsetsController = checkNotNull(activity.window?.insetsController)
  runTest {
    val scope = ImeScope(activity, view, onVisibilityChangeListener, animationCallback, this)
    windowInsetsController.addOnControllableInsetsChangedListener(onVisibilityChangeListener)
    view.setWindowInsetsAnimationCallback(animationCallback)
    try {
      scope.body()
    } finally {
      view.setWindowInsetsAnimationCallback(null)
      windowInsetsController.removeOnControllableInsetsChangedListener(onVisibilityChangeListener)
      scenario.close()
    }
  }
}
