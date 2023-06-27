package com.jeanbarrossilva.mastodonte.feature.tootdetails.ui.header

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.rounded.Comment
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetails
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.ui.FavoriteIcon
import com.jeanbarrossilva.mastodonte.platform.ui.SmallAvatar

@Composable
internal fun Header(modifier: Modifier = Modifier) {
    Header(
        avatar = { SmallAvatar() },
        name = { SmallTextualPlaceholder() },
        username = { MediumTextualPlaceholder() },
        body = {
            Column(
                verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.extraSmall)
            ) {
                repeat(3) { LargeTextualPlaceholder() }
                MediumTextualPlaceholder()
            }
        },
        metadata = { SmallTextualPlaceholder() },
        stats = { },
        modifier
    )
}

@Composable
internal fun Header(details: TootDetails, onShare: () -> Unit, modifier: Modifier = Modifier) {
    Header(
        avatar = { SmallAvatar(details.name, details.avatarURL) },
        name = { Text(details.name) },
        username = { Text(details.formattedUsername) },
        body = { Text(details.body) },
        metadata = { Text(details.formattedPublicationDateTime) },
        stats = {
            Divider()

            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Stat {
                    Icon(MastodonteTheme.Icons.Comment, contentDescription = "Comments")
                    Text(details.formattedCommentCount)
                }

                Stat {
                    FavoriteIcon(isActive = false, onToggle = { })
                    Text(details.formattedFavoriteCount)
                }

                Stat {
                    Icon(MastodonteTheme.Icons.Repeat, contentDescription = "Reblog")
                    Text(details.formattedReblogCount)
                }

                Stat {
                    Icon(
                        MastodonteTheme.Icons.Share,
                        contentDescription = "Share",
                        Modifier.clickable(onClick = onShare)
                    )
                }
            }

            Divider()
        },
        modifier
    )
}

@Composable
private fun Header(
    avatar: @Composable () -> Unit,
    name: @Composable () -> Unit,
    username: @Composable () -> Unit,
    body: @Composable () -> Unit,
    metadata: @Composable () -> Unit,
    stats: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = MastodonteTheme.spacings.large

    Column(modifier.padding(spacing), Arrangement.spacedBy(spacing)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            avatar()

            Column(
                verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.extraSmall)
            ) {
                ProvideTextStyle(MastodonteTheme.typography.bodyLarge, name)
                ProvideTextStyle(MastodonteTheme.typography.bodySmall, username)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.medium)) {
            body()
            ProvideTextStyle(MastodonteTheme.typography.bodySmall, metadata)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = stats
        )
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
private fun LoadedHeaderPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            Header(TootDetails.sample, onShare = { })
        }
    }
}
