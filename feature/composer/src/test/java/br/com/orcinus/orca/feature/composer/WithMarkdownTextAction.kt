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

package br.com.orcinus.orca.feature.composer

import android.view.View
import android.widget.EditText
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.spanned.toMarkdown
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.markdown.Markdown
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeDiagnosingMatcher

/**
 * [Matcher] that matches an [EditText] whose text equals to the given one when converted into
 * [Markdown].
 *
 * @property text [Markdown] that is expected.
 */
private class WithMarkdownTextAction(private val text: Markdown) :
  TypeSafeDiagnosingMatcher<View>() {
  override fun describeTo(description: Description?) {
    description?.appendText("view.getText() is ")?.appendValue(text)?.apply {
      if (text.styles.isEmpty()) {
        appendText(", unstyled")
      } else {
        appendValueList(", styled with ", ", ", "", text.styles)
      }
    }
  }

  override fun matchesSafely(item: View?, mismatchDescription: Description?): Boolean {
    if (item is EditText) {
      val text = item.text.toMarkdown(context)
      if (text != this.text) {
        mismatchDescription?.appendText("view.getText() is ")?.appendValue(text)?.apply {
          if (text.styles.isEmpty()) {
            appendText(", unstyled")
          } else {
            appendValueList(", styled with ", ", ", "", text.styles)
          }
        }
        return false
      }
    }
    return true
  }
}

/**
 * Creates a [Matcher] that matches an [EditText] whose text equals to the given one when converted
 * into [Markdown].
 *
 * @param text [Markdown] that is expected.
 */
internal fun withText(text: Markdown): Matcher<View> {
  return WithMarkdownTextAction(text)
}
