package com.jeanbarrossilva.mastodonte.platform.ui.component

import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class FavoriteIconTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun isUnselectedWhenInactive() {
        composeRule.setContent {
            FavoriteIcon(isActive = false, onToggle = { })
        }
        composeRule.onFavoriteIcon().assertIsNotSelected()
    }

    @Test
    fun isSelectedWhenActive() {
        composeRule.setContent {
            FavoriteIcon(isActive = true, onToggle = { })
        }
        composeRule.onFavoriteIcon().assertIsSelected()
    }
}
