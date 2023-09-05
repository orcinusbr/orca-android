package com.jeanbarrossilva.orca.platform.ui.core.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import java.net.URL

/** [ImageProvider] that provides an asynchronously-loaded image with Coil. **/
@Immutable
internal class CoilImageProvider : ImageProvider() {
    @Composable
    @Suppress("ComposableNaming")
    override fun provide(
        url: URL,
        contentDescription: String,
        onStateChange: (State) -> Unit,
        modifier: Modifier
    ) {
        AsyncImage(
            "$url",
            contentDescription,
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
