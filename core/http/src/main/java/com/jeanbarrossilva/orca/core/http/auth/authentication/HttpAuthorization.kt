package com.jeanbarrossilva.orca.core.http.auth.authentication

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizationViewModel
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.kit.action.button.PrimaryButton
import com.jeanbarrossilva.orca.platform.theme.kit.input.TextField
import com.jeanbarrossilva.orca.platform.ui.core.requestFocusWithDelay

/** Tag that identifies the username field for testing purposes. **/
const val HTTP_AUTHORIZATION_USERNAME_FIELD_TAG = "auth-username-field"

/** Tag that identifies the instance [Domain] field for testing purposes. **/
const val HTTP_AUTHORIZATION_INSTANCE_FIELD_TAG = "auth-instance-field"

/** Tag that identifies the sign-in button for testing purposes. **/
const val HTTP_AUTHORIZATION_SIGN_IN_BUTTON_TAG = "auth-sign-in-button"

/**
 * Screen that presents username and instance fields for the user to fill in order for them to be
 * authenticated.
 *
 * @param viewModel [HttpAuthorizationViewModel] from which the username and the [String] version
 * of the instance [Domain] will be obtained and to which updates regarding those will be sent.
 * @param modifier [Modifier] to be applied to the underlying [Box].
 **/
@Composable
internal fun HttpAuthorization(
    viewModel: HttpAuthorizationViewModel,
    modifier: Modifier = Modifier
) {
    val username by viewModel.usernameFlow.collectAsState()
    val domain by viewModel.instanceFlow.collectAsState()

    HttpAuthorization(
        username,
        onUsernameChange = viewModel::setUsername,
        domain,
        onDomainChange = viewModel::setInstance,
        onSignIn = viewModel::authorize,
        modifier
    )
}

/**
 * Screen that presents username and instance fields for the user to fill in order for them to be
 * authenticated.
 *
 * @param modifier [Modifier] to be applied to the underlying [Box].
 * @param initialUsername Username to be input to its respective [TextField].
 * @param initialDomain Domain [String] to be input to its respective [TextField].
 **/
@Composable
internal fun HttpAuthorization(
    modifier: Modifier = Modifier,
    initialUsername: String = "",
    initialDomain: String = ""
) {
    var username by remember(initialUsername) { mutableStateOf(initialUsername) }
    var domain by remember(initialDomain) { mutableStateOf(initialDomain) }

    HttpAuthorization(
        username,
        onUsernameChange = { username = it },
        initialDomain,
        onDomainChange = { domain = it },
        onSignIn = { },
        modifier
    )
}

/**
 * Screen that presents username and instance fields for the user to fill in order for them to be
 * authenticated.
 *
 * @param username Username to be input to its respective [TextField].
 * @param onUsernameChange Callback run whenever the user inputs to the username [TextField].
 * @param domain Domain [String] to be input to its respective [TextField].
 * @param onDomainChange Callback run whenever the user inputs to the [Domain][Domain] [TextField].
 * @param onSignIn Callback run whenever the sign-in [PrimaryButton] is clicked.
 * @param modifier [Modifier] to be applied to the underlying [Box].
 **/
@Composable
private fun HttpAuthorization(
    username: String,
    onUsernameChange: (username: String) -> Unit,
    domain: String,
    onDomainChange: (domain: String) -> Unit,
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
            .background(OrcaTheme.colors.background.container)
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
                            .testTag(HTTP_AUTHORIZATION_USERNAME_FIELD_TAG),
                        KeyboardOptions(imeAction = ImeAction.Next),
                        isSingleLined = true
                    ) {
                        Text("Username")
                    }

                    Text("@")

                    TextField(
                        domain,
                        onDomainChange,
                        Modifier
                            .fillMaxWidth()
                            .testTag(HTTP_AUTHORIZATION_INSTANCE_FIELD_TAG),
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
                .testTag(HTTP_AUTHORIZATION_SIGN_IN_BUTTON_TAG),
            isEnabled = Account.isUsernameValid(username) && Domain.isValid(domain)
        ) {
            Text("Sign in")
        }
    }
}

/** Preview of [HttpAuthorization] with invalid data. **/
@Composable
@MultiThemePreview
private fun InvalidHttpAuthorizationPreview() {
    OrcaTheme {
        HttpAuthorization(initialUsername = "", initialDomain = "")
    }
}

/** Preview of [HttpAuthorization] with valid data. **/
@Composable
@MultiThemePreview
private fun ValidHttpAuthorizationPreview() {
    OrcaTheme {
        HttpAuthorization(
            initialUsername = Account.sample.username,
            initialDomain = "${Account.sample.domain}"
        )
    }
}
