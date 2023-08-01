package com.jeanbarrossilva.orca.platform.ui.component.avatar.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/** [Remember][remember]s an [AvatarImageProvider]. **/
@Composable
fun rememberAvatarImageProvider(): AvatarImageProvider {
    return remember(::CoilAvatarImageProvider)
}
