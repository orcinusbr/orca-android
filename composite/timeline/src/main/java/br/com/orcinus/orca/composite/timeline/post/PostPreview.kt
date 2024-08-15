/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.composite.timeline.post

import androidx.annotation.VisibleForTesting
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.composite.timeline.R
import br.com.orcinus.orca.composite.timeline.avatar.SmallAvatar
import br.com.orcinus.orca.composite.timeline.post.figure.Figure
import br.com.orcinus.orca.composite.timeline.post.time.RelativeTimeProvider
import br.com.orcinus.orca.composite.timeline.post.time.rememberRelativeTimeProvider
import br.com.orcinus.orca.composite.timeline.stat.Stats
import br.com.orcinus.orca.composite.timeline.stat.details.StatsDetails
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.stat.Stat
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.IgnoringMutableInteractionSource
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import br.com.orcinus.orca.std.image.compose.SomeComposableImageLoader
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.flow.loadableFlow
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.test.Loading
import java.io.Serializable
import java.net.URI
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow

/** Tag that identifies a [PostPreview]'s name for testing purposes. */
internal const val PostPreviewNameTag = "post-preview-name"

/** Tag that identifies [PostPreview]'s metadata for testing purposes. */
internal const val PostPreviewMetadataTag = "post-preview-metadata"

/** Tag that identifies a [PostPreview]'s body for testing purposes. */
internal const val PostPreviewBodyTag = "post-preview-body"

/** Tag that identifies a [PostPreview]'s reblog metadata for testing purposes. */
internal const val PostPreviewRepostMetadataTag = "post-preview-repost-metadata"

/** Tag that identifies a [PostPreview] for testing purposes. */
const val PostPreviewTag = "post-preview"

/** [Modifier] to be applied to a [PostPreview]'s name. */
private val nameModifier = Modifier.testTag(PostPreviewNameTag)

/** [Modifier] to be applied to [PostPreview]'s metadata. */
private val metadataModifier = Modifier.testTag(PostPreviewMetadataTag)

/** [Modifier] to be applied to a [PostPreview]'s body. */
private val bodyModifier = Modifier.testTag(PostPreviewBodyTag)

/**
 * Information to be displayed on a [Post]'s preview.
 *
 * @param id Unique identifier.
 * @param avatarLoader [ImageLoader] that loads the author's avatar.
 * @param name Name of the author.
 * @param account [Account] of the author.
 * @param rebloggerName Name of the [Author] that reblogged the [Post].
 * @param text Content written by the author.
 * @param figure [Figure] that can be interacted with.
 * @param publicationDateTime Zoned moment in time in which it was published.
 * @param stats [StatsDetails] of the [Post]'s [Stat]s.
 * @param uri [URI] that leads to the [Post].
 */
@Immutable
data class PostPreview
internal constructor(
  val id: String,
  val avatarLoader: SomeComposableImageLoader,
  val name: String,
  private val account: Account,
  val rebloggerName: String?,
  val text: AnnotatedString,
  val figure: Figure?,
  private val publicationDateTime: ZonedDateTime,
  val stats: StatsDetails,
  internal val uri: URI
) : Serializable {
  /**
   * Obtains a [Flow] that gets emitted information about the author and how much time it's been
   * since it was published.
   *
   * @param relativeTimeProvider [RelativeTimeProvider] for providing relative time of publication.
   */
  fun getMetadataLoadableFlow(relativeTimeProvider: RelativeTimeProvider): Flow<Loadable<String>> {
    return loadableFlow {
      val relativeTimeSincePublication = relativeTimeProvider.provide(publicationDateTime)
      load("${account.username} • $relativeTimeSincePublication")
    }
  }

  companion object {
    /**
     * Creates a sample [PostPreview].
     *
     * @param postProvider [SamplePostProvider] that provides a sample [Post] to be converted into a
     *   [PostPreview].
     */
    @Composable
    fun createSample(postProvider: SamplePostProvider): PostPreview {
      return createSample(postProvider, AutosTheme.colors)
    }

    /**
     * Creates a sample [PostPreview].
     *
     * @param postProvider [SamplePostProvider] that provides a sample [Post] to be converted into a
     *   [PostPreview].
     * @param colors [Colors] by which the resulting [PostPreview]'s [text][PostPreview.text] can be
     *   colored.
     */
    fun createSample(postProvider: SamplePostProvider, colors: Colors): PostPreview {
      return postProvider.provideOneCurrent().toPostPreview(colors)
    }

    /**
     * Creates sample [PostPreview]s.
     *
     * @param postProvider [SamplePostProvider] that provides a sample [Post] to be converted into a
     *   [PostPreview].
     */
    @Composable
    fun createSamples(postProvider: SamplePostProvider): List<PostPreview> {
      return createSamples(postProvider, AutosTheme.colors)
    }

    /**
     * Creates sample [PostPreview]s.
     *
     * @param postProvider [SamplePostProvider] that provides a sample [Post] to be converted into a
     *   [PostPreview].
     * @param colors [Colors] by which the resulting [PostPreview]s' [text][PostPreview.text]s can
     *   be colored.
     */
    fun createSamples(postProvider: SamplePostProvider, colors: Colors): List<PostPreview> {
      return postProvider.provideAllCurrent().map { it.toPostPreview(colors) }
    }
  }
}

/** Default values used by a [PostPreview]. */
object PostPreviewDefaults {
  /** Amount of [Dp] by which a [PostPreview] is spaced by default. */
  val spacing
    @Composable get() = AutosTheme.spacings.medium.dp
}

/**
 * Loading preview of a [Post].
 *
 * @param modifier [Modifier] to be applied to the underlying [Card].
 */
@Composable
fun PostPreview(modifier: Modifier = Modifier) {
  PostPreview(
    avatar = { SmallAvatar() },
    name = { SmallTextualPlaceholder(nameModifier) },
    metadata = { MediumTextualPlaceholder(metadataModifier) },
    content = {
      Column(
        bodyModifier.semantics { set(SemanticsProperties.Loading, true) },
        Arrangement.spacedBy(AutosTheme.spacings.extraSmall.dp)
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
 * Preview of a [Post].
 *
 * @param preview [PostPreview] that holds the overall data to be displayed.
 * @param modifier [Modifier] to be applied to the underlying [Card].
 * @param relativeTimeProvider [RelativeTimeProvider] that provides the time that's passed since the
 *   [Post] was published.
 */
@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
fun LoadedPostPreview(
  preview: PostPreview,
  modifier: Modifier = Modifier,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider()
) {
  PostPreview(
    preview,
    onFavorite = {},
    onRepost = {},
    onShare = {},
    onClick = {},
    modifier,
    relativeTimeProvider
  )
}

/**
 * Preview of a [Post].
 *
 * @param preview [PostPreview] that holds the overall data to be displayed.
 * @param onFavorite Callback run whenever the [Post] is requested to be favorited.
 * @param onRepost Callback run whenever the [Post] is requested to be reblogged.
 * @param onShare Callback run whenever the [Post] is requested to be externally shared.
 * @param onClick Callback run whenever it's clicked.
 * @param modifier [Modifier] to be applied to the underlying [Card].
 * @param relativeTimeProvider [RelativeTimeProvider] that provides the time that's passed since the
 *   [Post] was published.
 */
@Composable
fun PostPreview(
  preview: PostPreview,
  onFavorite: () -> Unit,
  onRepost: () -> Unit,
  onShare: () -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider()
) {
  val metadataLoadable by
    remember(preview, relativeTimeProvider) {
        preview.getMetadataLoadableFlow(relativeTimeProvider)
      }
      .collectAsState(initial = Loadable.Loading())

  PostPreview(
    avatar = { SmallAvatar(preview.avatarLoader, preview.name) },
    name = { Text(preview.name, nameModifier) },
    metadata = {
      SmallTextualPlaceholder(metadataLoadable) { Text(it, metadataModifier) }

      preview.rebloggerName?.let {
        Row(
          Modifier.testTag(PostPreviewRepostMetadataTag),
          Arrangement.spacedBy(AutosTheme.spacings.small.dp),
          Alignment.CenterVertically
        ) {
          Icon(
            AutosTheme.iconography.repost.asImageVector,
            contentDescription = stringResource(R.string.composite_timeline_stat_repost),
            Modifier.size(14.dp)
          )

          Text(stringResource(R.string.composite_timeline_post_preview_reposted, it))
        }
      }
    },
    content = {
      Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.medium.dp)) {
        Text(preview.text, bodyModifier)
        preview.figure?.Content()
      }
    },
    stats = {
      Stats(preview.stats, onComment = {}, onFavorite, onRepost, onShare, Modifier.fillMaxWidth())
    },
    onClick,
    modifier.semantics { postPreview = preview }
  )
}

/**
 * Skeleton of the preview of a [Post].
 *
 * @param avatar Slot in which the avatar of the [Author] should be placed.
 * @param name Slot in which the name of the [Author] should be placed.
 * @param metadata Slot in which the username and the time that's passed since the [Post] was
 *   published should be placed.
 * @param content Actual content written by the [Author].
 * @param stats [Stat]s for data and actions such as comments, favorites, reblogs and external
 *   sharing.
 * @param onClick Callback run whenever it's clicked.
 * @param modifier [Modifier] to be applied to the underlying [Card].
 */
@Composable
private fun PostPreview(
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
  val metadataTextStyle = AutosTheme.typography.bodySmall

  Card(
    onClick ?: {},
    modifier.testTag(PostPreviewTag),
    shape = RectangleShape,
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    interactionSource = interactionSource
  ) {
    Column(
      Modifier.padding(PostPreviewDefaults.spacing),
      Arrangement.spacedBy(PostPreviewDefaults.spacing)
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(PostPreviewDefaults.spacing)) {
        avatar()

        Column(verticalArrangement = Arrangement.spacedBy(PostPreviewDefaults.spacing)) {
          Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.extraSmall.dp)) {
            ProvideTextStyle(AutosTheme.typography.bodyLarge, name)

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

/** Preview of a loading [PostPreview]. */
@Composable
@MultiThemePreview
private fun LoadingPostPreviewPreview() {
  AutosTheme { Surface(color = AutosTheme.colors.background.container.asColor) { PostPreview() } }
}

/** Preview of a loaded [PostPreview] with disabled [Stat]s. */
@Composable
@MultiThemePreview
private fun LoadedPostPreviewWithDisabledStatsPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      val postProvider = remember {
        SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
          .withDefaultProfiles()
          .withDefaultPosts()
          .build()
          .postProvider
      }

      LoadedPostPreview(
        preview =
          PostPreview.createSample(postProvider)
            .copy(
              stats =
                StatsDetails.createSample(postProvider).copy(isFavorite = false, isReposted = false)
            )
      )
    }
  }
}

/** Preview of a loaded [PostPreview] with enabled [Stat]s. */
@Composable
@MultiThemePreview
private fun LoadedPostPreviewWithEnabledStatsPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      val postProvider = remember {
        SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
          .withDefaultProfiles()
          .withDefaultPosts()
          .build()
          .postProvider
      }

      LoadedPostPreview(
        preview =
          PostPreview.createSample(postProvider)
            .copy(
              stats =
                StatsDetails.createSample(postProvider).copy(isFavorite = true, isReposted = true)
            )
      )
    }
  }
}
