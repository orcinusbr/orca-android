package com.jeanbarrossilva.orca.feature.auth

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performTextInput
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.feature.auth.test.TestAuth
import com.jeanbarrossilva.orca.feature.auth.test.onInstanceField
import com.jeanbarrossilva.orca.feature.auth.test.onSignInButton
import com.jeanbarrossilva.orca.feature.auth.test.onUsernameField
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import org.junit.Rule
import org.junit.Test

internal class AuthTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun disablesSignInButtonWhenUsernameIsInvalidAndInstanceIsValid() { // ktlint-disable max-line-length
        composeRule.setContent {
            OrcaTheme {
                TestAuth()
            }
        }
        composeRule.onUsernameField().performTextInput("john@")
        composeRule.onInstanceField().performTextInput(Account.sample.instance)
        composeRule.onSignInButton().assertIsNotEnabled()
    }

    @Test
    fun disabledSignInButtonWhenUsernameIsValidAndInstanceIsInvalid() { // ktlint-disable max-line-length
        composeRule.setContent {
            OrcaTheme {
                TestAuth()
            }
        }
        composeRule.onUsernameField().performTextInput(Account.sample.username)
        composeRule.onInstanceField().performTextInput("appleseed")
        composeRule.onSignInButton().assertIsNotEnabled()
    }

    @Test
    fun enablesSignInButtonWhenAccountIsValid() {
        composeRule.setContent {
            OrcaTheme {
                TestAuth()
            }
        }
        composeRule.onUsernameField().performTextInput(Account.sample.username)
        composeRule.onInstanceField().performTextInput(Account.sample.instance)
        composeRule.onSignInButton().assertIsEnabled()
    }
}
