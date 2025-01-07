/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.feature.composer

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import br.com.orcinus.orca.composite.timeline.test.composition.interop.scope.runCompositionTextFieldTest
import br.com.orcinus.orca.platform.testing.activity.scenario.activity
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class WithMarkdownTextActionTests {
  @Test(expected = AssertionError::class)
  fun doesNotMatchNonEditText() {
    launchActivity<ComponentActivity>().use {
      val nonEditTextView = TextView(it.activity).apply { text = "Hello, world!" }
      it.activity?.addContentView(
        nonEditTextView,
        FrameLayout.LayoutParams(
          FrameLayout.LayoutParams.WRAP_CONTENT,
          FrameLayout.LayoutParams.WRAP_CONTENT
        )
      )
      onView(`is`(nonEditTextView)).check(matches(withText(Markdown.unstyled("Hello, world!"))))
    }
  }

  @Test(expected = AssertionError::class)
  fun doesNotMatchEditTextWhoseTextIsNotEquivalentToTheSpecifiedOne() {
    runCompositionTextFieldTest {
      onView(`is`(textField))
        .perform(typeText("Hello, world!"))
        .check(
          matches(
            withText(
              buildMarkdown {
                bold { +"Hello" }
                +", "
                italic { +"world" }
                +'!'
              }
            )
          )
        )
    }
  }

  @Test
  fun matchesEditTextWhoseTextIsSpannedAndIsEquivalentToTheSpecifiedOne() {
    runCompositionTextFieldTest {
      textField.text =
        SpannableStringBuilder()
          .append("Hello", StyleSpan(Typeface.BOLD), 0)
          .append(", ")
          .append("world", StyleSpan(Typeface.ITALIC), 0)
          .append('!')
      onView(`is`(textField))
        .check(
          matches(
            withText(
              buildMarkdown {
                bold { +"Hello" }
                +", "
                italic { +"world" }
                +'!'
              }
            )
          )
        )
    }
  }

  @Test
  fun matchesEditTextWhoseTextIsMarkdownAndEqualsToTheSpecifiedOne() {
    runCompositionTextFieldTest {
      val text = buildMarkdown {
        bold { +"Hello" }
        +", "
        italic { +"world" }
        +'!'
      }
      textField.setText(text)
      onView(`is`(textField)).check(matches(withText(text)))
    }
  }
}
