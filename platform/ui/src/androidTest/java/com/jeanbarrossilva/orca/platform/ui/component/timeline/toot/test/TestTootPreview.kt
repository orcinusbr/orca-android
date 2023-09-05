package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.time.RelativeTimeProvider
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.time.test.rememberTestRelativeTimeProvider
import com.jeanbarrossilva.orca.platform.ui.core.image.test.rememberTestImageProvider

@Composable
@Suppress("TestFunctionName")
internal fun TestTootPreview(
    modifier: Modifier = Modifier,
    relativeTimeProvider: RelativeTimeProvider = rememberTestRelativeTimeProvider()
) {
    TootPreview(
        TootPreview.sample,
        onHighlightClick = { },
        onFavorite = { },
        onReblog = { },
        onShare = { },
        onClick = { },
        modifier,
        rememberTestImageProvider(),
        relativeTimeProvider
    )
}
