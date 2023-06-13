package com.jeanbarrossilva.mastodon.feature.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.mastodon.feature.profile.navigation.BackwardsNavigationState
import com.jeanbarrossilva.mastodon.feature.profile.navigation.Content
import com.jeanbarrossilva.mastodon.feature.profile.ui.Header
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.Placeholder
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.placeholder
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.plus
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.TootPreview

@Composable
internal fun Profile(
    onEdit: () -> Unit,
    backwardsNavigationState: BackwardsNavigationState,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val isHeaderHidden by remember(lazyListState) {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0
        }
    }

    Box {
        Scaffold(
            modifier,
            floatingActionButton = {
                FloatingActionButton(onClick = onEdit) {
                    Icon(MastodonteTheme.Icons.Edit, contentDescription = "Edit")
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) {
            LazyColumn(state = lazyListState, contentPadding = it + MastodonteTheme.overlays.fab) {
                item {
                    Header()
                }

                items(24) {
                    TootPreview(onClick = { })
                }
            }
        }

        AnimatedVisibility(
            visible = isHeaderHidden,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it }
        ) {
            @OptIn(ExperimentalMaterial3Api::class)
            CenterAlignedTopAppBar(
                title = {
                    Box(
                        Modifier
                            .placeholder(
                                Placeholder.Text(),
                                MastodonteTheme.shapes.small,
                                isVisible = true
                            )
                            .width(64.dp)
                    )
                },
                navigationIcon = { backwardsNavigationState.Content() }
            )
        }
    }
}

@Composable
@Preview
private fun ProfilePreview() {
    MastodonteTheme {
        Profile(onEdit = { }, BackwardsNavigationState.Unavailable)
    }
}
