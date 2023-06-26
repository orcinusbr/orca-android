package com.jeanbarrossilva.mastodonte.platform.ui

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.jeanbarrossilva.loadable.placeholder.Placeholder
import com.jeanbarrossilva.loadable.placeholder.PlaceholderDefaults
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import java.io.Serializable
import java.net.URL

private val SmallSize = 42.dp
private val LargeSize = 128.dp

private val smallShape
    @Composable get() = MastodonteTheme.shapes.small
private val largeShape
    @Composable get() = MastodonteTheme.shapes.large

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
fun SmallAvatar(name: String, url: URL, modifier: Modifier = Modifier) {
    Avatar(name, url, SmallSize, smallShape, modifier)
}

@Composable
fun LargeAvatar(modifier: Modifier = Modifier) {
    Avatar(LargeSize, largeShape, modifier)
}

@Composable
fun LargeAvatar(name: String, url: URL, modifier: Modifier = Modifier) {
    Avatar(name, url, LargeSize, largeShape, modifier)
}

@Composable
private fun Avatar(
    name: String,
    url: URL,
    size: Dp,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    var state by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }
    val isPreviewing = remember(view) { view.isInEditMode }

    Avatar(size, shape, modifier, state is AsyncImagePainter.State.Loading) {
        AsyncImage(
            "$url",
            contentDescription = "$name's avatar",
            Modifier.size(size),
            onState = { state = it }
        )

        if (isPreviewing || state is AsyncImagePainter.State.Error) {
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
            .requiredSize(size),
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
            MastodonteTheme.Icons.BrokenImage,
            contentDescription = "Unavailable avatar",
            Modifier.size(size / 2),
            tint = MastodonteTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
@Preview
private fun LoadingLargeAvatarPreview() {
    MastodonteTheme {
        LargeAvatar()
    }
}

@Composable
@Preview
private fun LoadedLargeAvatarPreview() {
    MastodonteTheme {
        LargeAvatar(Avatar.sample.name, Avatar.sample.url)
    }
}

@Composable
@Preview
private fun LoadingSmallAvatarPreview() {
    MastodonteTheme {
        SmallAvatar()
    }
}

@Composable
@Preview
private fun LoadedSmallAvatarPreview() {
    MastodonteTheme {
        SmallAvatar(Avatar.sample.name, Avatar.sample.url)
    }
}
