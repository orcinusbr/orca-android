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

package br.com.orcinus.orca.platform.autos.test.kit.input.text.composition.interop.scope

import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.runAndroidComposeUiTest
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import br.com.orcinus.orca.platform.autos.InternalPlatformAutosApi
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.CompositionTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.proxy
import br.com.orcinus.orca.platform.autos.theme.AutosContextThemeWrapper
import br.com.orcinus.orca.platform.testing.context
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

/** Tag that identifies a [CompositionTextField] in a test run by [runCompositionTextFieldTest]. */
internal const val CompositionTextFieldTag = "composition-text-field"

/**
 * [InteropEditTextScope] implementation provided to the body of a [CompositionTextField] test.
 *
 * @param testScope [TestScope] in which operations performed by the test can be suspended and that
 *   launches the unconfined [Job] responsible for blocking the execution flow until an IME
 *   animation reaches its end when [awaitImeAnimation] is called.
 * @property composeUiTest [AndroidComposeUiTest] that provides both
 *   [SemanticsNodeInteractionsProvider]-like behavior to this class and the [WindowInsets]-related
 *   APIs for blocking the execution flow until an IME animation finishes running.
 */
@OptIn(ExperimentalTestApi::class)
private class CompositionTextFieldEnvironment(
  testScope: TestScope,
  private val composeUiTest: AndroidComposeUiTest<ComponentActivity>,
  override val textField: CompositionTextField
) :
  InteropEditTextScope(),
  CoroutineScope by testScope,
  SemanticsNodeInteractionsProvider by composeUiTest {
  override suspend fun awaitImeAnimation(trigger: () -> Unit) {
    val window = composeUiTest.activity?.window ?: return
    val getWindowInsets = { ViewCompat.getRootWindowInsets(textField) }
    val windowInsetsController = WindowCompat.getInsetsController(window, textField)
    val wasImeVisible = getWindowInsets()?.isVisible(imeType)
    suspendCancellableCoroutine {
      val listener =
        object : WindowInsetsControllerCompat.OnControllableInsetsChangedListener {
          override fun onControllableInsetsChanged(
            controller: WindowInsetsControllerCompat,
            typeMask: Int
          ) {
            val haveImeInsetsChanged = typeMask and imeType == imeType
            if (haveImeInsetsChanged) {
              controller.removeOnControllableInsetsChangedListener(this)
              it.resume(Unit)
            }
          }
        }
      windowInsetsController.addOnControllableInsetsChangedListener(listener)
      it.invokeOnCancellation {
        windowInsetsController.removeOnControllableInsetsChangedListener(listener)
      }
      trigger()
    }
    var isImeVisible: Boolean?
    do {
      isImeVisible = getWindowInsets()?.isVisible(imeType) ?: break
    } while (isImeVisible == wasImeVisible)
  }

  companion object {
    /** Constant from [WindowInsetsCompat.Type] for the IME. */
    internal val imeType = WindowInsetsCompat.Type.ime()
  }
}

/** [CoroutineScope] suited for [CompositionTextField]-focused tests. */
@InternalPlatformAutosApi
abstract class InteropEditTextScope internal constructor() :
  CoroutineScope, SemanticsNodeInteractionsProvider {
  /** [CompositionTextField] under test. */
  abstract val textField: CompositionTextField

  /**
   * Suspends after the [trigger] is invoked until the animation of the IME finishes running.
   *
   * @param trigger Operation that opens or closes the IME.
   */
  abstract suspend fun awaitImeAnimation(trigger: () -> Unit)
}

/**
 * Runs a [CompositionTextField]-focused test.
 *
 * @param coroutineContext [CoroutineContext] that provides the [CoroutineDispatcher] for
 *   determining the threads to be used for execution.
 * @param body Lambda in which the testing is performed.
 */
@InternalPlatformAutosApi
@OptIn(ExperimentalContracts::class)
fun runCompositionTextFieldTest(
  coroutineContext: CoroutineContext = EmptyCoroutineContext,
  body: suspend InteropEditTextScope.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }

  @OptIn(ExperimentalTestApi::class)
  runAndroidComposeUiTest<ComponentActivity> {
    runTest(coroutineContext) {
      val context = AutosContextThemeWrapper(context)
      val textField = CompositionTextField(context)
      setContent {
        AndroidView({ textField }, Modifier.proxy(textField).testTag(CompositionTextFieldTag))
      }
      CompositionTextFieldEnvironment(this, this@runAndroidComposeUiTest, textField).body()
    }
  }
}
