package com.jeanbarrossilva.mastodonte.platform.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.rounded.BrokenImage
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.Placeholder
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.placeholder
import java.io.Serializable
import java.net.URL

data class Avatar(val name: String, val url: URL) : Serializable

@Composable
fun LargeAvatar(loadable: Loadable<Avatar>, modifier: Modifier = Modifier) {
    Avatar(loadable, 128.dp, MastodonteTheme.shapes.large, modifier)
}

@Composable
fun SmallAvatar(loadable: Loadable<Avatar>, modifier: Modifier = Modifier) {
    Avatar(loadable, 42.dp, MastodonteTheme.shapes.small, modifier)
}

@Composable
private fun Avatar(
    loadable: Loadable<Avatar>,
    size: Dp,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    var painterState by remember {
        mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty)
    }
    val isLoading by remember(loadable, painterState) {
        derivedStateOf {
            loadable is Loadable.Loading || painterState is AsyncImagePainter.State.Loading
        }
    }

    Box(
        modifier
            .placeholder(
                Placeholder withHeightOf size,
                MastodonteTheme.colorScheme.surfaceVariant,
                shape,
                isVisible = isLoading
            )
            .clip(shape)
            .size(size),
        Alignment.Center
    ) {
        when (loadable) {
            is Loadable.Loaded -> LoadedAvatar(
                loadable.content,
                onPainterStateChange = { painterState = it },
                Modifier.matchParentSize()
            )
            is Loadable.Failed -> FailedAvatar(size, Modifier.matchParentSize())
            else -> { }
        }
    }
}

@Composable
private fun LoadedAvatar(
    avatar: Avatar,
    onPainterStateChange: (painterState: AsyncImagePainter.State) -> Unit,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        "${avatar.url}",
        contentDescription = "${avatar.name}'s avatar",
        modifier,
        onState = onPainterStateChange
    )
}

@Composable
private fun FailedAvatar(size: Dp, modifier: Modifier = Modifier) {
    Box(modifier.background(MastodonteTheme.colorScheme.surfaceVariant), Alignment.Center) {
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
        LargeAvatar(Loadable.Loading())
    }
}

@Composable
@Preview
private fun LoadingSmallAvatarPreview() {
    MastodonteTheme {
        SmallAvatar(Loadable.Loading())
    }
}

@Composable
@Preview
private fun FailedLargeAvatarPreview() {
    MastodonteTheme {
        LargeAvatar(Loadable.Failed(Exception()))
    }
}

@Composable
@Preview
private fun FailedSmallAvatarPreview() {
    MastodonteTheme {
        SmallAvatar(Loadable.Failed(Exception()))
    }
}
