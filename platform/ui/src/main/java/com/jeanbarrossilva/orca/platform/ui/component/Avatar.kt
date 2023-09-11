package com.jeanbarrossilva.orca.platform.ui.component

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.placeholder.Placeholder
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.Samples
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
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
    Placeholder(
        modifier
            .requiredSize(smallSize)
            .testTag(AVATAR_TAG),
        shape = smallShape
    )
}

@Composable
fun SmallAvatar(
    name: String,
    url: URL,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = rememberImageLoader()
) {
    Image(
        url,
        contentDescriptionFor(name),
        modifier
            .requiredSize(smallSize)
            .testTag(AVATAR_TAG),
        imageLoader,
        smallShape
    )
}

@Composable
fun LargeAvatar(modifier: Modifier = Modifier) {
    Placeholder(
        modifier
            .requiredSize(largeSize)
            .testTag(AVATAR_TAG),
        shape = largeShape
    )
}

@Composable
fun LargeAvatar(
    name: String,
    url: URL,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = rememberImageLoader()
) {
    Image(
        url,
        contentDescriptionFor(name),
        modifier
            .requiredSize(largeSize)
            .testTag(AVATAR_TAG),
        imageLoader,
        largeShape
    )
}

private fun contentDescriptionFor(name: String): String {
    return "$name's avatar"
}

@Composable
@MultiThemePreview
private fun LoadingLargeAvatarPreview() {
    OrcaTheme {
        LargeAvatar()
    }
}

@Composable
@MultiThemePreview
private fun LoadedLargeAvatarPreview() {
    OrcaTheme {
        LargeAvatar(Avatar.sample.name, Avatar.sample.url)
    }
}

@Composable
@MultiThemePreview
private fun LoadingSmallAvatarPreview() {
    OrcaTheme {
        SmallAvatar()
    }
}

@Composable
@MultiThemePreview
private fun LoadedSmallAvatarPreview() {
    OrcaTheme {
        SmallAvatar(Avatar.sample.name, Avatar.sample.url)
    }
}
