package com.jeanbarrossilva.orca.core.http.auth.authentication.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.core.http.auth.authorization.HTTP_AUTHORIZATION_SIGN_IN_BUTTON_TAG

/** [SemanticsNodeInteraction] of the sign-in button. **/
internal fun ComposeTestRule.onSignInButton(): SemanticsNodeInteraction {
    return onNodeWithTag(HTTP_AUTHORIZATION_SIGN_IN_BUTTON_TAG)
}
