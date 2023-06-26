package com.jeanbarrossilva.mastodon.feature.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.serialize
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.mastodon.feature.profile.navigation.BackwardsNavigationState
import com.jeanbarrossilva.mastodon.feature.profile.navigation.Content
import com.jeanbarrossilva.mastodon.feature.profile.ui.Header
import com.jeanbarrossilva.mastodon.feature.profile.viewmodel.ProfileViewModel
import com.jeanbarrossilva.mastodonte.core.inmemory.profile.sample
import com.jeanbarrossilva.mastodonte.core.inmemory.profile.toot.samples
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.edit.EditableProfile
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.plus
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.Timeline
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.TootPreview
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.loadingTootPreviews
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.toTootPreview
import java.net.URL

@Composable
fun Profile(
    viewModel: ProfileViewModel,
    navigator: ProfileNavigator,
    onEdit: () -> Unit,
    backwardsNavigationState: BackwardsNavigationState,
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
    modifier: Modifier = Modifier
) {
    val profileLoadable by viewModel.profileLoadableFlow.collectAsState()
    val tootsLoadable by viewModel.tootsLoadableFlow.collectAsState()

    Profile(
        profileLoadable,
        tootsLoadable,
        onFollowToggle = viewModel::toggleFollow,
        onFavorite = viewModel::favorite,
        onReblog = viewModel::reblog,
        navigator::navigateToTootDetails,
        onNext = viewModel::loadTootsAt,
        onEdit,
        backwardsNavigationState,
        navigator::navigateToWebpage,
        onShare = viewModel::share,
        onBottomAreaAvailabilityChangeListener,
        modifier
    )
}

@Composable
private fun Profile(
    profileLoadable: Loadable<Profile>,
    tootsLoadable: ListLoadable<Toot>,
    onFollowToggle: () -> Unit,
    onFavorite: (tootID: String) -> Unit,
    onReblog: (tootID: String) -> Unit,
    onNavigationToTootDetails: (id: String) -> Unit,
    onNext: (index: Int) -> Unit,
    onEdit: () -> Unit,
    backwardsNavigationState: BackwardsNavigationState,
    onNavigateToWebpage: (URL) -> Unit,
    onShare: (URL) -> Unit,
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
    modifier: Modifier = Modifier
) {
    when (profileLoadable) {
        is Loadable.Loading ->
            Profile(backwardsNavigationState, onBottomAreaAvailabilityChangeListener, modifier)
        is Loadable.Loaded ->
            Profile(
                profileLoadable.content,
                tootsLoadable,
                onFollowToggle,
                onFavorite,
                onReblog,
                onNavigationToTootDetails,
                onNext,
                onEdit,
                backwardsNavigationState,
                onNavigateToWebpage,
                onShare,
                onBottomAreaAvailabilityChangeListener,
                modifier
            )
        is Loadable.Failed ->
            Unit
    }
}

@Composable
private fun Profile(
    backwardsNavigationState: BackwardsNavigationState,
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
    modifier: Modifier = Modifier
) {
    Profile(
        title = { MediumTextualPlaceholder() },
        actions = { },
        header = { Header() },
        toots = { loadingTootPreviews() },
        onNext = { },
        floatingActionButton = { },
        backwardsNavigationState,
        onBottomAreaAvailabilityChangeListener,
        modifier
    )
}

@Composable
private fun Profile(
    profile: Profile,
    tootsLoadable: ListLoadable<Toot>,
    onFollowToggle: () -> Unit,
    onFavorite: (tootID: String) -> Unit,
    onReblog: (tootID: String) -> Unit,
    onNavigationToTootDetails: (id: String) -> Unit,
    onNext: (index: Int) -> Unit,
    onEdit: () -> Unit,
    backwardsNavigationState: BackwardsNavigationState,
    onNavigateToWebpage: (URL) -> Unit,
    onShare: (URL) -> Unit,
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    var isTopBarDropdownExpanded by remember { mutableStateOf(false) }

    Profile(
        title = { Text("@${profile.account.username}") },
        actions = {
            Box {
                IconButton(onClick = { isTopBarDropdownExpanded = true }) {
                    Icon(MastodonteTheme.Icons.MoreVert, contentDescription = "More")
                }

                DropdownMenu(
                    isTopBarDropdownExpanded,
                    onDismissRequest = { isTopBarDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Open in browser") },
                        onClick = {
                            onNavigateToWebpage(profile.url)
                            isTopBarDropdownExpanded = false
                        },
                        leadingIcon = {
                            Icon(
                                MastodonteTheme.Icons.OpenInBrowser,
                                contentDescription = "Open in browser"
                            )
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Copy URL") },
                        onClick = {
                            clipboardManager.setText(AnnotatedString("${profile.url}"))
                            isTopBarDropdownExpanded = false
                        },
                        leadingIcon = {
                            Icon(MastodonteTheme.Icons.Link, contentDescription = "Share")
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Share") },
                        onClick = {
                            onShare(profile.url)
                            isTopBarDropdownExpanded = false
                        },
                        leadingIcon = {
                            Icon(MastodonteTheme.Icons.Share, contentDescription = "Share")
                        }
                    )
                }
            }
        },
        header = { Header(profile, onFollowToggle) },
        toots = {
            when (
                @Suppress("NAME_SHADOWING")
                val tootsLoadable = tootsLoadable
            ) {
                is ListLoadable.Populated ->
                    items(tootsLoadable.content) {
                        TootPreview(
                            it.toTootPreview(),
                            onFavorite = { onFavorite(it.id) },
                            onReblog = { onReblog(it.id) },
                            onShare = { onShare(it.url) },
                            onClick = { onNavigationToTootDetails(it.id) }
                        )
                    }
                is ListLoadable.Loading ->
                    loadingTootPreviews()
                is ListLoadable.Empty, is ListLoadable.Failed ->
                    Unit
            }
        },
        onNext,
        floatingActionButton = {
            if (profile is EditableProfile) {
                FloatingActionButton(onClick = onEdit) {
                    Icon(MastodonteTheme.Icons.Edit, contentDescription = "Edit")
                }
            }
        },
        backwardsNavigationState,
        onBottomAreaAvailabilityChangeListener,
        modifier
    )
}

@Composable
private fun Profile(
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    header: @Composable () -> Unit,
    toots: LazyListScope.() -> Unit,
    onNext: (index: Int) -> Unit,
    floatingActionButton: @Composable () -> Unit,
    backwardsNavigationState: BackwardsNavigationState,
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val isHeaderHidden by remember(lazyListState) {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0
        }
    }
    val isBottomAreaAvailable by remember {
        derivedStateOf {
            lazyListState.canScrollForward
        }
    }

    DisposableEffect(isBottomAreaAvailable) {
        onBottomAreaAvailabilityChangeListener.onBottomAreaAvailabilityChange(isBottomAreaAvailable)
        onDispose { }
    }

    Box(modifier) {
        Scaffold(
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = FabPosition.Center
        ) {
            Timeline(
                onNext,
                Modifier.statusBarsPadding(),
                lazyListState,
                contentPadding = it + MastodonteTheme.overlays.fab
            ) {
                item { header() }
                toots()
            }
        }

        AnimatedVisibility(
            visible = isHeaderHidden,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it }
        ) {
            @OptIn(ExperimentalMaterial3Api::class)
            CenterAlignedTopAppBar(
                title = title,
                navigationIcon = { backwardsNavigationState.Content() },
                actions = actions
            )
        }
    }
}

@Composable
@Preview
private fun LoadingProfilePreview() {
    MastodonteTheme {
        Profile(BackwardsNavigationState.Unavailable, OnBottomAreaAvailabilityChangeListener.empty)
    }
}

@Composable
@Preview
private fun LoadedProfilePreview() {
    MastodonteTheme {
        Profile(
            Profile.sample,
            ListLoadable.Populated(Toot.samples.serialize()),
            onFollowToggle = { },
            onFavorite = { },
            onReblog = { },
            onNavigationToTootDetails = { },
            onNext = { },
            onEdit = { },
            BackwardsNavigationState.Unavailable,
            onNavigateToWebpage = { },
            onShare = { },
            onBottomAreaAvailabilityChangeListener = OnBottomAreaAvailabilityChangeListener.empty
        )
    }
}
