package com.jeanbarrossilva.mastodonte.platform.ui.component

import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import com.jeanbarrossilva.mastodonte.platform.ui.component.stat.FavoriteStatIcon
import org.junit.Rule
import org.junit.Test

internal class FavoriteIconTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun isUnselectedWhenInactive() {
        composeRule.setContent {
            FavoriteStatIcon(isActive = false, onToggle = { })
        }
        composeRule.onFavoriteIcon().assertIsNotSelected()
    }

    @Test
    fun isSelectedWhenActive() {
        composeRule.setContent {
            FavoriteStatIcon(isActive = true, onToggle = { })
        }
        composeRule.onFavoriteIcon().assertIsSelected()
    }
}
