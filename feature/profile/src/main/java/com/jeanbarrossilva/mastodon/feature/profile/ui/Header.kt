package com.jeanbarrossilva.mastodon.feature.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.Placeholder
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.placeholder
import com.jeanbarrossilva.mastodonte.platform.ui.user.LargeAvatar

@Composable
internal fun Header(modifier: Modifier = Modifier) {
    Column(
        modifier
            .padding(MastodonteTheme.spacings.extraLarge)
            .fillMaxWidth(),
        Arrangement.spacedBy(MastodonteTheme.spacings.extraLarge),
        Alignment.CenterHorizontally
    ) {
        LargeAvatar()

        Column(
            verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .placeholder(
                        Placeholder.Text { MastodonteTheme.typography.titleLarge },
                        isVisible = true
                    )
                    .width(256.dp)
            )

            Box(
                Modifier
                    .placeholder(
                        Placeholder.Text { MastodonteTheme.typography.titleSmall },
                        MastodonteTheme.shapes.small,
                        isVisible = true
                    )
                    .width(128.dp)
            )
        }
    }
}

@Composable
@Preview
private fun HeaderPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            Header()
        }
    }
}
