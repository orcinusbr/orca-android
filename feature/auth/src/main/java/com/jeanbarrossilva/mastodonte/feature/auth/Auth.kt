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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isSpecified
import com.jeanbarrossilva.mastodonte.core.sample.toot.sample
import com.jeanbarrossilva.mastodonte.core.toot.Account
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.ui.component.action.PrimaryButton
import com.jeanbarrossilva.mastodonte.platform.ui.component.input.TextField

@Composable
private fun Auth(
    username: String,
    onUsernameChange: (username: String) -> Unit,
    instance: String,
    onInstanceChange: (instance: String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val spacing = MastodonteTheme.spacings.extraLarge
    var nextButtonHeight by remember { mutableStateOf(Dp.Unspecified) }
    val contentPadding = remember(nextButtonHeight) {
        if (nextButtonHeight.isSpecified) {
            PaddingValues(bottom = nextButtonHeight)
        } else {
            PaddingValues()
        }
    }

    Box(
        modifier
            .background(MastodonteTheme.colorScheme.background)
            .imePadding()
            .padding(PaddingValues(spacing))
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = contentPadding
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
                        onTextChange = {
                            if (Account.isUsernameValid(it)) {
                                onUsernameChange(it)
                            }
                        },
                        Modifier.fillMaxWidth(.45f),
                        isSingleLined = true
                    ) {
                        Text("Username")
                    }

                    Text("@")

                    TextField(
                        instance,
                        onTextChange = {
                            if (Account.isInstanceValid(it)) {
                                onInstanceChange(it)
                            }
                        },
                        Modifier.fillMaxWidth(),
                        isSingleLined = true
                    ) {
                        Text("Instance")
                    }
                }
            }
        }

        PrimaryButton(
            onClick = onNext,
            Modifier
                .align(Alignment.BottomStart)
                .onPlaced {
                    nextButtonHeight = with(density) {
                        it.size.height.toDp()
                    }
                }
                .fillMaxWidth()
        ) {
            Text("Next")
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
            onNext = { }
        )
    }
}
