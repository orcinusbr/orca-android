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
import androidx.compose.material3.Button
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
import com.jeanbarrossilva.mastodonte.core.inmemory.profile.toot.samples
import com.jeanbarrossilva.mastodonte.core.profile.toot.Account
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.plus
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.Timeline
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.TootPreview
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.loadingTootPreviews
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.toTootPreview
import java.io.Serializable
import java.net.URL
import java.util.UUID

internal sealed class ProfileDetails : Serializable {
    protected abstract val account: Account

    abstract val id: String
    abstract val avatarURL: URL
    abstract val name: String
    abstract val bio: String
    abstract val url: URL

    val formattedAccount
        get() = "${account.username}@${account.instance}"
    val username
        get() = "@${account.instance}"

    data class Default(
        override val id: String,
        override val avatarURL: URL,
        override val name: String,
        override val account: Account,
        override val bio: String,
        override val url: URL
    ) : ProfileDetails()

    data class Editable(
        override val id: String,
        override val avatarURL: URL,
        override val name: String,
        override val account: Account,
        override val bio: String,
        override val url: URL
    ) : ProfileDetails() {
        @Composable
        override fun FloatingActionButton(navigator: ProfileNavigator, modifier: Modifier) {
            FloatingActionButton(onClick = { }) {
                Icon(MastodonteTheme.Icons.Edit, contentDescription = "Edit")
            }
        }
    }

    data class Followable(
        override val id: String,
        override val avatarURL: URL,
        override val name: String,
        override val account: Account,
        override val bio: String,
        override val url: URL,
        val follow: Follow,
        val onToggleFollow: () -> Unit
    ) : ProfileDetails() {
        enum class Follow {
            UNFOLLOWED {
                override val label = "Follow"
            },
            REQUESTED {
                override val label = "Requested"
            },
            FOLLOWING {
                override val label = "Unfollow"
            };

            abstract val label: String
        }

        @Composable
        override fun MainActionButton(modifier: Modifier) {
            Button(onClick = onToggleFollow) {
                Text(follow.label)
            }
        }
    }

    @Composable
    fun MainActionButton() {
        MainActionButton(Modifier)
    }

    @Composable
    open fun MainActionButton(modifier: Modifier) {
    }

    @Composable
    fun FloatingActionButton(navigator: ProfileNavigator) {
        FloatingActionButton(navigator, Modifier)
    }

    @Composable
    open fun FloatingActionButton(navigator: ProfileNavigator, modifier: Modifier) {
    }

    companion object {
        @Suppress("SpellCheckingInspection")
        val sample = Default(
            "${UUID.randomUUID()}",
            TootPreview.sample.avatarURL,
            TootPreview.sample.name,
            TootPreview.sampleAccount,
            bio = "Co-founder @ Grupo Estoa, software engineer, author, writer and content " +
                "creator; neuroscience, quantum physics and philosophy enthusiast.",
            URL(
                "https://en.gravatar.com/userimage/153558542/08942ba9443ce68bf66345a2e6db656e.png"
            )
        )
    }
}

@Composable
fun Profile(
    viewModel: ProfileViewModel,
    navigator: ProfileNavigator,
    backwardsNavigationState: BackwardsNavigationState,
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
    modifier: Modifier = Modifier
) {
    val detailsLoadable by viewModel.detailsLoadableFlow.collectAsState()
    val tootsLoadable by viewModel.tootsLoadableFlow.collectAsState()

    Profile(
        navigator,
        detailsLoadable,
        tootsLoadable,
        onFavorite = viewModel::favorite,
        onReblog = viewModel::reblog,
        navigator::navigateToTootDetails,
        onNext = viewModel::loadTootsAt,
        backwardsNavigationState,
        navigator::navigateToWebpage,
        onShare = viewModel::share,
        onBottomAreaAvailabilityChangeListener,
        modifier
    )
}

@Composable
private fun Profile(
    navigator: ProfileNavigator,
    detailsLoadable: Loadable<ProfileDetails>,
    tootsLoadable: ListLoadable<Toot>,
    onFavorite: (tootID: String) -> Unit,
    onReblog: (tootID: String) -> Unit,
    onNavigationToTootDetails: (id: String) -> Unit,
    onNext: (index: Int) -> Unit,
    backwardsNavigationState: BackwardsNavigationState,
    onNavigateToWebpage: (URL) -> Unit,
    onShare: (URL) -> Unit,
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
    modifier: Modifier = Modifier
) {
    when (detailsLoadable) {
        is Loadable.Loading ->
            Profile(backwardsNavigationState, onBottomAreaAvailabilityChangeListener, modifier)
        is Loadable.Loaded ->
            Profile(
                navigator,
                detailsLoadable.content,
                tootsLoadable,
                onFavorite,
                onReblog,
                onNavigationToTootDetails,
                onNext,
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
    navigator: ProfileNavigator,
    details: ProfileDetails,
    tootsLoadable: ListLoadable<Toot>,
    onFavorite: (tootID: String) -> Unit,
    onReblog: (tootID: String) -> Unit,
    onNavigationToTootDetails: (id: String) -> Unit,
    onNext: (index: Int) -> Unit,
    backwardsNavigationState: BackwardsNavigationState,
    onNavigateToWebpage: (URL) -> Unit,
    onShare: (URL) -> Unit,
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    var isTopBarDropdownExpanded by remember { mutableStateOf(false) }

    Profile(
        title = { Text(details.username) },
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
                            onNavigateToWebpage(details.url)
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
                            clipboardManager.setText(AnnotatedString("${details.url}"))
                            isTopBarDropdownExpanded = false
                        },
                        leadingIcon = {
                            Icon(MastodonteTheme.Icons.Link, contentDescription = "Share")
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Share") },
                        onClick = {
                            onShare(details.url)
                            isTopBarDropdownExpanded = false
                        },
                        leadingIcon = {
                            Icon(MastodonteTheme.Icons.Share, contentDescription = "Share")
                        }
                    )
                }
            }
        },
        header = { Header(details) },
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
        floatingActionButton = { details.FloatingActionButton(navigator) },
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
            ProfileNavigator.empty,
            ProfileDetails.sample,
            ListLoadable.Populated(Toot.samples.serialize()),
            onFavorite = { },
            onReblog = { },
            onNavigationToTootDetails = { },
            onNext = { },
            BackwardsNavigationState.Unavailable,
            onNavigateToWebpage = { },
            onShare = { },
            onBottomAreaAvailabilityChangeListener = OnBottomAreaAvailabilityChangeListener.empty
        )
    }
}
