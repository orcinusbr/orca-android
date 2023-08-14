package com.jeanbarrossilva.orca.feature.auth

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.kit.action.PrimaryButton
import com.jeanbarrossilva.orca.platform.theme.kit.input.TextField
import com.jeanbarrossilva.orca.platform.ui.core.requestFocusWithDelay

/** Tag that identifies the username field for testing purposes. **/
internal const val AUTH_USERNAME_FIELD_TAG = "auth-username-field"

/** Tag that identifies the instance field for testing purposes. **/
internal const val AUTH_INSTANCE_FIELD_TAG = "auth-instance-field"

/** Tag that identifies the sign-in button for testing purposes. **/
internal const val AUTH_SIGN_IN_BUTTON_TAG = "auth-sign-in-button"

@Composable
internal fun Auth(viewModel: AuthViewModel, modifier: Modifier = Modifier) {
    val username by viewModel.usernameFlow.collectAsState()
    val instance by viewModel.instanceFlow.collectAsState()

    Auth(
        username,
        onUsernameChange = viewModel::setUsername,
        instance,
        onInstanceChange = viewModel::setInstance,
        onSignIn = viewModel::signIn,
        modifier
    )
}

@Composable
internal fun Auth(
    username: String,
    onUsernameChange: (username: String) -> Unit,
    instance: String,
    onInstanceChange: (instance: String) -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = OrcaTheme.spacings.extraLarge
    val usernameFocusRequester = remember(::FocusRequester)
    val nextButtonFocusRequester = remember(::FocusRequester)

    LaunchedEffect(Unit) {
        usernameFocusRequester.requestFocusWithDelay()
    }

    Box(
        modifier
            .background(OrcaTheme.colors.background)
            .imePadding()
            .padding(spacing)
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = spacing)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.small),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Welcome!",
                        textAlign = TextAlign.Center,
                        style = OrcaTheme.typography.headlineLarge
                    )

                    Text(
                        "What is your account?",
                        textAlign = TextAlign.Center,
                        style = OrcaTheme.typography.titleSmall
                    )
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement
                        .spacedBy(OrcaTheme.spacings.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        username,
                        onUsernameChange,
                        Modifier
                            .focusRequester(usernameFocusRequester)
                            .fillMaxWidth(.45f)
                            .testTag(AUTH_USERNAME_FIELD_TAG),
                        KeyboardOptions(imeAction = ImeAction.Next),
                        isSingleLined = true
                    ) {
                        Text("Username")
                    }

                    Text("@")

                    TextField(
                        instance,
                        onInstanceChange,
                        Modifier
                            .fillMaxWidth()
                            .testTag(AUTH_INSTANCE_FIELD_TAG),
                        KeyboardOptions(imeAction = ImeAction.Done),
                        KeyboardActions(onDone = { nextButtonFocusRequester.requestFocus() }),
                        isSingleLined = true
                    ) {
                        Text("Instance")
                    }
                }
            }
        }

        PrimaryButton(
            onClick = onSignIn,
            Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .testTag(AUTH_SIGN_IN_BUTTON_TAG),
            isEnabled = Account.isUsernameValid(username) && Account.isInstanceValid(instance)
        ) {
            Text("Sign in")
        }
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun InvalidAuthPreview() {
    OrcaTheme {
        Auth(username = "", instance = "")
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ValidAuthPreview() {
    OrcaTheme {
        Auth(Account.sample.username, Account.sample.instance)
    }
}

@Composable
private fun Auth(username: String, instance: String, modifier: Modifier = Modifier) {
    Auth(
        username,
        onUsernameChange = { },
        instance,
        onInstanceChange = { },
        onSignIn = { },
        modifier
    )
}
