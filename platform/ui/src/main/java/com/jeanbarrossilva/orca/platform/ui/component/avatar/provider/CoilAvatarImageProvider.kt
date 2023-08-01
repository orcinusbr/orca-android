package com.jeanbarrossilva.orca.platform.ui.component.avatar.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import java.net.URL

/** [AvatarImageProvider] that provides an asynchronously-loaded image with Coil. **/
@Immutable
internal class CoilAvatarImageProvider : AvatarImageProvider() {
    @Composable
    @Suppress("ComposableNaming")
    override fun provide(
        name: String,
        url: URL,
        onStateChange: (State) -> Unit,
        modifier: Modifier
    ) {
        AsyncImage(
            "$url",
            contentDescription = "$name's avatar",
            modifier,
            onState = {
                when (it) {
                    is AsyncImagePainter.State.Empty -> onStateChange(State.EMPTY)
                    is AsyncImagePainter.State.Loading -> onStateChange(State.LOADING)
                    is AsyncImagePainter.State.Success -> onStateChange(State.LOADED)
                    is AsyncImagePainter.State.Error -> onStateChange(State.FAILED)
                }
            }
        )
    }
}
