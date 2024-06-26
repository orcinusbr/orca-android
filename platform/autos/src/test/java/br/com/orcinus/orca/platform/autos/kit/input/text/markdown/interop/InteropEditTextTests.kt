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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSameAs
import assertk.assertions.isTrue
import br.com.orcinus.orca.platform.autos.test.kit.input.text.markdown.interop.scope.runInteropEditTextTest
import kotlin.test.Ignore
import kotlin.test.Test
import org.hamcrest.Matchers.`is`
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class InteropEditTextTests {
  @Test
  fun leadingCompoundDrawableIndexIsThatOfTheLeftOneWhenLayoutDirectionIsLtr() {
    runInteropEditTextTest {
      view.setLayoutDirectionProvider { View.LAYOUT_DIRECTION_LTR }
      assertThat(view.leadingCompoundDrawableIndex)
        .isEqualTo(InteropEditText.LEFT_COMPOUND_DRAWABLE_INDEX)
    }
  }

  @Test
  fun leadingCompoundDrawableIndexIsThatOfTheRightOneWhenLayoutDirectionIsRtl() {
    runInteropEditTextTest {
      view.setLayoutDirectionProvider { View.LAYOUT_DIRECTION_RTL }
      assertThat(view.leadingCompoundDrawableIndex)
        .isEqualTo(InteropEditText.RIGHT_COMPOUND_DRAWABLE_INDEX)
    }
  }

  @Test
  fun trailingCompoundDrawableIndexIsThatOfTheRightOneWhenLayoutDirectionIsLtr() {
    runInteropEditTextTest {
      view.setLayoutDirectionProvider { View.LAYOUT_DIRECTION_LTR }
      assertThat(view.trailingCompoundDrawableIndex)
        .isEqualTo(InteropEditText.RIGHT_COMPOUND_DRAWABLE_INDEX)
    }
  }

  @Test
  fun trailingCompoundDrawableIndexIsThatOfTheLeftOneWhenLayoutDirectionIsRtl() {
    runInteropEditTextTest {
      view.setLayoutDirectionProvider { View.LAYOUT_DIRECTION_RTL }
      assertThat(view.trailingCompoundDrawableIndex)
        .isEqualTo(InteropEditText.LEFT_COMPOUND_DRAWABLE_INDEX)
    }
  }

  @Test
  fun changesBackgroundColorOnEnabling() {
    runInteropEditTextTest({ colors(unfocusedContainerColor = it) }) {
      view.isEnabled = true
      assertThat(view.background?.color).isEqualTo(color)
    }
  }

  @Test
  fun changesLeadingCompoundDrawableColorOnEnabling() {
    runInteropEditTextTest({ colors(unfocusedLeadingIconColor = it) }) {
      view.isEnabled = true
      assertThat(view.compoundDrawables[view.leadingCompoundDrawableIndex]?.color).isEqualTo(color)
    }
  }

  @Test
  fun changesTrailingCompoundDrawableColorOnEnabling() {
    runInteropEditTextTest({ colors(unfocusedTrailingIconColor = it) }) {
      view.isEnabled = true
      assertThat(view.compoundDrawables[view.trailingCompoundDrawableIndex]?.color).isEqualTo(color)
    }
  }

  @Test
  fun changesHintTextColorOnEnabling() {
    runInteropEditTextTest({ colors(unfocusedPlaceholderColor = it) }) {
      view.isEnabled = true
      assertThat(view.currentHintTextColor).isEqualTo(color)
    }
  }

  @Test
  fun changesTextColorOnEnabling() {
    runInteropEditTextTest({ colors(unfocusedTextColor = it) }) {
      view.isEnabled = true
      assertThat(view.currentTextColor).isEqualTo(color)
    }
  }

  @Test
  fun changesBackgroundColorOnFocusing() {
    runInteropEditTextTest({ colors(focusedContainerColor = it) }) {
      view.requestFocus()
      assertThat(view.background?.color).isEqualTo(color)
    }
  }

  @Test
  fun changesLeadingCompoundDrawableColorOnFocusing() {
    runInteropEditTextTest({ colors(focusedLeadingIconColor = it) }) {
      view.requestFocus()
      assertThat(view.compoundDrawables[view.leadingCompoundDrawableIndex]?.color).isEqualTo(color)
    }
  }

  @Test
  fun changesTrailingCompoundDrawableColorOnFocusing() {
    runInteropEditTextTest({ colors(focusedTrailingIconColor = it) }) {
      view.requestFocus()
      assertThat(view.compoundDrawables[view.trailingCompoundDrawableIndex]?.color).isEqualTo(color)
    }
  }

  @Test
  fun changesHintTextColorOnFocusing() {
    runInteropEditTextTest({ colors(focusedPlaceholderColor = it) }) {
      view.requestFocus()
      assertThat(view.currentHintTextColor).isEqualTo(color)
    }
  }

  @Test
  fun changesTextColorOnFocusing() {
    runInteropEditTextTest({ colors(focusedTextColor = it) }) {
      view.requestFocus()
      assertThat(view.currentTextColor).isEqualTo(color)
    }
  }

  @Test
  fun changesBackgroundColorOnError() {
    runInteropEditTextTest({ colors(errorContainerColor = it) }) {
      view.error = ""
      assertThat(view.background?.color).isEqualTo(color)
    }
  }

  @Test
  fun changesLeadingCompoundDrawableColorOnError() {
    runInteropEditTextTest({ colors(errorLeadingIconColor = it) }) {
      view.error = ""
      assertThat(view.compoundDrawables[view.leadingCompoundDrawableIndex]?.color).isEqualTo(color)
    }
  }

  @Test
  fun changesTrailingCompoundDrawableColorOnError() {
    runInteropEditTextTest({ colors(errorTrailingIconColor = it) }) {
      view.error = ""
      assertThat(view.compoundDrawables[view.trailingCompoundDrawableIndex]?.color).isEqualTo(color)
    }
  }

  @Test
  fun changesHintTextColorOnError() {
    runInteropEditTextTest({ colors(errorPlaceholderColor = it) }) {
      view.error = ""
      assertThat(view.currentHintTextColor).isEqualTo(color)
    }
  }

  @Test
  fun changesTextColorOnError() {
    runInteropEditTextTest({ colors(errorTextColor = it) }) {
      view.error = ""
      assertThat(view.currentTextColor).isEqualTo(color)
    }
  }

  @Test
  fun callsOnDone() {
    runInteropEditTextTest {
      var isDone = false
      view.setKeyboardActions(KeyboardActions(onDone = { isDone = true }))
      view.onEditorAction(EditorInfo.IME_ACTION_DONE)
      assertThat(isDone).isTrue()
    }
  }

  @Test
  fun callsOnGo() {
    runInteropEditTextTest {
      var hasGone = false
      view.setKeyboardActions(KeyboardActions(onGo = { hasGone = true }))
      view.onEditorAction(EditorInfo.IME_ACTION_GO)
      assertThat(hasGone).isTrue()
    }
  }

  @Test
  fun callsOnNext() {
    runInteropEditTextTest {
      var hasCalledOnNext = false
      view.setKeyboardActions(KeyboardActions(onNext = { hasCalledOnNext = true }))
      view.onEditorAction(EditorInfo.IME_ACTION_NEXT)
      assertThat(hasCalledOnNext).isTrue()
    }
  }

  @Test
  fun callsOnPrevious() {
    runInteropEditTextTest {
      var hasCalledOnPrevious = false
      view.setKeyboardActions(KeyboardActions(onPrevious = { hasCalledOnPrevious = true }))
      view.onEditorAction(EditorInfo.IME_ACTION_PREVIOUS)
      assertThat(hasCalledOnPrevious).isTrue()
    }
  }

  @Test
  fun callsOnSearch() {
    runInteropEditTextTest {
      var hasSearched = false
      view.setKeyboardActions(KeyboardActions(onSearch = { hasSearched = true }))
      view.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
      assertThat(hasSearched).isTrue()
    }
  }

  @Test
  fun callsOnSend() {
    runInteropEditTextTest {
      var hasSent = false
      view.setKeyboardActions(KeyboardActions(onSend = { hasSent = true }))
      view.onEditorAction(EditorInfo.IME_ACTION_SEND)
      assertThat(hasSent).isTrue()
    }
  }

  @Ignore("Auto-correction testing seems momentarily impracticable.")
  @Test
  fun enablesAutoCorrect() {
    runInteropEditTextTest {
      view.setKeyboardOptions(KeyboardOptions(autoCorrect = true))
      onView(`is`(view))
        .perform(typeText(@Suppress("SpellCheckingInspection") "Helo"))
        .perform(pressImeActionButton())
        .check(matches(withText("Hello")))
    }
  }

  @Ignore("Auto-correction testing seems momentarily impracticable.")
  @Test
  fun disablesAutoCorrect() {
    runInteropEditTextTest {
      view.setKeyboardOptions(KeyboardOptions(autoCorrect = false))
      onView(`is`(view))
        .perform(typeText(@Suppress("SpellCheckingInspection") "Helo"))
        .perform(pressImeActionButton())
        .check(matches(withText(@Suppress("SpellCheckingInspection") "Helo")))
    }
  }

  @Test
  fun listensToSelection() {
    runInteropEditTextTest {
      var selection = IntRange.EMPTY
      val listener = OnSelectionChangeListener { selection = it }
      view.addOnSelectionChangeListener(listener)
      view.setText("Hello!")
      view.setSelection(0, 5)
      view.removeOnSelectionChangeListener(listener)
      assertThat(selection).isEqualTo(0..5)
    }
  }

  @Test
  fun stopsListeningToSelection() {
    runInteropEditTextTest {
      var selection = IntRange.EMPTY
      val listener = OnSelectionChangeListener { selection = it }
      view.addOnSelectionChangeListener(listener)
      view.removeOnSelectionChangeListener(listener)
      view.setText("Hello!")
      view.setSelection(5, 6)
      assertThat(selection).isSameAs(IntRange.EMPTY)
    }
  }
}
