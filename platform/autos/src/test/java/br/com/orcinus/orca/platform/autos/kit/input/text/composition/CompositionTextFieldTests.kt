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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition

import android.app.Activity
import android.graphics.Typeface
import android.text.style.StyleSpan
import androidx.test.core.app.launchActivity
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned.IndexedSpans
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned.getIndexedSpans
import br.com.orcinus.orca.platform.navigation.content
import br.com.orcinus.orca.platform.testing.activity.scenario.activity
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.markdown.buildMarkdown
import kotlin.test.Test
import kotlin.time.Duration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class CompositionTextFieldTests {
  class WindowDetachmentActivity : Activity()

  @Test
  fun setsMarkdownText() {
    runTest(@OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()) {
      assertThat(
          CompositionTextField(context, this)
            .apply {
              setText(
                buildMarkdown {
                  italic { +"Hello" }
                  +", "
                  bold { +"world" }
                  +'!'
                }
              )
            }
            .text
        )
        .all {
          transform("text") { it?.toString() }.isEqualTo("Hello, world!")
          transform("indexed spans") { it?.getIndexedSpans(context) }
            .isNotNull()
            .areStructurallyEqual(
              IndexedSpans(context, 0..5, StyleSpan(Typeface.ITALIC)),
              IndexedSpans(context, 7..12, StyleSpan(Typeface.BOLD))
            )
        }
    }
  }

  @Test
  fun cancelsTextSettingsWhenSettingNonMarkdownBasedText() {
    var exception: CancellationException? = null
    runTest {
      launch(Dispatchers.Unconfined) { delay(Duration.INFINITE) }
        .invokeOnCompletion { exception = it as? CancellationException }
      CompositionTextField(context, this).setText("Hello, world!")
    }
    assertThat(exception).isNotNull().isInstanceOf<CompositionTextField.ResetTextException>()
  }

  @Test
  fun cancelsTextSettingsWhenDetachedFromTheWindow() {
    var exception: CancellationException? = null
    runTest {
      launch(Dispatchers.Unconfined) { delay(Duration.INFINITE) }
        .invokeOnCompletion { exception = it as? CancellationException }
      launchActivity<WindowDetachmentActivity>().use {
        it.activity?.content?.addView(CompositionTextField(context, this))
      }
    }
    assertThat(exception)
      .isNotNull()
      .isInstanceOf<CompositionTextField.DetachedFromWindowException>()
  }
}
