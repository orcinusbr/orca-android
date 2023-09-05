package com.jeanbarrossilva.orca.platform.ui.core.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/** [Remember][remember]s an [ImageProvider]. **/
@Composable
fun rememberImageProvider(): ImageProvider {
    return remember(::CoilImageProvider)
}
