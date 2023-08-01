package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.ui.component.avatar.provider.test.rememberTestAvatarImageProvider
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.time.RelativeTimeProvider
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.time.test.rememberTestRelativeTimeProvider

@Composable
@Suppress("TestFunctionName")
internal fun TestTootPreview(
    modifier: Modifier = Modifier,
    relativeTimeProvider: RelativeTimeProvider = rememberTestRelativeTimeProvider()
) {
    TootPreview(
        TootPreview.sample,
        onFavorite = { },
        onReblog = { },
        onShare = { },
        onClick = { },
        modifier,
        rememberTestAvatarImageProvider(),
        relativeTimeProvider
    )
}
