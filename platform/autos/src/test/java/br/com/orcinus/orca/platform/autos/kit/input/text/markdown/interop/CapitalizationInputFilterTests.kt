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

import android.text.SpannableString
import android.text.SpannedString
import androidx.core.text.getSpans
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class CapitalizationInputFilterTests {
  private val inputFilters =
    arrayOf(CapitalizationInputFilter.Sentence, CapitalizationInputFilter.Word)

  @Test
  fun capitalizesFirstSentence() {
    inputFilters.forEach {
      assertThat(
          it.filter(
            source = "hello!",
            start = 0,
            end = 5,
            dest = SpannedString(""),
            dstart = 0,
            dend = 1
          )
        )
        .isEqualTo("Hello!")
    }
  }

  @Test
  fun capitalizesParagraphs() {
    inputFilters.forEach {
      assertThat(
          it.filter(
            source = "hello\nworld",
            start = 0,
            end = 5,
            dest = SpannedString(""),
            dstart = 0,
            dend = 1
          )
        )
        .isEqualTo("Hello\nWorld")
    }
  }

  @Test
  fun capitalizesDotDelimitedSentences() {
    inputFilters.forEach {
      assertThat(
          it.filter(
            source = "hello. world.",
            start = 0,
            end = 5,
            dest = SpannedString(""),
            dstart = 0,
            dend = 1
          )
        )
        .isEqualTo("Hello. World.")
    }
  }

  @Test
  fun capitalizesEllipsisDelimitedSentences() {
    inputFilters.forEach {
      assertThat(
          it.filter(
            source = "hello… world…",
            start = 0,
            end = 5,
            dest = SpannedString(""),
            dstart = 0,
            dend = 1
          )
        )
        .isEqualTo("Hello… World…")
    }
  }

  @Test
  fun capitalizesExclamations() {
    inputFilters.forEach {
      assertThat(
          it.filter(
            source = "hello! world!",
            start = 0,
            end = 5,
            dest = SpannedString(""),
            dstart = 0,
            dend = 1
          )
        )
        .isEqualTo("Hello! World!")
    }
  }

  @Test
  fun capitalizesQuestions() {
    inputFilters.forEach {
      assertThat(
          it.filter(
            source = "hello? world?",
            start = 0,
            end = 5,
            dest = SpannedString(""),
            dstart = 0,
            dend = 1
          )
        )
        .isEqualTo("Hello? World?")
    }
  }

  @Test
  fun maintainsSpansWhenCapitalizing() {
    val span: Byte = 0
    val destination = SpannableString("")
    inputFilters.forEach {
      it.filter(
        source = SpannableString("ßen").apply { setSpan(span, 0, 1, 0) },
        start = 0,
        end = 1,
        destination,
        dstart = 0,
        dend = 1
      )
      assertThat(destination.getSpans<Byte>(0, 1)).containsExactly(span)
    }
  }
}
