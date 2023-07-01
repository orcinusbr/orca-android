package com.jeanbarrossilva.mastodonte.platform.ui.component.avatar.provider.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.mastodonte.platform.ui.component.avatar.provider.AvatarImageProvider
import java.net.URL

/** [AvatarImageProvider] that doesn't actually provide an image. **/
internal class TestAvatarImageProvider : AvatarImageProvider() {
    @Composable
    @Suppress("ComposableNaming")
    override fun provide(
        name: String,
        url: URL,
        onStateChange: (State) -> Unit,
        modifier: Modifier
    ) {
    }
}
