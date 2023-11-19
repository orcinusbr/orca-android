package com.jeanbarrossilva.orca.platform.autos.kit.input.option.list

import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onSiblings
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.orca.autos.forms.Forms
import com.jeanbarrossilva.orca.platform.autos.kit.bottom
import com.jeanbarrossilva.orca.platform.autos.kit.input.option.OptionDefaults
import com.jeanbarrossilva.orca.platform.autos.kit.input.option.list.test.assertIsShapedBy
import com.jeanbarrossilva.orca.platform.autos.kit.input.option.test.onOption
import com.jeanbarrossilva.orca.platform.autos.kit.input.option.test.onOptions
import com.jeanbarrossilva.orca.platform.autos.kit.top
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

internal class OptionsTests {
  private val defaultOptionShape = OptionDefaults.getShape(Forms.default)

  @get:Rule val composeRule = createComposeRule()

  @Test
  fun shapesSingleOptionWithDefaultShape() {
    composeRule.setContent { AutosTheme { SampleOptions(count = 1) } }
    composeRule.onOption().assertIsShapedBy(defaultOptionShape)
  }

  @Test
  fun shapesFirstOptionWithZeroedBottomCornerSizes() {
    composeRule.setContent { AutosTheme { Surface(color = Color.Transparent) { SampleOptions() } } }
    composeRule.onOptions().onFirst().assertIsShapedBy(defaultOptionShape.top)
  }

  @Test
  fun shapesIntermediateOptionWithZeroedCornerSizes() {
    composeRule.setContent { AutosTheme { SampleOptions() } }
    composeRule.onOptions()[1].assertIsShapedBy(RectangleShape)
  }

  @Test
  fun shapesLastOptionWithZeroedTopCornerSizes() {
    composeRule.setContent { AutosTheme { Surface(color = Color.Transparent) { SampleOptions() } } }
    composeRule.onOptions().onLast().assertIsShapedBy(defaultOptionShape.bottom)
  }

  @Test
  fun surroundsIntermediateOptionWithDividers() {
    composeRule.setContent { AutosTheme { SampleOptions() } }
    composeRule
      .onOptions()[1]
      .onSiblings()
      .filter(hasTestTag(OPTIONS_DIVIDER_TAG))
      .assertCountEquals(2)
  }

  @Test
  fun selectsFirstOptionByDefault() {
    composeRule.setContent { AutosTheme { SampleOptions() } }
    composeRule.onOptions().onFirst().assertIsSelected()
  }

  @Test
  fun runsCallbackWhenOptionIsSelectedByDefault() {
    var index = -1
    composeRule.setContent { AutosTheme { SampleOptions(onSelection = { index = it }) } }
    assertEquals(0, index)
  }

  @Test
  fun runsCallbackOnceWhenOptionIsSelectedMultipleTimes() {
    var count = 0
    composeRule.setContent {
      AutosTheme {
        SampleOptions(
          onSelection = {
            if (it == 0) {
              count++
            }
          }
        )
      }
    }
    composeRule.onOptions().onFirst().performClick()
    assertEquals(1, count)
  }

  @Test
  fun unselectsPreviouslySelectedOptionWhenAnotherOneIsSelected() {
    composeRule.setContent { AutosTheme { SampleOptions() } }
    composeRule.onOptions().onLast().performClick()
    composeRule.onOptions().onFirst().assertIsNotSelected()
  }
}
