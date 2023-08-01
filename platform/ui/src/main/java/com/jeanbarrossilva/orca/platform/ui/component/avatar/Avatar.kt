package com.jeanbarrossilva.orca.platform.ui.component.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.rounded.BrokenImage
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.placeholder.Placeholder
import com.jeanbarrossilva.loadable.placeholder.PlaceholderDefaults
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.Samples
import com.jeanbarrossilva.orca.platform.ui.component.avatar.provider.AvatarImageProvider
import com.jeanbarrossilva.orca.platform.ui.component.avatar.provider.rememberAvatarImageProvider
import java.io.Serializable
import java.net.URL

internal const val AVATAR_TAG = "avatar"

private val SmallSize = 42.dp
private val LargeSize = 128.dp

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
    Avatar(SmallSize, smallShape, modifier)
}

@Composable
fun SmallAvatar(
    name: String,
    url: URL,
    modifier: Modifier = Modifier,
    imageProvider: AvatarImageProvider = rememberAvatarImageProvider()
) {
    Avatar(name, url, SmallSize, smallShape, modifier, imageProvider)
}

@Composable
fun LargeAvatar(modifier: Modifier = Modifier) {
    Avatar(LargeSize, largeShape, modifier)
}

@Composable
fun LargeAvatar(
    name: String,
    url: URL,
    modifier: Modifier = Modifier,
    imageProvider: AvatarImageProvider = rememberAvatarImageProvider()
) {
    Avatar(name, url, LargeSize, largeShape, modifier, imageProvider)
}

@Composable
private fun Avatar(
    name: String,
    url: URL,
    size: Dp,
    shape: Shape,
    modifier: Modifier = Modifier,
    imageProvider: AvatarImageProvider = rememberAvatarImageProvider()
) {
    val view = LocalView.current
    var state by remember { mutableStateOf(AvatarImageProvider.State.EMPTY) }
    val isPreviewing = remember(view) { view.isInEditMode }

    Avatar(
        size,
        shape,
        modifier.testTag(AVATAR_TAG),
        isLoading = state == AvatarImageProvider.State.LOADING
    ) {
        if (!isPreviewing && state != AvatarImageProvider.State.FAILED) {
            imageProvider.provide(name, url, onStateChange = { state = it }, Modifier.size(size))
        } else {
            UnavailableContent(size)
        }
    }
}

@Composable
private fun Avatar(
    size: Dp,
    shape: Shape,
    modifier: Modifier = Modifier,
    isLoading: Boolean = true,
    content: @Composable BoxScope.() -> Unit = { }
) {
    Placeholder(
        modifier
            .clip(shape)
            .requiredSize(size)
            .testTag(AVATAR_TAG),
        isLoading,
        shape,
        content = content
    )
}

@Composable
private fun BoxScope.UnavailableContent(size: Dp, modifier: Modifier = Modifier) {
    Box(
        modifier
            .background(PlaceholderDefaults.color)
            .matchParentSize(),
        Alignment.Center
    ) {
        Icon(
            OrcaTheme.Icons.BrokenImage,
            contentDescription = "Unavailable avatar",
            Modifier.size(size / 2),
            tint = OrcaTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
@Preview
private fun LoadingLargeAvatarPreview() {
    OrcaTheme {
        LargeAvatar()
    }
}

@Composable
@Preview
private fun LoadedLargeAvatarPreview() {
    OrcaTheme {
        LargeAvatar(Avatar.sample.name, Avatar.sample.url)
    }
}

@Composable
@Preview
private fun LoadingSmallAvatarPreview() {
    OrcaTheme {
        SmallAvatar()
    }
}

@Composable
@Preview
private fun LoadedSmallAvatarPreview() {
    OrcaTheme {
        SmallAvatar(Avatar.sample.name, Avatar.sample.url)
    }
}
