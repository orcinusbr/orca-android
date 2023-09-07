package com.jeanbarrossilva.orca.platform.ui.core.image.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.ui.core.image.ImageProvider
import java.net.URL

/** [ImageProvider] that doesn't actually provide an image. **/
internal class TestImageProvider : ImageProvider() {
    @Composable
    @Suppress("ComposableNaming")
    override fun provide(
        url: URL,
        contentDescription: String,
        onStateChange: (State) -> Unit,
        modifier: Modifier
    ) {
    }
}
