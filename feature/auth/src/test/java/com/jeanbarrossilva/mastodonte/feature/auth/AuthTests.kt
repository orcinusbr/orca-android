package com.jeanbarrossilva.mastodonte.feature.auth

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performTextInput
import com.jeanbarrossilva.mastodonte.core.account.Account
import com.jeanbarrossilva.mastodonte.core.sample.account.sample
import com.jeanbarrossilva.mastodonte.feature.auth.test.onInstanceField
import com.jeanbarrossilva.mastodonte.feature.auth.test.onSignInButton
import com.jeanbarrossilva.mastodonte.feature.auth.test.onUsernameField
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.GraphicsMode

@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(RobolectricTestRunner::class)
internal class AuthTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `GIVEN an invalid username and a valid instance WHEN inputting them THEN the sign-in button is disabled`() { // ktlint-disable max-line-length
        composeRule.setContent {
            MastodonteTheme {
                TestAuth()
            }
        }
        composeRule.onUsernameField().performTextInput("john@")
        composeRule.onInstanceField().performTextInput(Account.sample.instance)
        composeRule.onSignInButton().assertIsNotEnabled()
    }

    @Test
    fun `GIVEN a valid username and an invalid instance WHEN inputting them THEN the sign-in button is disabled`() { // ktlint-disable max-line-length
        composeRule.setContent {
            MastodonteTheme {
                TestAuth()
            }
        }
        composeRule.onUsernameField().performTextInput(Account.sample.username)
        composeRule.onInstanceField().performTextInput("appleseed")
        composeRule.onSignInButton().assertIsNotEnabled()
    }

    @Test
    fun `GIVEN a valid account WHEN inputting it THEN the sign-in button is enabled`() {
        composeRule.setContent {
            MastodonteTheme {
                TestAuth()
            }
        }
        composeRule.onUsernameField().performTextInput(Account.sample.username)
        composeRule.onInstanceField().performTextInput(Account.sample.instance)
        composeRule.onSignInButton().assertIsEnabled()
    }
}
