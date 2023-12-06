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
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.core.createSample
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.imageloader.compose.Image

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
fun SmallAvatar(imageLoader: SomeImageLoader, name: String, modifier: Modifier = Modifier) {
  Image(
    imageLoader,
    contentDescriptionFor(name),
    modifier.requiredSize(smallSize).testTag(AVATAR_TAG),
    shape = smallShape
  )
}

@Composable
fun LargeAvatar(modifier: Modifier = Modifier) {
  Placeholder(modifier.requiredSize(largeSize).testTag(AVATAR_TAG), shape = largeShape)
}

@Composable
fun LargeAvatar(imageLoader: SomeImageLoader, name: String, modifier: Modifier = Modifier) {
  Image(
    imageLoader,
    contentDescriptionFor(name),
    modifier.requiredSize(largeSize).testTag(AVATAR_TAG),
    shape = largeShape
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
  AutosTheme { LargeAvatar(ImageLoader.forDefaultSampleAuthor(), Profile.createSample().name) }
}

@Composable
@MultiThemePreview
private fun LoadingSmallAvatarPreview() {
  AutosTheme { SmallAvatar() }
}

@Composable
@MultiThemePreview
private fun LoadedSmallAvatarPreview() {
  AutosTheme { SmallAvatar(ImageLoader.forDefaultSampleAuthor(), Profile.createSample().name) }
}
