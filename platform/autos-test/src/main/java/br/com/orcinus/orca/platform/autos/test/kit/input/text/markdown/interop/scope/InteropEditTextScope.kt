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

package br.com.orcinus.orca.platform.autos.test.kit.input.text.markdown.interop.scope

import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.annotation.ColorInt
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.runAndroidComposeUiTest
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import br.com.orcinus.orca.platform.autos.InternalPlatformAutosApi
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop.InteropEditText
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop.proxy
import br.com.orcinus.orca.platform.autos.theme.AutosContextThemeWrapper
import br.com.orcinus.orca.platform.testing.context
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.resume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

/** Tag that identifies an [InteropEditText] in a test run by [runInteropEditTextTest]. */
@PublishedApi internal const val InteropEditTextTag = "interop-edit-text"

/**
 * [InteropEditTextScope] implementation provided to the body of an [InteropEditText] test.
 *
 * @param testScope [TestScope] in which operations performed by the test can be suspended and that
 *   launches the unconfined [Job] responsible for blocking the execution flow until an IME
 *   animation reaches its end when [awaitImeAnimation] is called.
 * @property composeUiTest [AndroidComposeUiTest] that provides both
 *   [SemanticsNodeInteractionsProvider]-like behavior to this class and the [WindowInsets]-related
 *   APIs for blocking the execution flow until an IME animation finishes running.
 * @property contentState [MutableState] to which a [Composable] requested to be added will be set.
 *   Its value is intended be observed in the content of the [ComposeUiTest] in order for it to be
 *   shown and perform recomposition according to the test's setting.
 */
@OptIn(ExperimentalTestApi::class)
@PublishedApi
internal class InteropEditTestEnvironment(
  testScope: TestScope,
  private val composeUiTest: AndroidComposeUiTest<ComponentActivity>,
  private val contentState: MutableState<(@Composable () -> Unit)?>,
  override val view: InteropEditText,
  @ColorInt override val color: Int
) :
  InteropEditTextScope(),
  CoroutineScope by testScope,
  SemanticsNodeInteractionsProvider by composeUiTest {
  override fun addContent(content: @Composable () -> Unit) {
    contentState.value = content
  }

  override suspend fun awaitImeAnimation(trigger: () -> Unit) {
    val window = composeUiTest.activity?.window ?: return
    val getWindowInsets = { ViewCompat.getRootWindowInsets(view) }
    val windowInsetsController = WindowCompat.getInsetsController(window, view)
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

/** [CoroutineScope] suited for [InteropEditText]-focused tests. */
@InternalPlatformAutosApi
abstract class InteropEditTextScope @PublishedApi internal constructor() :
  CoroutineScope, SemanticsNodeInteractionsProvider {
  /** [InteropEditText] under test. */
  abstract val view: InteropEditText

  /** Default ARGB color by which the area targeted by the test can be colored. */
  @get:ColorInt abstract val color: Int

  /**
   * Adds the content to be displayed.
   *
   * @param content [Composable] that will be added and displayed.
   */
  abstract fun addContent(content: @Composable () -> Unit)

  /**
   * Suspends after the [trigger] is invoked until the animation of the IME finishes running.
   *
   * @param trigger Operation that opens or closes the IME.
   */
  abstract suspend fun awaitImeAnimation(trigger: () -> Unit)
}

/**
 * Runs an [InteropEditText]-focused test.
 *
 * @param coloring Provides the [TextFieldColors] to be set to the [InteropEditText].
 * @param body Lambda in which the testing is performed.
 */
@InternalPlatformAutosApi
@OptIn(ExperimentalContracts::class)
inline fun runInteropEditTextTest(
  crossinline coloring: @Composable TextFieldDefaults.(Color) -> TextFieldColors = { colors() },
  crossinline body: suspend InteropEditTextScope.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }

  @OptIn(ExperimentalTestApi::class)
  runAndroidComposeUiTest<ComponentActivity> {
    val context = AutosContextThemeWrapper(context)
    val view = InteropEditText(context)
    val color = Color.Transparent
    val colorInArgb = color.toArgb()
    val contentState = mutableStateOf<(@Composable () -> Unit)?>(null)
    setContent {
      TextFieldDefaults.coloring(color).let {
        DisposableEffect(view, it) {
          view.setColors(it)
          onDispose {}
        }

        contentState.value?.invoke()
      }

      AndroidView(
        { view },
        Modifier.proxy(view).testTag(InteropEditTextTag),
        onRelease = {
          it.setCompoundDrawables(null, null, null, null)
          it.setColors(null)
        }
      ) {
        it.setCompoundDrawablesRelativeWithIntrinsicBounds(
          android.R.drawable.ic_dialog_alert,
          0,
          android.R.drawable.ic_menu_delete,
          0
        )
      }
    }
    runTest {
      InteropEditTestEnvironment(
          this,
          this@runAndroidComposeUiTest,
          contentState,
          view,
          colorInArgb
        )
        .body()
    }
  }
}
