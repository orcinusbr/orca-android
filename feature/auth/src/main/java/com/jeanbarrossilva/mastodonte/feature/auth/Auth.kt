package com.jeanbarrossilva.mastodonte.feature.auth

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.mastodonte.core.account.Account
import com.jeanbarrossilva.mastodonte.core.sample.account.sample
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.ui.component.action.PrimaryButton
import com.jeanbarrossilva.mastodonte.platform.ui.component.input.TextField

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
private fun Auth(
    username: String,
    onUsernameChange: (username: String) -> Unit,
    instance: String,
    onInstanceChange: (instance: String) -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = MastodonteTheme.spacings.extraLarge
    val usernameFocusRequester = remember(::FocusRequester)
    val nextButtonFocusRequester = remember(::FocusRequester)

    LaunchedEffect(Unit) {
        usernameFocusRequester.requestFocus()
    }

    Box(
        modifier
            .background(MastodonteTheme.colorScheme.background)
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
                    verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.small),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Welcome!",
                        textAlign = TextAlign.Center,
                        style = MastodonteTheme.typography.headlineLarge
                    )

                    Text(
                        "What is your account?",
                        textAlign = TextAlign.Center,
                        style = MastodonteTheme.typography.titleSmall
                    )
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement
                        .spacedBy(MastodonteTheme.spacings.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        username,
                        onUsernameChange,
                        Modifier
                            .focusRequester(usernameFocusRequester)
                            .fillMaxWidth(.45f),
                        KeyboardOptions(imeAction = ImeAction.Next),
                        isSingleLined = true
                    ) {
                        Text("Username")
                    }

                    Text("@")

                    TextField(
                        instance,
                        onInstanceChange,
                        Modifier.fillMaxWidth(),
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
                .fillMaxWidth(),
            isEnabled = Account.isUsernameValid(username) && Account.isInstanceValid(instance)
        ) {
            Text("Sign in")
        }
    }
}

@Composable
@Preview
private fun AuthPreview() {
    MastodonteTheme {
        Auth(
            Account.sample.username,
            onUsernameChange = { },
            Account.sample.instance,
            onInstanceChange = { },
            onSignIn = { }
        )
    }
}
