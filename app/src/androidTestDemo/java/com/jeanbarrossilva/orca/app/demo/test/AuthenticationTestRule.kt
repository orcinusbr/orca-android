package com.jeanbarrossilva.orca.app.demo.test

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.feature.auth.AUTH_INSTANCE_FIELD_TAG
import com.jeanbarrossilva.orca.feature.auth.AUTH_SIGN_IN_BUTTON_TAG
import com.jeanbarrossilva.orca.feature.auth.AUTH_USERNAME_FIELD_TAG
import org.junit.rules.ExternalResource

internal class AuthenticationTestRule(private val composeRule: ComposeTestRule) :
    ExternalResource() {
    override fun before() {
        composeRule.onNodeWithTag(AUTH_USERNAME_FIELD_TAG).performTextInput(Account.sample.username)
        composeRule.onNodeWithTag(AUTH_INSTANCE_FIELD_TAG).performTextInput(Account.sample.instance)
        composeRule.onNodeWithTag(AUTH_SIGN_IN_BUTTON_TAG).performClick()
    }
}
