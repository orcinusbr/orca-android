package com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.rounded.Comment
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.test.Loading
import com.jeanbarrossilva.mastodonte.core.sample.toot.sample
import com.jeanbarrossilva.mastodonte.core.toot.Account
import com.jeanbarrossilva.mastodonte.core.toot.Toot
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.EmptyMutableInteractionSource
import com.jeanbarrossilva.mastodonte.platform.ui.AccountFormatter
import com.jeanbarrossilva.mastodonte.platform.ui.component.avatar.SmallAvatar
import com.jeanbarrossilva.mastodonte.platform.ui.component.avatar.provider.AvatarImageProvider
import com.jeanbarrossilva.mastodonte.platform.ui.component.avatar.provider.rememberAvatarImageProvider
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.time.RelativeTimeProvider
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.time.rememberRelativeTimeProvider
import com.jeanbarrossilva.mastodonte.platform.ui.html.HtmlAnnotatedString
import java.net.URL
import java.time.ZonedDateTime

/** Tag that identifies a [TootPreview]'s name for testing purposes. **/
internal const val TOOT_PREVIEW_NAME_TAG = "toot-preview-name"

/** Tag that identifies [TootPreview]'s metadata for testing purposes. **/
internal const val TOOT_PREVIEW_METADATA_TAG = "toot-preview-metadata"

/** Tag that identifies a [TootPreview]'s body for testing purposes. **/
internal const val TOOT_PREVIEW_BODY_TAG = "toot-preview-body"

/** Tag that identifies a [TootPreview]'s comment count stat for testing purposes. **/
internal const val TOOT_PREVIEW_COMMENT_COUNT_STAT_TAG = "toot-preview-comments-stat"

/** Tag that identifies a [TootPreview]'s reblog count stat for testing purposes. **/
internal const val TOOT_PREVIEW_REBLOG_COUNT_STAT_TAG = "toot-preview-reblogs-stat"

/** Tag that identifies a [TootPreview]'s share action for testing purposes. **/
internal const val TOOT_PREVIEW_SHARE_ACTION_TAG = "toot-preview-share-action"

/** Tag that identifies a [TootPreview] for testing purposes. **/
const val TOOT_PREVIEW_TAG = "toot-preview"

/** [Modifier] to be applied to a [TootPreview]'s name. **/
private val nameModifier = Modifier.testTag(TOOT_PREVIEW_NAME_TAG)

/** [Modifier] to be applied to [TootPreview]'s metadata. **/
private val metadataModifier = Modifier.testTag(TOOT_PREVIEW_METADATA_TAG)

/** [Modifier] to be applied to a [TootPreview]'s body. **/
private val bodyModifier = Modifier.testTag(TOOT_PREVIEW_BODY_TAG)

/**
 * Information to be displayed on a [Toot]'s preview.
 *
 * @param avatarURL [URL] that leads to the author's avatar.
 * @param name Name of the author.
 * @param account [Account] of the author.
 * @param body Content written by the author.
 * @param publicationDateTime Zoned moment in time in which it was published.
 * @param commentCount Amount of comments.
 * @param isFavorite Whether it's marked as favorite.
 * @param favoriteCount Amount of times it's been marked as favorite.
 * @param isReblogged Whether it's reblogged.
 * @param reblogCount Amount of times it's been reblogged.
 **/
@Immutable
data class TootPreview(
    val avatarURL: URL,
    val name: String,
    private val account: Account,
    val body: AnnotatedString,
    private val publicationDateTime: ZonedDateTime,
    private val commentCount: Int,
    val isFavorite: Boolean,
    private val favoriteCount: Int,
    val isReblogged: Boolean,
    private val reblogCount: Int
) {
    /** Formatted, displayable version of [commentCount]. **/
    val formattedCommentCount = commentCount.formatted

    /** Formatted, displayable version of [favoriteCount]. **/
    val formattedFavoriteCount = favoriteCount.formatted

    /** Formatted, displayable version of [reblogCount]. **/
    val formattedReblogCount = reblogCount.formatted

    /**
     * Gets information about the author and how much time it's been since it was published.
     *
     * @param relativeTimeProvider [RelativeTimeProvider] for providing relative time of
     * publication.
     **/
    fun getMetadata(relativeTimeProvider: RelativeTimeProvider): String {
        val username = AccountFormatter.username(account)
        val timeSincePublication = relativeTimeProvider.provide(publicationDateTime)
        return "$username • $timeSincePublication"
    }

    companion object {
        /** [TootPreview] sample. **/
        val sample = TootPreview(
            Toot.sample.author.avatarURL,
            Toot.sample.author.name,
            Toot.sample.author.account,
            body = HtmlAnnotatedString(Toot.sample.content),
            Toot.sample.publicationDateTime,
            Toot.sample.commentCount,
            Toot.sample.isFavorite,
            Toot.sample.favoriteCount,
            Toot.sample.isReblogged,
            Toot.sample.reblogCount
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
        name = { SmallTextualPlaceholder(nameModifier) },
        metadata = { MediumTextualPlaceholder(metadataModifier) },
        body = {
            Column(
                bodyModifier.semantics { set(SemanticsProperties.Loading, true) },
                Arrangement.spacedBy(MastodonteTheme.spacings.extraSmall)
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
    modifier: Modifier = Modifier,
    avatarImageProvider: AvatarImageProvider = rememberAvatarImageProvider(),
    relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider()
) {
    val metadata = remember(preview, relativeTimeProvider) {
        preview.getMetadata(relativeTimeProvider)
    }

    TootPreview(
        avatar = {
            SmallAvatar(preview.name, preview.avatarURL, imageProvider = avatarImageProvider)
        },
        name = { Text(preview.name, nameModifier) },
        metadata = { Text(metadata, metadataModifier) },
        body = { Text(preview.body, bodyModifier) },
        stats = {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Stat(
                    MastodonteTheme.Icons.Comment,
                    contentDescription = "Comments",
                    onClick = { },
                    Modifier.testTag(TOOT_PREVIEW_COMMENT_COUNT_STAT_TAG)
                ) {
                    Text(preview.formattedCommentCount)
                }

                FavoriteStat(preview, onClick = onFavorite)
                ReblogStat(preview, onClick = onReblog)

                Stat(
                    MastodonteTheme.Icons.Share,
                    contentDescription = "Share",
                    onClick = onShare,
                    Modifier.testTag(TOOT_PREVIEW_SHARE_ACTION_TAG)
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
    val spacing = MastodonteTheme.spacings.large

    @OptIn(ExperimentalMaterial3Api::class)
    Card(
        onClick ?: { },
        modifier.testTag(TOOT_PREVIEW_TAG),
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
                        ProvideTextStyle(MastodonteTheme.typography.bodyLarge, name)
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
private fun LoadedInactiveTootPreviewPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            TootPreview(TootPreview.sample.copy(isFavorite = false, isReblogged = false))
        }
    }
}

@Composable
@Preview
private fun LoadedActiveTootPreviewPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            TootPreview(TootPreview.sample.copy(isFavorite = true, isReblogged = true))
        }
    }
}

@Composable
private fun TootPreview(preview: TootPreview, modifier: Modifier = Modifier) {
    TootPreview(preview, onFavorite = { }, onReblog = { }, onShare = { }, onClick = { }, modifier)
}
