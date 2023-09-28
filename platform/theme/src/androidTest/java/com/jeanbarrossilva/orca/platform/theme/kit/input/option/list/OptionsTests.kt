package com.jeanbarrossilva.orca.platform.theme.kit.input.option.list

import androidx.compose.foundation.shape.CornerBasedShape
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
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.configuration.Shapes
import com.jeanbarrossilva.orca.platform.theme.extensions.bottom
import com.jeanbarrossilva.orca.platform.theme.extensions.top
import com.jeanbarrossilva.orca.platform.theme.kit.input.option.OptionDefaults
import com.jeanbarrossilva.orca.platform.theme.kit.input.option.list.test.assertIsShapedBy
import com.jeanbarrossilva.orca.platform.theme.kit.input.option.test.onOption
import com.jeanbarrossilva.orca.platform.theme.kit.input.option.test.onOptions
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

internal class OptionsTests {
    private val defaultOptionShape: CornerBasedShape
        get() {
            val context = InstrumentationRegistry.getInstrumentation().context
            val shapes = Shapes.getDefault(context)
            return OptionDefaults.getShape(shapes)
        }

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shapesSingleOptionWithDefaultShape() {
        composeRule.setContent {
            OrcaTheme {
                SampleOptions(count = 1)
            }
        }
        composeRule.onOption().assertIsShapedBy(defaultOptionShape)
    }

    @Test
    fun shapesFirstOptionWithZeroedBottomCornerSizes() {
        composeRule.setContent {
            OrcaTheme {
                Surface(color = Color.Transparent) {
                    SampleOptions()
                }
            }
        }
        composeRule.onOptions().onFirst().assertIsShapedBy(defaultOptionShape.top)
    }

    @Test
    fun shapesIntermediateOptionWithZeroedCornerSizes() {
        composeRule.setContent {
            OrcaTheme {
                SampleOptions()
            }
        }
        composeRule.onOptions()[1].assertIsShapedBy(RectangleShape)
    }

    @Test
    fun shapesLastOptionWithZeroedTopCornerSizes() {
        composeRule.setContent {
            OrcaTheme {
                Surface(color = Color.Transparent) {
                    SampleOptions()
                }
            }
        }
        composeRule.onOptions().onLast().assertIsShapedBy(defaultOptionShape.bottom)
    }

    @Test
    fun surroundsIntermediateOptionWithDividers() {
        composeRule.setContent {
            OrcaTheme {
                SampleOptions()
            }
        }
        composeRule
            .onOptions()[1]
            .onSiblings()
            .filter(hasTestTag(OPTIONS_DIVIDER_TAG))
            .assertCountEquals(2)
    }

    @Test
    fun selectsFirstOptionByDefault() {
        composeRule.setContent {
            OrcaTheme {
                SampleOptions()
            }
        }
        composeRule.onOptions().onFirst().assertIsSelected()
    }

    @Test
    fun runsCallbackWhenOptionIsSelectedByDefault() {
        var selection = IndexedValue<Boolean?>(index = -1, null)
        composeRule.setContent {
            OrcaTheme {
                SampleOptions(
                    onSelection = { index, isSelected ->
                        selection = IndexedValue(index, isSelected)
                    }
                )
            }
        }
        assertEquals(IndexedValue(index = 0, true), selection)
    }

    @Test
    fun runsCallbackOnceWhenOptionIsSelectedMultipleTimes() {
        var count = 0
        composeRule.setContent {
            OrcaTheme {
                SampleOptions(
                    onSelection = { index, _ ->
                        if (index == 0) {
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
        composeRule.setContent {
            OrcaTheme {
                SampleOptions()
            }
        }
        composeRule.onOptions().onLast().performClick()
        composeRule.onOptions().onFirst().assertIsNotSelected()
    }
}
