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

package br.com.orcinus.orca.platform.markdown.interop.scope

import android.content.res.Resources
import androidx.annotation.ColorInt
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.viewinterop.AndroidView
import br.com.orcinus.orca.platform.markdown.interop.InteropEditText
import br.com.orcinus.orca.platform.markdown.interop.proxy
import br.com.orcinus.orca.platform.testing.context
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest

/** Tag that identifies an [InteropEditText] in a test run by [runInteropEditTextTest]. */
@InternalInteropEditTextScopeApi internal const val InteropEditTextTag = "interop-edit-text"

/** [CoroutineScope] suited for [InteropEditText]-focused tests. */
internal interface InteropEditTextScope : CoroutineScope, SemanticsNodeInteractionsProvider {
  /** [InteropEditText] under test. */
  val view: InteropEditText

  /** Default ARGB color by which the area targeted by the test can be colored. */
  @get:ColorInt val color: Int

  /**
   * Adds the content to be displayed.
   *
   * @param content [Composable] that will be added and displayed.
   */
  fun addContent(content: @Composable () -> Unit)
}

/**
 * Runs a [InteropEditText]-focused test.
 *
 * @param coloring Provides the [TextFieldColors] to be set to the [InteropEditText].
 * @param body Lambda in which the testing is performed.
 */
@OptIn(ExperimentalContracts::class)
internal inline fun runInteropEditTextTest(
  crossinline coloring: @Composable TextFieldDefaults.(Color) -> TextFieldColors = { colors() },
  crossinline body: suspend InteropEditTextScope.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  val view = InteropEditText(context)
  val color = Color.Transparent
  val colorInArgb = color.toArgb()
  var content by mutableStateOf<(@Composable () -> Unit)?>(null)

  @OptIn(ExperimentalTestApi::class)
  runComposeUiTest {
    setContent {
      TextFieldDefaults.coloring(color).let {
        DisposableEffect(view, it) {
          view.setColors(it)
          onDispose {}
        }

        content?.invoke()
      }

      AndroidView(
        { view },
        Modifier.proxy(view).testTag(InteropEditTextTag),
        onRelease = { it.setColors(null) }
      ) {
        it.setCompoundDrawablesRelativeWithIntrinsicBounds(
          android.R.drawable.ic_dialog_alert,
          Resources.ID_NULL,
          android.R.drawable.ic_menu_delete,
          Resources.ID_NULL
        )
        runTest {
          object :
              InteropEditTextScope,
              CoroutineScope by this,
              SemanticsNodeInteractionsProvider by this@runComposeUiTest {
              override val view = view
              override val color = colorInArgb

              override fun addContent(
                @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
                toBeAddedContent: @Composable () -> Unit
              ) {
                if (content == null) {
                  content = toBeAddedContent
                }
              }
            }
            .body()
        }
      }
    }
  }
}
