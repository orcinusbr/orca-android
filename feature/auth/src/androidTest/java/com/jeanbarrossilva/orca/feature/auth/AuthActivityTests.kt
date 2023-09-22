package com.jeanbarrossilva.orca.feature.auth

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.feature.auth.test.AuthModule
import com.jeanbarrossilva.orca.feature.auth.test.onInstanceField
import com.jeanbarrossilva.orca.feature.auth.test.onSignInButton
import com.jeanbarrossilva.orca.feature.auth.test.onUsernameField
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state.CompleteLifecycleState
import com.jeanbarrossilva.orca.platform.ui.test.core.lifecycle.state.assertIsAtLeast
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.koin.test.KoinTestRule

internal class AuthActivityTests {
    private val koinRule = KoinTestRule.create { modules(AuthModule()) }
    private val composeRule = createAndroidComposeRule<AuthActivity>()

    @get:Rule
    val ruleChain: RuleChain? = RuleChain.outerRule(koinRule).around(composeRule)

    @Test
    fun signsInWhenAccountIsValidAndSignInButtonIsClicked() {
        composeRule.onUsernameField().performTextInput(Account.sample.username)
        composeRule.onInstanceField().performTextInput("${Account.sample.domain}")
        composeRule.onSignInButton().performClick()
        assertIsAtLeast(CompleteLifecycleState.PAUSED, composeRule.activity.completeLifecycleState)
    }
}
