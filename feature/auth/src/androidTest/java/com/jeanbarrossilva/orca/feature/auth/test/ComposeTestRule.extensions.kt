package com.jeanbarrossilva.orca.feature.auth.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.feature.auth.AUTH_INSTANCE_FIELD_TAG
import com.jeanbarrossilva.orca.feature.auth.AUTH_SIGN_IN_BUTTON_TAG
import com.jeanbarrossilva.orca.feature.auth.AUTH_USERNAME_FIELD_TAG

/** [SemanticsNodeInteraction] of the username field. **/
internal fun ComposeTestRule.onUsernameField(): SemanticsNodeInteraction {
    return onNodeWithTag(AUTH_USERNAME_FIELD_TAG)
}

/** [SemanticsNodeInteraction] of the instance field. **/
internal fun ComposeTestRule.onInstanceField(): SemanticsNodeInteraction {
    return onNodeWithTag(AUTH_INSTANCE_FIELD_TAG)
}

/** [SemanticsNodeInteraction] of the sign-in button. **/
internal fun ComposeTestRule.onSignInButton(): SemanticsNodeInteraction {
    return onNodeWithTag(AUTH_SIGN_IN_BUTTON_TAG)
}
