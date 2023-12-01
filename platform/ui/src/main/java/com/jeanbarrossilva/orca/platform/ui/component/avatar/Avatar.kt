/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
    smallShape
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
    largeShape
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
