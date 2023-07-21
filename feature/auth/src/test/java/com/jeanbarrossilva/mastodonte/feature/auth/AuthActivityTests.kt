package com.jeanbarrossilva.mastodonte.feature.auth

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.jeanbarrossilva.mastodonte.core.account.Account
import com.jeanbarrossilva.mastodonte.core.sample.account.sample
import com.jeanbarrossilva.mastodonte.feature.auth.test.AuthModule
import com.jeanbarrossilva.mastodonte.feature.auth.test.onInstanceField
import com.jeanbarrossilva.mastodonte.feature.auth.test.onSignInButton
import com.jeanbarrossilva.mastodonte.feature.auth.test.onUsernameField
import com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle.state.CompleteLifecycleState
import com.jeanbarrossilva.mastodonte.platform.ui.test.core.lifecycle.state.assertIsAtLeast
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTestRule
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AuthActivityTests {
    @get:Rule
    val koinRule = KoinTestRule.create { modules(AuthModule()) }

    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun `GIVEN an account WHEN signing in with it THEN it signs in`() {
        Robolectric.buildActivity(AuthActivity::class.java).setup().use {
            composeRule.onUsernameField().performTextInput(Account.sample.username)
            composeRule.onInstanceField().performTextInput(Account.sample.instance)
            composeRule.onSignInButton().performClick()
            assertIsAtLeast(CompleteLifecycleState.PAUSED, it.get().completeLifecycleState)
        }
    }
}
