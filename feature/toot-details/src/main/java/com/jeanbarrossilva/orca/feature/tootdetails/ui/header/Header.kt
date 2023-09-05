package com.jeanbarrossilva.orca.feature.tootdetails.ui.header

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.rounded.Comment
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
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetails
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.SmallAvatar
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.orca.platform.ui.component.stat.favorite.FavoriteStatIcon
import com.jeanbarrossilva.orca.platform.ui.component.stat.reblog.ReblogStatIcon

@Composable
internal fun Header(modifier: Modifier = Modifier) {
    Header(
        avatar = { SmallAvatar() },
        name = { SmallTextualPlaceholder() },
        username = { MediumTextualPlaceholder() },
        body = {
            Column(
                verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.extraSmall)
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
internal fun Header(
    details: TootDetails,
    onFavorite: () -> Unit,
    onReblog: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    Icon(OrcaTheme.Icons.Comment, contentDescription = "Comments")
                    Text(details.formattedCommentCount)
                }

                Stat {
                    FavoriteStatIcon(
                        isActive = details.isFavorite,
                        ActivateableStatIconInteractiveness.Interactive { onFavorite() },
                        Modifier.size(24.dp)
                    )

                    Text(details.formattedFavoriteCount)
                }

                Stat {
                    ReblogStatIcon(
                        isActive = details.isReblogged,
                        ActivateableStatIconInteractiveness.Interactive { onReblog() },
                        Modifier.size(24.dp)
                    )

                    Text(details.formattedReblogCount)
                }

                Stat {
                    Icon(
                        OrcaTheme.Icons.Share,
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
    val spacing = OrcaTheme.spacings.large

    Column(modifier.padding(spacing), Arrangement.spacedBy(spacing)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            avatar()

            Column(
                verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.extraSmall)
            ) {
                ProvideTextStyle(OrcaTheme.typography.bodyLarge, name)
                ProvideTextStyle(OrcaTheme.typography.bodySmall, username)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.medium)) {
            body()
            ProvideTextStyle(OrcaTheme.typography.bodySmall, metadata)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = stats
        )
    }
}

@Composable
@Preview
private fun LoadingHeaderPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colors.background) {
            Header()
        }
    }
}

@Composable
@Preview
private fun LoadedHeaderPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colors.background) {
            Header(TootDetails.sample, onFavorite = { }, onReblog = { }, onShare = { })
        }
    }
}
