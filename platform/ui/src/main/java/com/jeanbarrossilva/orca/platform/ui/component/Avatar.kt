package com.jeanbarrossilva.orca.platform.ui.component

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.placeholder.Placeholder
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.Samples
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.imageloader.compose.Image
import com.jeanbarrossilva.orca.std.imageloader.compose.rememberImageLoader
import java.io.Serializable
import java.net.URL

internal const val AVATAR_TAG = "avatar"

private val smallSize = 42.dp
private val largeSize = 128.dp

private val smallShape
  @Composable get() = OrcaTheme.shapes.small
private val largeShape
  @Composable get() = OrcaTheme.shapes.large

data class Avatar(val name: String, val url: URL) : Serializable {
  companion object {
    val sample = Avatar(Samples.NAME, Samples.avatarURL)
  }
}

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
  OrcaTheme { LargeAvatar() }
}

@Composable
@MultiThemePreview
private fun LoadedLargeAvatarPreview() {
  OrcaTheme { LargeAvatar(rememberImageLoader(Avatar.sample.url), Avatar.sample.name) }
}

@Composable
@MultiThemePreview
private fun LoadingSmallAvatarPreview() {
  OrcaTheme { SmallAvatar() }
}

@Composable
@MultiThemePreview
private fun LoadedSmallAvatarPreview() {
  OrcaTheme { SmallAvatar(rememberImageLoader(Avatar.sample.url), Avatar.sample.name) }
}
