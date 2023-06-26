package com.jeanbarrossilva.mastodonte.platform.ui.timeline

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.loadingTootPreviews

@Composable
fun Timeline(
    onNext: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
    content: LazyListScope.() -> Unit
) {
    val shouldLoad = remember(state) { !state.canScrollForward }
    var index by remember { mutableIntStateOf(0) }

    DisposableEffect(shouldLoad) {
        onNext(index++)
        onDispose { }
    }

    LazyColumn(modifier, state, contentPadding, content = content)
}

@Composable
@Preview
private fun TimelineColumnPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            Timeline(onNext = { }) {
                loadingTootPreviews()
            }
        }
    }
}
