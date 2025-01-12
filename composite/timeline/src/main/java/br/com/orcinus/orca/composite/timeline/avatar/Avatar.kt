/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.composite.timeline.avatar

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.updateBounds
import br.com.orcinus.orca.autos.forms.Form
import br.com.orcinus.orca.autos.forms.Forms
import br.com.orcinus.orca.composite.timeline.R
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.sample.image.AuthorImageSource
import br.com.orcinus.orca.platform.autos.forms.asShape
import br.com.orcinus.orca.platform.autos.forms.clip
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.Units
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.core.image.createSample
import br.com.orcinus.orca.std.image.android.AndroidImageLoader
import br.com.orcinus.orca.std.image.android.asComposable
import com.jeanbarrossilva.loadable.placeholder.Placeholder

/**
 * Tag that identifies an avatar composable for testing purposes.
 *
 * @see SmallAvatar
 * @see LargeAvatar
 */
internal const val AvatarTag = "avatar"

/** Default values used by an avatar. */
private object AvatarDefaults {
  /**
   * Creates a description for an avatar whose owner's name is [name].
   *
   * @param name Name of the owner to which the avatar belongs.
   */
  @Composable
  @JvmStatic
  fun contentDescriptionFor(name: String): String {
    return stringResource(R.string.composite_timeline_avatar, name)
  }
}

/**
 * Properties and behaviors of the rendering of the picture of a [Profile] based on its size:
 * - [SMALL], for contexts in which it is not one of the most important parts of the UI (e. g.,
 *   composition of comments or standalone posts); or
 * - [LARGE], targeting ones that have such [Profile] or its owner be highlighted in terms of
 *   prominence (e. g., their own page).
 */
@Immutable
enum class Avatar {
  /**
   * Avatar size-based rendering characteristics for contexts in which it is not one of the most
   * prominent UI components.
   */
  SMALL {
    override val sizeThreshold = 42.dp
    override val form = Forms.default.medium as Form.PerCorner
  },

  /**
   * Avatar size-based rendering characteristics for contexts in which it is one of the most
   * prominent UI components.
   */
  LARGE {
    override val sizeThreshold = 128.dp
    override val form = Forms.default.large as Form.PerCorner
  };

  /**
   * [Form] that defines how the avatar is clipped.
   *
   * @see transform
   * @see shape
   */
  protected abstract val form: Form.PerCorner

  /** [Shape] of an avatar of this size. */
  internal inline val shape: Shape
    get() = form.asShape

  /**
   * Starting amount of density-dependent pixels that compose both the width and the height of the
   * avatar.
   */
  abstract val sizeThreshold: Dp

  /**
   * Resizes and clips the [drawable] based on this specific avatar size.
   *
   * @param context [Context] for converting density-independent pixels into absolute ones.
   * @param drawable [Drawable] to be transformed.
   */
  fun transform(context: Context, drawable: Drawable): Drawable {
    val sizeThresholdInPx = Units.dp(context, sizeThreshold.value)
    return drawable.clip(context, form).apply {
      updateBounds(right = bounds.left + sizeThresholdInPx, bottom = bounds.top + sizeThresholdInPx)
    }
  }
}

/**
 * Loading [Profile] picture at a small size.
 *
 * @param modifier [Modifier] to be applied to the underlying [Placeholder].
 */
@Composable
fun SmallAvatar(modifier: Modifier = Modifier) {
  Placeholder(
    modifier.requiredSize(Avatar.SMALL.sizeThreshold).testTag(AvatarTag),
    shape = Avatar.SMALL.shape
  )
}

/**
 * [Profile] picture at a small size.
 *
 * @param imageLoader [AndroidImageLoader] by which the avatar will be loaded.
 * @param name Name of the owner to which avatar belongs.
 * @param modifier [Modifier] to be applied to the image.
 */
@Composable
fun SmallAvatar(imageLoader: AndroidImageLoader<*>, name: String, modifier: Modifier = Modifier) {
  imageLoader
    .load()
    .asComposable(
      AvatarDefaults.contentDescriptionFor(name),
      Avatar.SMALL.shape,
      modifier.requiredSize(Avatar.SMALL.sizeThreshold).testTag(AvatarTag)
    )
}

/**
 * Loading [Profile] picture at a large size.
 *
 * @param modifier [Modifier] to be applied to the underlying [Placeholder].
 */
@Composable
fun LargeAvatar(modifier: Modifier = Modifier) {
  Placeholder(
    modifier.requiredSize(Avatar.LARGE.sizeThreshold).testTag(AvatarTag),
    shape = Avatar.LARGE.shape
  )
}

/**
 * [Profile] picture at a large size.
 *
 * @param imageLoader [AndroidImageLoader] by which the avatar will be loaded.
 * @param name Name of the owner to which avatar belongs.
 * @param modifier [Modifier] to be applied to the image.
 */
@Composable
fun LargeAvatar(imageLoader: AndroidImageLoader<*>, name: String, modifier: Modifier = Modifier) {
  imageLoader
    .load()
    .asComposable(
      AvatarDefaults.contentDescriptionFor(name),
      Avatar.LARGE.shape,
      modifier.requiredSize(Avatar.LARGE.sizeThreshold).testTag(AvatarTag)
    )
}

/**
 * [Profile] picture of the default sample [Author] at a small size.
 *
 * This avatar is intended for previewing and testing only.
 *
 * @param modifier [Modifier] to be applied to the image.
 * @see AuthorImageSource.Default
 */
@Composable
@VisibleForTesting
internal fun SampleSmallAvatar(modifier: Modifier = Modifier) {
  SmallAvatar(
    AndroidImageLoader.createSample(AuthorImageSource.Default),
    Author.sample.name,
    modifier
  )
}

/**
 * [Profile] picture of the default sample [Author] at a large size.
 *
 * This avatar is intended for previewing and testing only.
 *
 * @param modifier [Modifier] to be applied to the image.
 * @see AuthorImageSource.Default
 */
@Composable
@VisibleForTesting
internal fun SampleLargeAvatar(modifier: Modifier = Modifier) {
  LargeAvatar(
    AndroidImageLoader.createSample(AuthorImageSource.Default),
    Author.sample.name,
    modifier
  )
}

@Composable
@MultiThemePreview
private fun LoadingLargeAvatarPreview() {
  AutosTheme { LargeAvatar() }
}

/** Preview of a loaded [LargeAvatar]. */
@Composable
@MultiThemePreview
private fun LoadedLargeAvatarPreview() {
  AutosTheme { SampleLargeAvatar() }
}

/** Preview of a loading [SmallAvatar]. */
@Composable
@MultiThemePreview
private fun LoadingSmallAvatarPreview() {
  AutosTheme { SmallAvatar() }
}

/** Preview of a loaded [SmallAvatar]. */
@Composable
@MultiThemePreview
private fun LoadedSmallAvatarPreview() {
  AutosTheme { SampleSmallAvatar() }
}
