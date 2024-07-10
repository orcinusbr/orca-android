/*
 * Copyright © 2023–2024 Orcinus
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

import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.CompositionTextField
import br.com.orcinus.orca.std.markdown.buildMarkdown
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ComposerActivityTests {
  @get:Rule val composeRule = createAndroidComposeRule<ComposerActivity>()

  @Test
  fun stylesSelectedComposition() {
    onView(isAssignableFrom(CompositionTextField::class.java))
      .perform(typeText("Hello, world!"), selectText(0..5))
    composeRule.onToolbar().onChildren().filterToOne(isBoldFormat()).performClick()
    onView(isAssignableFrom(CompositionTextField::class.java))
      .check(
        matches(
          withText(
            buildMarkdown {
              bold { +"Hello" }
              +", world!"
            }
          )
        )
      )
  }

  @Test
  fun keepsStylizationWhenUnselectingStylizedComposition() {
    onView(isAssignableFrom(CompositionTextField::class.java))
      .perform(typeText("Hello, world!"), selectText(7..12))
    composeRule.onToolbar().onChildren().filterToOne(isItalicFormat()).performClick()
    repeat(64) {
      onView(isAssignableFrom(CompositionTextField::class.java))
        .perform(selectText((0..13).random()))
    }
    onView(isAssignableFrom(CompositionTextField::class.java))
      .check(
        matches(
          withText(
            buildMarkdown {
              +"Hello, "
              italic { +"world" }
              +'!'
            }
          )
        )
      )
  }
}
