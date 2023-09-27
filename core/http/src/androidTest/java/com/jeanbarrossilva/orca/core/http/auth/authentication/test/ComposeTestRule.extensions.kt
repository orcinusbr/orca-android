package com.jeanbarrossilva.orca.core.http.auth.authentication.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.core.http.auth.authentication.HTTP_AUTHORIZATION_INSTANCE_FIELD_TAG
import com.jeanbarrossilva.orca.core.http.auth.authentication.HTTP_AUTHORIZATION_SIGN_IN_BUTTON_TAG
import com.jeanbarrossilva.orca.core.http.auth.authentication.HTTP_AUTHORIZATION_USERNAME_FIELD_TAG

/** [SemanticsNodeInteraction] of the username field. **/
internal fun ComposeTestRule.onUsernameField(): SemanticsNodeInteraction {
    return onNodeWithTag(HTTP_AUTHORIZATION_USERNAME_FIELD_TAG)
}

/** [SemanticsNodeInteraction] of the instance field. **/
internal fun ComposeTestRule.onInstanceField(): SemanticsNodeInteraction {
    return onNodeWithTag(HTTP_AUTHORIZATION_INSTANCE_FIELD_TAG)
}

/** [SemanticsNodeInteraction] of the sign-in button. **/
internal fun ComposeTestRule.onSignInButton(): SemanticsNodeInteraction {
    return onNodeWithTag(HTTP_AUTHORIZATION_SIGN_IN_BUTTON_TAG)
}
