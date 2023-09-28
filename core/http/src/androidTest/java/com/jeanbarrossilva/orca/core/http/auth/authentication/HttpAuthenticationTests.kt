package com.jeanbarrossilva.orca.core.http.auth.authentication

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performTextInput
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.http.auth.authentication.test.onInstanceField
import com.jeanbarrossilva.orca.core.http.auth.authentication.test.onSignInButton
import com.jeanbarrossilva.orca.core.http.auth.authentication.test.onUsernameField
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorization
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import org.junit.Rule
import org.junit.Test

internal class HttpAuthenticationTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun disablesSignInButtonWhenUsernameIsInvalidAndInstanceIsValid() {
        composeRule.setContent {
            OrcaTheme {
                HttpAuthorization()
            }
        }
        composeRule.onUsernameField().performTextInput("john@")
        composeRule.onInstanceField().performTextInput("${Account.sample.domain}")
        composeRule.onSignInButton().assertIsNotEnabled()
    }

    @Test
    fun disablesSignInButtonWhenUsernameIsValidAndInstanceIsInvalid() {
        composeRule.setContent {
            OrcaTheme {
                HttpAuthorization()
            }
        }
        composeRule.onUsernameField().performTextInput(Account.sample.username)
        composeRule.onInstanceField().performTextInput("appleseed")
        composeRule.onSignInButton().assertIsNotEnabled()
    }
}
