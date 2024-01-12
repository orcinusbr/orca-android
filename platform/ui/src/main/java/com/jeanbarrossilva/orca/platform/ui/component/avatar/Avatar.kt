/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.ui.component.avatar

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.placeholder.Placeholder
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.core.image.createSample
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.std.image.compose.ComposableImageLoader
import com.jeanbarrossilva.orca.std.image.compose.SomeComposableImageLoader

internal const val AVATAR_TAG = "avatar"

private val smallSize = 42.dp
private val largeSize = 128.dp

private val smallShape
  @Composable get() = AutosTheme.forms.small.asShape
private val largeShape
  @Composable get() = AutosTheme.forms.large.asShape

@Composable
fun SmallAvatar(modifier: Modifier = Modifier) {
  Placeholder(modifier.requiredSize(smallSize).testTag(AVATAR_TAG), shape = smallShape)
}

@Composable
fun SmallAvatar(
  imageLoader: SomeComposableImageLoader,
  name: String,
  modifier: Modifier = Modifier
) {
  imageLoader.load()(
    contentDescriptionFor(name),
    smallShape,
    ComposableImageLoader.DefaultContentScale,
    modifier.requiredSize(smallSize).testTag(AVATAR_TAG)
  )
}

@Composable
fun LargeAvatar(modifier: Modifier = Modifier) {
  Placeholder(modifier.requiredSize(largeSize).testTag(AVATAR_TAG), shape = largeShape)
}

@Composable
fun LargeAvatar(
  imageLoader: SomeComposableImageLoader,
  name: String,
  modifier: Modifier = Modifier
) {
  imageLoader.load()(
    contentDescriptionFor(name),
    largeShape,
    ComposableImageLoader.DefaultContentScale,
    modifier.requiredSize(largeSize).testTag(AVATAR_TAG)
  )
}

@Composable
internal fun SampleSmallAvatar(modifier: Modifier = Modifier) {
  SmallAvatar(
    ComposableImageLoader.createSample(AuthorImageSource.Default),
    Author.sample.name,
    modifier
  )
}

@Composable
internal fun SampleLargeAvatar(modifier: Modifier = Modifier) {
  LargeAvatar(
    ComposableImageLoader.createSample(AuthorImageSource.Default),
    Author.sample.name,
    modifier
  )
}

@Composable
private fun contentDescriptionFor(name: String): String {
  return stringResource(R.string.platform_ui_avatar, name)
}

@Composable
@MultiThemePreview
private fun LoadingLargeAvatarPreview() {
  AutosTheme { LargeAvatar() }
}

@Composable
@MultiThemePreview
private fun LoadedLargeAvatarPreview() {
  AutosTheme { SampleLargeAvatar() }
}

@Composable
@MultiThemePreview
private fun LoadingSmallAvatarPreview() {
  AutosTheme { SmallAvatar() }
}

@Composable
@MultiThemePreview
private fun LoadedSmallAvatarPreview() {
  AutosTheme { SampleSmallAvatar() }
}
