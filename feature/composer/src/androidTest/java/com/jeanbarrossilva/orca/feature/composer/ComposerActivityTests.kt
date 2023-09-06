package com.jeanbarrossilva.orca.feature.composer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextInputSelection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.jeanbarrossilva.orca.feature.composer.test.ComposerModule
import com.jeanbarrossilva.orca.feature.composer.test.assertTextEquals
import com.jeanbarrossilva.orca.feature.composer.test.isBoldFormat
import com.jeanbarrossilva.orca.feature.composer.test.isItalicFormat
import com.jeanbarrossilva.orca.feature.composer.test.onField
import com.jeanbarrossilva.orca.feature.composer.test.onToolbar
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.koin.test.KoinTestRule

@OptIn(ExperimentalTestApi::class)
internal class ComposerActivityTests {
    private val koinRule = KoinTestRule.create { modules(ComposerModule()) }
    private val composeRule = createAndroidComposeRule<ComposerActivity>()

    @get:Rule
    val ruleChain: RuleChain? = RuleChain.outerRule(koinRule).around(composeRule)

    @Test
    fun stylesSelectedComposition() {
        composeRule.onField().performTextInput("Hello, world!")
        composeRule.onField().performTextInputSelection(TextRange(0, 5))
        composeRule.onToolbar().onChildren().filterToOne(isBoldFormat()).performClick()
        composeRule.onField().assertTextEquals(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Hello") }
                append(", world!")
            }
        )
    }

    @Test
    fun keepsStylizationWhenUnselectingStylizedComposition() { // ktlint-disable max-line-length
        composeRule.onField().performTextInput("Hello, world!")
        composeRule.onField().performTextInputSelection(TextRange(7, 12))
        composeRule.onToolbar().onChildren().filterToOne(isItalicFormat()).performClick()
        repeat(64) { composeRule.onField().performTextInputSelection(TextRange((0..12).random())) }
        composeRule.onField().assertTextEquals(
            buildAnnotatedString {
                append("Hello, ")
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("world") }
                append('!')
            }
        )
    }
}
