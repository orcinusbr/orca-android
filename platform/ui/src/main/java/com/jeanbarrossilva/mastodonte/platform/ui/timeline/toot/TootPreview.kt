package com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.rounded.Comment
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.EmptyMutableInteractionSource
import com.jeanbarrossilva.mastodonte.platform.ui.Samples
import com.jeanbarrossilva.mastodonte.platform.ui.SmallAvatar
import com.jeanbarrossilva.mastodonte.platform.ui.html.HtmlAnnotatedString
import java.net.URL

private val nameTextStyle
    @Composable get() = MastodonteTheme.typography.bodyLarge
private val spacing
    @Composable get() = MastodonteTheme.spacings.large

/**
 * Information to be displayed on a [Toot]'s preview.
 *
 * @param avatarURL [URL] that leads to the author's avatar.
 * @param name Name of the author.
 * @param username Username of the author.
 * @param timeSincePublication How much time has passed since publication.
 * @param body Content written by the author.
 * @param commentCount Amount of comments.
 * @param favoriteCount Amount of times it's been marked as favorite.
 * @param reblogCount Amount of times it's been reblogged.
 **/
data class TootPreview(
    val avatarURL: URL,
    val name: String,
    val username: String,
    val timeSincePublication: String,
    val body: AnnotatedString,
    val commentCount: String,
    val favoriteCount: String,
    val reblogCount: String
) {
    companion object {
        /** [TootPreview] sample. **/
        val sample = TootPreview(
            Samples.avatarURL,
            Samples.NAME,
            username = "@jeanbarrossilva",
            timeSincePublication = "19 years ago",
            body = HtmlAnnotatedString("<p><b>Hello</b>, <i>world</i>!</p>"),
            commentCount = "1K",
            favoriteCount = "2K",
            reblogCount = "512"
        )
    }
}

fun LazyListScope.loadingTootPreviews() {
    items(128) {
        TootPreview()
    }
}

@Composable
fun TootPreview(modifier: Modifier = Modifier) {
    TootPreview(
        avatar = { SmallAvatar() },
        name = { SmallTextualPlaceholder() },
        metadata = { MediumTextualPlaceholder() },
        body = {
            Column(
                verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.extraSmall)
            ) {
                repeat(3) { LargeTextualPlaceholder() }
                MediumTextualPlaceholder()
            }
        },
        stats = { },
        onClick = null,
        modifier
    )
}

@Composable
fun TootPreview(
    preview: TootPreview,
    onFavorite: () -> Unit,
    onReblog: () -> Unit,
    onShare: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TootPreview(
        avatar = { SmallAvatar(preview.name, preview.avatarURL) },
        name = { Text(preview.name, style = nameTextStyle) },
        metadata = {
            Text("${preview.username} • ${preview.timeSincePublication}")
        },
        body = { Text(preview.body) },
        stats = {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Stat(
                    MastodonteTheme.Icons.Comment,
                    contentDescription = "Comments",
                    onClick = { }
                ) {
                    Text(preview.commentCount)
                }

                Stat(
                    MastodonteTheme.Icons.Favorite,
                    contentDescription = "Favorites",
                    onClick = onFavorite
                ) {
                    Text(preview.favoriteCount)
                }

                Stat(
                    MastodonteTheme.Icons.Repeat,
                    contentDescription = "Reblogs",
                    onClick = onReblog
                ) {
                    Text(preview.reblogCount)
                }

                Stat(
                    MastodonteTheme.Icons.Share,
                    contentDescription = "Share",
                    onClick = onShare
                )
            }
        },
        onClick,
        modifier
    )
}

@Composable
private fun TootPreview(
    avatar: @Composable () -> Unit,
    name: @Composable () -> Unit,
    metadata: @Composable () -> Unit,
    body: @Composable () -> Unit,
    stats: @Composable () -> Unit,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember(onClick) {
        onClick?.let { MutableInteractionSource() } ?: EmptyMutableInteractionSource()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    Card(
        onClick ?: { },
        modifier,
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        interactionSource = interactionSource
    ) {
        Column(
            Modifier.padding(spacing),
            Arrangement.spacedBy(spacing)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                avatar()

                Column(
                    verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.small)
                ) {
                    Column(
                        verticalArrangement = Arrangement
                            .spacedBy(MastodonteTheme.spacings.extraSmall)
                    ) {
                        name()
                        ProvideTextStyle(MastodonteTheme.typography.bodySmall, metadata)
                    }

                    body()
                    stats()
                }
            }
        }
    }
}

@Composable
@Preview
private fun LoadingTootPreviewPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            TootPreview()
        }
    }
}

@Composable
@Preview
private fun LoadedTootPreviewPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            TootPreview(
                TootPreview.sample,
                onFavorite = { },
                onReblog = { },
                onShare = { },
                onClick = { }
            )
        }
    }
}
