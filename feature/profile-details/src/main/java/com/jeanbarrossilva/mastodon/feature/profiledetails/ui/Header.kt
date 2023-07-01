package com.jeanbarrossilva.mastodon.feature.profiledetails.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.ui.component.avatar.LargeAvatar
import com.jeanbarrossilva.mastodonte.platform.ui.html.HtmlAnnotatedString

@Composable
internal fun Header(modifier: Modifier = Modifier) {
    Header(
        avatar = { LargeAvatar() },
        name = { MediumTextualPlaceholder() },
        account = { LargeTextualPlaceholder() },
        bio = {
            Column(
                verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.extraSmall),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(3) { LargeTextualPlaceholder() }
                MediumTextualPlaceholder()
            }
        },
        mainActionButton = { },
        modifier
    )
}

@Composable
internal fun Header(details: ProfileDetails, modifier: Modifier = Modifier) {
    Header(
        avatar = { LargeAvatar(details.name, details.avatarURL) },
        name = { Text(details.name) },
        account = { Text(details.formattedAccount) },
        bio = { Text(HtmlAnnotatedString(details.bio)) },
        mainActionButton = { details.MainActionButton() },
        modifier
    )
}

@Composable
private fun Header(
    avatar: @Composable () -> Unit,
    name: @Composable () -> Unit,
    account: @Composable () -> Unit,
    bio: @Composable () -> Unit,
    mainActionButton: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .padding(MastodonteTheme.spacings.extraLarge)
            .fillMaxWidth(),
        Arrangement.spacedBy(MastodonteTheme.spacings.extraLarge),
        Alignment.CenterHorizontally
    ) {
        avatar()

        Column(
            verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.extraSmall),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProvideTextStyle(MastodonteTheme.typography.headlineLarge, name)
            ProvideTextStyle(MastodonteTheme.typography.titleSmall, account)
        }

        ProvideTextStyle(LocalTextStyle.current.copy(textAlign = TextAlign.Center), bio)
        mainActionButton()
    }
}

@Composable
@Preview
private fun LoadingHeaderPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            Header()
        }
    }
}

@Composable
@Preview
private fun HeaderPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            Header(ProfileDetails.sample)
        }
    }
}
