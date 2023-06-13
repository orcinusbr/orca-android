package com.jeanbarrossilva.mastodonte.platform.ui.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.Placeholder
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.placeholder

@Composable
fun LargeAvatar(modifier: Modifier = Modifier) {
    Avatar(128.dp, MastodonteTheme.shapes.large, modifier.size(128.dp))
}

@Composable
fun SmallAvatar(modifier: Modifier = Modifier) {
    Avatar(56.dp, MastodonteTheme.shapes.small, modifier)
}

@Composable
private fun Avatar(size: Dp, shape: Shape, modifier: Modifier = Modifier) {
    Box(
        modifier
            .placeholder(
                Placeholder withHeightOf size,
                MastodonteTheme.colorScheme.surfaceVariant,
                shape,
                isVisible = true
            )
            .size(size)
    )
}

@Composable
@Preview
private fun LargeAvatarPreview() {
    MastodonteTheme {
        LargeAvatar()
    }
}

@Composable
@Preview
private fun SmallAvatarPreview() {
    MastodonteTheme {
        SmallAvatar()
    }
}
