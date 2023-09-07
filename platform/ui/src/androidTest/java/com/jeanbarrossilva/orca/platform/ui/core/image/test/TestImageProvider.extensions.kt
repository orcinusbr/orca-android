package com.jeanbarrossilva.orca.platform.ui.core.image.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/** [Remember][remember]s a [TestImageProvider]. **/
@Composable
internal fun rememberTestImageProvider(): TestImageProvider {
    return remember(::TestImageProvider)
}
