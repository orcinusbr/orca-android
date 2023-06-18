package com.jeanbarrossilva.mastodon.feature.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.ifLoaded
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.serialize
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.map
import com.jeanbarrossilva.mastodon.feature.profile.navigation.BackwardsNavigationState
import com.jeanbarrossilva.mastodon.feature.profile.navigation.Content
import com.jeanbarrossilva.mastodon.feature.profile.ui.Header
import com.jeanbarrossilva.mastodonte.core.profile.AnyProfile
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.toot.Account
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.Placeholder
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.`if`
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.placeholder
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.plus
import com.jeanbarrossilva.mastodonte.platform.ui.profile.sample
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.TootPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Composable
internal fun Profile(
    loadable: Loadable<AnyProfile>,
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
    var tootsLoadable by remember { mutableStateOf<ListLoadable<Toot>>(ListLoadable.Loading()) }
    val accountLoadable = remember(loadable) { loadable.map(AnyProfile::account) }

    LaunchedEffect(loadable) {
        tootsLoadable = when (loadable) {
            is Loadable.Loading -> ListLoadable.Loading()
            is Loadable.Loaded ->
                loadable
                    .content
                    .getToots(0)
                    .map(List<Toot>::serialize)
                    .first()
                    .toListLoadable()
            is Loadable.Failed -> ListLoadable.Failed(loadable.error)
        }
    }

    Box(modifier) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = onEdit) {
                    Icon(MastodonteTheme.Icons.Edit, contentDescription = "Edit")
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { padding ->
            LazyColumn(
                state = lazyListState,
                contentPadding = padding + MastodonteTheme.overlays.fab
            ) {
                item {
                    Header(loadable)
                }

                when (
                    @Suppress("NAME_SHADOWING")
                    val tootsLoadable = tootsLoadable
                ) {
                    is ListLoadable.Loading -> {
                        items(24) {
                            TootPreview(Loadable.Loading(), onClick = { })
                        }
                    }
                    is ListLoadable.Populated -> {
                        items(tootsLoadable.content) {
                            TootPreview(Loadable.Loaded(it), onClick = { })
                        }
                    }
                    else -> { }
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
                title = { Title(accountLoadable) },
                navigationIcon = { backwardsNavigationState.Content() }
            )
        }
    }
}

@Composable
private fun Title(loadable: Loadable<Account>, modifier: Modifier = Modifier) {
    val isLoading = remember(loadable) {
        loadable is Loadable.Loading
    }

    Text(
        loadable.ifLoaded { "@$username" }.orEmpty(),
        modifier
            .placeholder(Placeholder.Text(), MastodonteTheme.shapes.small, isVisible = isLoading)
            .`if`({ isLoading }) { width(64.dp) }
    )
}

@Composable
@Preview
private fun ProfilePreview() {
    MastodonteTheme {
        Profile(Loadable.Loaded(Profile.sample), onEdit = { }, BackwardsNavigationState.Unavailable)
    }
}
