/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.composite.timeline.avatar

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.placeholder.Placeholder
import com.jeanbarrossilva.orca.composite.timeline.R
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.core.image.createSample
import com.jeanbarrossilva.orca.std.image.compose.ComposableImage
import com.jeanbarrossilva.orca.std.image.compose.ComposableImageLoader
import com.jeanbarrossilva.orca.std.image.compose.SomeComposableImageLoader

/** Tag that identifies an avatar for testing purposes. */
internal const val AVATAR_TAG = "avatar"

/** Default values used by an avatar. */
private object AvatarDefaults {
  /** [Shape] by which a [SmallAvatar] is clipped by default. */
  val smallShape
    @Composable get() = AutosTheme.forms.small.asShape

  /** Default size of a [SmallAvatar]. */
  val SmallSize = 42.dp

  /** [Shape] by which a [LargeAvatar] is clipped by default. */
  val largeShape
    @Composable get() = AutosTheme.forms.large.asShape

  /** Default size of a [LargeAvatar]. */
  val LargeSize = 128.dp

  /**
   * Creates a description for an avatar whose owner's name is [name].
   *
   * @param name Name of the owner to which the avatar belongs.
   */
  @Composable
  fun contentDescriptionFor(name: String): String {
    return stringResource(R.string.composite_timeline_avatar, name)
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
    modifier.requiredSize(AvatarDefaults.SmallSize).testTag(AVATAR_TAG),
    shape = AvatarDefaults.smallShape
  )
}

/**
 * [Profile] picture at a small size.
 *
 * @param imageLoader [ComposableImageLoader] by which the avatar will be loaded.
 * @param name Name of the owner to which avatar belongs.
 * @param modifier [Modifier] to be applied to the underlying [ComposableImage].
 */
@Composable
fun SmallAvatar(
  imageLoader: SomeComposableImageLoader,
  name: String,
  modifier: Modifier = Modifier
) {
  imageLoader.load()(
    AvatarDefaults.contentDescriptionFor(name),
    AvatarDefaults.smallShape,
    ComposableImageLoader.DefaultContentScale,
    modifier.requiredSize(AvatarDefaults.SmallSize).testTag(AVATAR_TAG)
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
    modifier.requiredSize(AvatarDefaults.LargeSize).testTag(AVATAR_TAG),
    shape = AvatarDefaults.largeShape
  )
}

/**
 * [Profile] picture at a large size.
 *
 * @param imageLoader [ComposableImageLoader] by which the avatar will be loaded.
 * @param name Name of the owner to which avatar belongs.
 * @param modifier [Modifier] to be applied to the underlying [ComposableImage].
 */
@Composable
fun LargeAvatar(
  imageLoader: SomeComposableImageLoader,
  name: String,
  modifier: Modifier = Modifier
) {
  imageLoader.load()(
    AvatarDefaults.contentDescriptionFor(name),
    AvatarDefaults.largeShape,
    ComposableImageLoader.DefaultContentScale,
    modifier.requiredSize(AvatarDefaults.LargeSize).testTag(AVATAR_TAG)
  )
}

/**
 * [Profile] picture of the default sample [Author] at a small size.
 *
 * @param modifier [Modifier] to be applied to the underlying [ComposableImage].
 * @see AuthorImageSource.Default
 */
@Composable
internal fun SampleSmallAvatar(modifier: Modifier = Modifier) {
  SmallAvatar(
    ComposableImageLoader.createSample(AuthorImageSource.Default),
    Author.sample.name,
    modifier
  )
}

/**
 * [Profile] picture of the default sample [Author] at a large size.
 *
 * @param modifier [Modifier] to be applied to the underlying [ComposableImage].
 * @see AuthorImageSource.Default
 */
@Composable
internal fun SampleLargeAvatar(modifier: Modifier = Modifier) {
  LargeAvatar(
    ComposableImageLoader.createSample(AuthorImageSource.Default),
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
