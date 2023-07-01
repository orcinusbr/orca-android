package com.jeanbarrossilva.mastodonte.platform.ui.component.avatar.provider.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/** [Remember][remember]s a [TestAvatarImageProvider]. **/
@Composable
internal fun rememberTestAvatarImageProvider(): TestAvatarImageProvider {
    return remember(::TestAvatarImageProvider)
}
