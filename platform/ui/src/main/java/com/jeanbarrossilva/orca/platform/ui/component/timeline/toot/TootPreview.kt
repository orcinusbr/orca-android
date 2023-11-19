package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import android.content.Context
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.test.Loading
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.createSamples
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.theme.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.theme.extensions.EmptyMutableInteractionSource
import com.jeanbarrossilva.orca.platform.theme.extensions.IgnoringMutableInteractionSource
import com.jeanbarrossilva.orca.platform.ui.AccountFormatter
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.avatar.SmallAvatar
import com.jeanbarrossilva.orca.platform.ui.component.avatar.createSample
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.headline.HeadlineCard
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.stat.FavoriteStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.stat.ReblogStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.time.RelativeTimeProvider
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.time.rememberRelativeTimeProvider
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import java.io.Serializable
import java.net.URL
import java.time.ZonedDateTime

/** Tag that identifies a [SampleTootPreview]'s name for testing purposes. */
internal const val TOOT_PREVIEW_NAME_TAG = "toot-preview-name"

/** Tag that identifies [SampleTootPreview]'s metadata for testing purposes. */
internal const val TOOT_PREVIEW_METADATA_TAG = "toot-preview-metadata"

/** Tag that identifies a [SampleTootPreview]'s body for testing purposes. */
internal const val TOOT_PREVIEW_BODY_TAG = "toot-preview-body"

/** Tag that identifies a [SampleTootPreview]'s comment count stat for testing purposes. */
internal const val TOOT_PREVIEW_COMMENT_COUNT_STAT_TAG = "toot-preview-comments-stat"

/** Tag that identifies a [SampleTootPreview]'s reblog count stat for testing purposes. */
internal const val TOOT_PREVIEW_REBLOG_COUNT_STAT_TAG = "toot-preview-reblogs-stat"

/** Tag that identifies a [SampleTootPreview]'s reblog metadata for testing purposes. */
internal const val TOOT_PREVIEW_REBLOG_METADATA_TAG = "toot-preview-reblog-metadata"

/** Tag that identifies a [SampleTootPreview]'s share action for testing purposes. */
internal const val TOOT_PREVIEW_SHARE_ACTION_TAG = "toot-preview-share-action"

/** Tag that identifies a [SampleTootPreview] for testing purposes. */
const val TOOT_PREVIEW_TAG = "toot-preview"

/** [Modifier] to be applied to a [SampleTootPreview]'s name. */
private val nameModifier = Modifier.testTag(TOOT_PREVIEW_NAME_TAG)

/** [Modifier] to be applied to [SampleTootPreview]'s metadata. */
private val metadataModifier = Modifier.testTag(TOOT_PREVIEW_METADATA_TAG)

/** [Modifier] to be applied to a [SampleTootPreview]'s body. */
private val bodyModifier = Modifier.testTag(TOOT_PREVIEW_BODY_TAG)

/**
 * Information to be displayed on a [Toot]'s preview.
 *
 * @param id Unique identifier.
 * @param avatarLoader [ImageLoader] that loads the author's avatar.
 * @param name Name of the author.
 * @param account [Account] of the author.
 * @param rebloggerName Name of the [Author] that reblogged the [Toot].
 * @param text Content written by the author.
 * @param highlight [Highlight] from the [text].
 * @param publicationDateTime Zoned moment in time in which it was published.
 * @param commentCount Amount of comments.
 * @param isFavorite Whether it's marked as favorite.
 * @param favoriteCount Amount of times it's been marked as favorite.
 * @param isReblogged Whether it's reblogged.
 * @param reblogCount Amount of times it's been reblogged.
 * @param url [URL] that leads to the [Toot].
 */
@Immutable
data class TootPreview(
  val id: String,
  val avatarLoader: SomeImageLoader,
  val name: String,
  private val account: Account,
  val rebloggerName: String?,
  val text: AnnotatedString,
  val highlight: Highlight?,
  private val publicationDateTime: ZonedDateTime,
  private val commentCount: Int,
  val isFavorite: Boolean,
  private val favoriteCount: Int,
  val isReblogged: Boolean,
  private val reblogCount: Int,
  internal val url: URL
) : Serializable {
  /** Formatted, displayable version of [commentCount]. */
  val formattedCommentCount = commentCount.formatted

  /** Formatted, displayable version of [favoriteCount]. */
  val formattedFavoriteCount = favoriteCount.formatted

  /** Formatted, displayable version of [reblogCount]. */
  val formattedReblogCount = reblogCount.formatted

  /**
   * Gets information about the author and how much time it's been since it was published.
   *
   * @param relativeTimeProvider [RelativeTimeProvider] for providing relative time of publication.
   */
  fun getMetadata(relativeTimeProvider: RelativeTimeProvider): String {
    val username = AccountFormatter.username(account)
    val timeSincePublication = relativeTimeProvider.provide(publicationDateTime)
    return "$username • $timeSincePublication"
  }

  companion object {
    /** [SampleTootPreview] sample. */
    val sample
      @Composable get() = getSample(LocalContext.current, OrcaTheme.colors)

    /** [SampleTootPreview] samples. */
    val samples
      @Composable
      get() = Toot.createSamples(ImageLoader.Provider.createSample()).map { it.toTootPreview() }

    /**
     * Gets a sample [SampleTootPreview].
     *
     * @param context [Context] through which a sample [ImageLoader.Provider] will be created.
     * @param colors [Colors] by which the resulting [TootPreview]'s [text][TootPreview.text] can be
     *   colored.
     */
    fun getSample(context: Context, colors: Colors): TootPreview {
      return Toot.createSample(ImageLoader.Provider.createSample(context)).toTootPreview(colors)
    }
  }
}

/**
 * Loading preview of a [Toot].
 *
 * @param modifier [Modifier] to be applied to the underlying [Card].
 */
@Composable
fun TootPreview(modifier: Modifier = Modifier) {
  TootPreview(
    avatar = { SmallAvatar() },
    name = { SmallTextualPlaceholder(nameModifier) },
    metadata = { MediumTextualPlaceholder(metadataModifier) },
    content = {
      Column(
        bodyModifier.semantics { set(SemanticsProperties.Loading, true) },
        Arrangement.spacedBy(OrcaTheme.spacings.extraSmall.dp)
      ) {
        repeat(3) { LargeTextualPlaceholder() }
        MediumTextualPlaceholder()
      }
    },
    stats = {},
    onClick = null,
    modifier
  )
}

/**
 * Preview of a [Toot].
 *
 * @param preview [SampleTootPreview] that holds the overall data to be displayed.
 * @param onHighlightClick Callback run whenever the [HeadlineCard] (if displayed) is clicked.
 * @param onFavorite Callback run whenever the [Toot] is requested to be favorited.
 * @param onReblog Callback run whenever the [Toot] is requested to be reblogged.
 * @param onShare Callback run whenever the [Toot] is requested to be externally shared.
 * @param onClick Callback run whenever it's clicked.
 * @param modifier [Modifier] to be applied to the underlying [Card].
 * @param relativeTimeProvider [RelativeTimeProvider] that provides the time that's passed since the
 *   [Toot] was published.
 */
@Composable
fun TootPreview(
  preview: TootPreview,
  onHighlightClick: () -> Unit,
  onFavorite: () -> Unit,
  onReblog: () -> Unit,
  onShare: () -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider()
) {
  val metadata =
    remember(preview, relativeTimeProvider) { preview.getMetadata(relativeTimeProvider) }

  TootPreview(
    avatar = { SmallAvatar(preview.avatarLoader, preview.name) },
    name = { Text(preview.name, nameModifier) },
    metadata = {
      Text(metadata, metadataModifier)

      preview.rebloggerName?.let {
        Row(
          Modifier.testTag(TOOT_PREVIEW_REBLOG_METADATA_TAG),
          Arrangement.spacedBy(OrcaTheme.spacings.small.dp),
          Alignment.CenterVertically
        ) {
          Icon(
            OrcaTheme.iconography.repost.asImageVector,
            contentDescription = stringResource(R.string.platform_ui_reblog_stat),
            Modifier.size(14.dp)
          )

          Text(stringResource(R.string.platform_ui_toot_preview_reblogged, it))
        }
      }
    },
    content = {
      Column(verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.medium.dp)) {
        Text(preview.text, bodyModifier)

        preview.highlight?.headline?.let { HeadlineCard(it, onHighlightClick) }
      }
    },
    stats = {
      Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        Stat(
          StatPosition.LEADING,
          OrcaTheme.iconography.comment.outlined.asImageVector,
          contentDescription = stringResource(R.string.platform_ui_toot_preview_comments),
          onClick = {},
          Modifier.testTag(TOOT_PREVIEW_COMMENT_COUNT_STAT_TAG)
        ) {
          Text(preview.formattedCommentCount)
        }

        FavoriteStat(StatPosition.SUBSEQUENT, preview, onClick = onFavorite)
        ReblogStat(StatPosition.SUBSEQUENT, preview, onClick = onReblog)

        Stat(
          StatPosition.TRAILING,
          OrcaTheme.iconography.share.outlined.asImageVector,
          contentDescription = stringResource(R.string.platform_ui_toot_preview_share),
          onClick = onShare,
          Modifier.testTag(TOOT_PREVIEW_SHARE_ACTION_TAG)
        )
      }
    },
    onClick,
    modifier
  )
}

/**
 * Preview of a [Toot].
 *
 * @param modifier [Modifier] to be applied to the underlying [Card].
 * @param preview [TootPreview] that holds the overall data to be displayed.
 * @param relativeTimeProvider [RelativeTimeProvider] that provides the time that's passed since the
 *   [Toot] was published.
 */
@Composable
internal fun SampleTootPreview(
  modifier: Modifier = Modifier,
  preview: TootPreview = TootPreview.sample,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider()
) {
  TootPreview(
    preview,
    onHighlightClick = {},
    onFavorite = {},
    onReblog = {},
    onShare = {},
    onClick = {},
    modifier,
    relativeTimeProvider
  )
}

/**
 * Skeleton of the preview of a [Toot].
 *
 * @param avatar Slot in which the avatar of the [Author] should be placed.
 * @param name Slot in which the name of the [Author] should be placed.
 * @param metadata Slot in which the username and the time that's passed since the [Toot] was
 *   published should be placed.
 * @param content Actual content written by the [Author].
 * @param stats [Stat]s for data and actions such as comments, favorites, reblogs and external
 *   sharing.
 * @param onClick Callback run whenever it's clicked.
 * @param modifier [Modifier] to be applied to the underlying [Card].
 */
@Composable
private fun TootPreview(
  avatar: @Composable () -> Unit,
  name: @Composable () -> Unit,
  metadata: @Composable ColumnScope.() -> Unit,
  content: @Composable () -> Unit,
  stats: @Composable () -> Unit,
  onClick: (() -> Unit)?,
  modifier: Modifier = Modifier
) {
  val interactionSource =
    remember(onClick) {
      onClick?.let { IgnoringMutableInteractionSource(HoverInteraction::class) }
        ?: EmptyMutableInteractionSource()
    }
  val spacing = OrcaTheme.spacings.medium.dp
  val metadataTextStyle = OrcaTheme.typography.bodySmall

  @OptIn(ExperimentalMaterial3Api::class)
  Card(
    onClick ?: {},
    modifier.testTag(TOOT_PREVIEW_TAG),
    shape = RectangleShape,
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    interactionSource = interactionSource
  ) {
    Column(Modifier.padding(spacing), Arrangement.spacedBy(spacing)) {
      Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
        avatar()

        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
          Column(verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.extraSmall.dp)) {
            ProvideTextStyle(OrcaTheme.typography.bodyLarge, name)

            CompositionLocalProvider(
              LocalContentColor provides metadataTextStyle.color,
              LocalTextStyle provides metadataTextStyle
            ) {
              metadata()
            }
          }

          content()
          stats()
        }
      }
    }
  }
}

/** Preview of a loading [SampleTootPreview]. */
@Composable
@MultiThemePreview
private fun LoadingTootPreviewPreview() {
  OrcaTheme { Surface(color = OrcaTheme.colors.background.container.asColor) { TootPreview() } }
}

/** Preview of a loaded [SampleTootPreview] with disabled [Stat]s. */
@Composable
@MultiThemePreview
private fun LoadedTootPreviewWithDisabledStatsPreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container.asColor) {
      SampleTootPreview(preview = TootPreview.sample.copy(isFavorite = false, isReblogged = false))
    }
  }
}

/** Preview of a loaded [SampleTootPreview] with enabled [Stat]s. */
@Composable
@MultiThemePreview
private fun LoadedTootPreviewWithEnabledStatsPreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container.asColor) {
      SampleTootPreview(preview = TootPreview.sample.copy(isFavorite = true, isReblogged = true))
    }
  }
}
