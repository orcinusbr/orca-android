package com.jeanbarrossilva.mastodon.feature.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.ifLoaded
import com.jeanbarrossilva.loadable.map
import com.jeanbarrossilva.mastodonte.core.profile.AnyProfile
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.Placeholder
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.`if`
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.placeholder
import com.jeanbarrossilva.mastodonte.platform.ui.profile.Avatar
import com.jeanbarrossilva.mastodonte.platform.ui.profile.LargeAvatar
import com.jeanbarrossilva.mastodonte.platform.ui.profile.sample

@Composable
internal fun Header(profileLoadable: Loadable<AnyProfile>, modifier: Modifier = Modifier) {
    val avatarLoadable = remember(profileLoadable) {
        profileLoadable.map {
            Avatar(it.name, it.avatarURL)
        }
    }
    val titleStyle = MastodonteTheme.typography.headlineLarge
    val accountStyle = MastodonteTheme.typography.titleSmall
    val isLoading = remember(profileLoadable) { profileLoadable is Loadable.Loading }

    Column(
        modifier
            .padding(MastodonteTheme.spacings.extraLarge)
            .fillMaxWidth(),
        Arrangement.spacedBy(MastodonteTheme.spacings.extraLarge),
        Alignment.CenterHorizontally
    ) {
        LargeAvatar(avatarLoadable)

        Column(
            verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.extraSmall),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                profileLoadable.ifLoaded(AnyProfile::name).orEmpty(),
                Modifier
                    .placeholder(Placeholder.Text { titleStyle }, isVisible = isLoading)
                    .`if`({ isLoading }) { width(256.dp) },
                style = titleStyle
            )

            Text(
                profileLoadable.ifLoaded { "${account.username}@${account.instance}" }.orEmpty(),
                Modifier
                    .placeholder(
                        Placeholder.Text { accountStyle },
                        MastodonteTheme.shapes.small,
                        isVisible = isLoading
                    )
                    .`if`({ isLoading }) { width(128.dp) },
                style = accountStyle
            )
        }
    }
}

@Composable
@Preview
private fun HeaderPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            Header(Loadable.Loaded(Profile.sample))
        }
    }
}
