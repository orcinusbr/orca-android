package com.jeanbarrossilva.orca.feature.profiledetails

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable.sample
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable.toStatus
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.NavigationButton
import com.jeanbarrossilva.orca.feature.profiledetails.ui.Header
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.theme.extensions.`if`
import com.jeanbarrossilva.orca.platform.theme.kit.menu.DropdownMenu
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBar
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBarDefaults as _TopAppBarDefaults
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.theme.reactivity.BottomAreaAvailabilityNestedScrollConnection
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.platform.theme.reactivity.rememberBottomAreaAvailabilityNestedScrollConnection
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString
import java.io.Serializable
import java.net.URL

internal const val PROFILE_DETAILS_TOP_BAR_TAG = "profile-details-top-bar"

internal sealed class ProfileDetails : Serializable {
    protected abstract val account: Account

    abstract val id: String
    abstract val avatarURL: URL
    abstract val name: String
    abstract val bio: AnnotatedString
    abstract val url: URL

    val formattedAccount
        get() = "${account.username}@${account.instance}"
    val username
        get() = "@${account.username}"

    data class Default(
        override val id: String,
        override val avatarURL: URL,
        override val name: String,
        override val account: Account,
        override val bio: AnnotatedString,
        override val url: URL
    ) : ProfileDetails() {
        companion object {
            fun createSample(colors: Colors): Default {
                return Default(
                    Profile.sample.id,
                    Profile.sample.avatarURL,
                    Profile.sample.name,
                    Profile.sample.account,
                    Profile.sample.bio.toAnnotatedString(colors),
                    Profile.sample.url
                )
            }
        }
    }

    data class Editable(
        override val id: String,
        override val avatarURL: URL,
        override val name: String,
        override val account: Account,
        override val bio: AnnotatedString,
        override val url: URL
    ) : ProfileDetails() {
        @Composable
        override fun FloatingActionButton(navigator: ProfileDetailsBoundary, modifier: Modifier) {
            FloatingActionButton(onClick = { }) {
                Icon(OrcaTheme.Icons.Edit, contentDescription = "Edit")
            }
        }

        companion object {
            fun createSample(colors: Colors): Editable {
                return Editable(
                    EditableProfile.sample.id,
                    EditableProfile.sample.avatarURL,
                    EditableProfile.sample.name,
                    EditableProfile.sample.account,
                    EditableProfile.sample.bio.toAnnotatedString(colors),
                    EditableProfile.sample.url
                )
            }
        }
    }

    data class Followable(
        override val id: String,
        override val avatarURL: URL,
        override val name: String,
        override val account: Account,
        override val bio: AnnotatedString,
        override val url: URL,
        val status: Status,
        private val onStatusToggle: () -> Unit
    ) : ProfileDetails() {
        enum class Status {
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
            Button(onClick = onStatusToggle, modifier.testTag(MAIN_ACTION_BUTTON_TAG)) {
                Text(status.label)
            }
        }

        companion object {
            const val MAIN_ACTION_BUTTON_TAG = "followable-profile-details-main-action-button"

            fun createSample(colors: Colors, onStatusToggle: () -> Unit): Followable {
                return Followable(
                    FollowableProfile.sample.id,
                    FollowableProfile.sample.avatarURL,
                    FollowableProfile.sample.name,
                    FollowableProfile.sample.account,
                    FollowableProfile.sample.bio.toAnnotatedString(colors),
                    FollowableProfile.sample.url,
                    FollowableProfile.sample.follow.toStatus(),
                    onStatusToggle
                )
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
    fun FloatingActionButton(navigator: ProfileDetailsBoundary) {
        FloatingActionButton(navigator, Modifier)
    }

    @Composable
    open fun FloatingActionButton(navigator: ProfileDetailsBoundary, modifier: Modifier) {
    }

    companion object {
        val sample
            @Composable get() = Default.createSample(OrcaTheme.colors)
    }
}

@Composable
internal fun ProfileDetails(
    viewModel: ProfileDetailsViewModel,
    navigator: ProfileDetailsBoundary,
    origin: BackwardsNavigationState,
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
    modifier: Modifier = Modifier
) {
    val detailsLoadable by viewModel.detailsLoadableFlow.collectAsState()
    val tootsLoadable by viewModel.tootPreviewsLoadableFlow.collectAsState()
    val bottomAreaAvailabilityNestedScrollConnection =
        rememberBottomAreaAvailabilityNestedScrollConnection(onBottomAreaAvailabilityChangeListener)

    ProfileDetails(
        navigator,
        detailsLoadable,
        tootsLoadable,
        onFavorite = viewModel::favorite,
        onReblog = viewModel::reblog,
        navigator::navigateToTootDetails,
        onNext = viewModel::loadTootsAt,
        origin,
        navigator::navigateToWebpage,
        onShare = viewModel::share,
        bottomAreaAvailabilityNestedScrollConnection,
        modifier
    )
}

@Composable
internal fun ProfileDetails(
    detailsLoadable: Loadable<ProfileDetails>,
    tootPreviewsLoadable: ListLoadable<TootPreview>,
    modifier: Modifier = Modifier,
    isTopBarDropdownMenuExpanded: Boolean = false,
    initialFirstVisibleTimelineItemIndex: Int = 0
) {
    ProfileDetails(
        ProfileDetailsBoundary.empty,
        detailsLoadable,
        tootPreviewsLoadable,
        onFavorite = { },
        onReblog = { },
        onNavigationToTootDetails = { },
        onNext = { },
        BackwardsNavigationState.Unavailable,
        onNavigateToWebpage = { },
        onShare = { },
        BottomAreaAvailabilityNestedScrollConnection.empty,
        modifier,
        isTopBarDropdownMenuExpanded,
        initialFirstVisibleTimelineItemIndex
    )
}

@Composable
private fun ProfileDetails(
    navigator: ProfileDetailsBoundary,
    detailsLoadable: Loadable<ProfileDetails>,
    tootPreviewsLoadable: ListLoadable<TootPreview>,
    onFavorite: (tootID: String) -> Unit,
    onReblog: (tootID: String) -> Unit,
    onNavigationToTootDetails: (id: String) -> Unit,
    onNext: (index: Int) -> Unit,
    origin: BackwardsNavigationState,
    onNavigateToWebpage: (URL) -> Unit,
    onShare: (URL) -> Unit,
    bottomAreaAvailabilityNestedScrollConnection: BottomAreaAvailabilityNestedScrollConnection,
    modifier: Modifier = Modifier,
    isTopBarDropdownMenuExpanded: Boolean = false,
    initialFirstVisibleTimelineItemIndex: Int = 0
) {
    when (detailsLoadable) {
        is Loadable.Loading ->
            ProfileDetails(origin, bottomAreaAvailabilityNestedScrollConnection, modifier)
        is Loadable.Loaded ->
            ProfileDetails(
                navigator,
                detailsLoadable.content,
                tootPreviewsLoadable,
                onFavorite,
                onReblog,
                onNavigationToTootDetails,
                onNext,
                origin,
                onNavigateToWebpage,
                onShare,
                bottomAreaAvailabilityNestedScrollConnection,
                modifier,
                isTopBarDropdownMenuExpanded,
                initialFirstVisibleTimelineItemIndex
            )
        is Loadable.Failed ->
            Unit
    }
}

@Composable
private fun ProfileDetails(
    origin: BackwardsNavigationState,
    bottomAreaAvailabilityNestedScrollConnection: BottomAreaAvailabilityNestedScrollConnection,
    modifier: Modifier = Modifier
) {
    @OptIn(ExperimentalMaterial3Api::class)
    val topAppBarScrollBehavior = _TopAppBarDefaults.scrollBehavior

    @OptIn(ExperimentalMaterial3Api::class)
    ProfileDetails(
        topAppBarScrollBehavior,
        title = { MediumTextualPlaceholder() },
        actions = { },
        timelineState = rememberLazyListState(),
        timeline = { shouldNestScrollToTopAppBar, _ ->
            Timeline(
                Modifier
                    .nestedScroll(bottomAreaAvailabilityNestedScrollConnection)
                    .`if`(shouldNestScrollToTopAppBar) {
                        nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                    }
            ) {
                Header()
            }
        },
        floatingActionButton = { },
        origin,
        modifier
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ProfileDetails(
    navigator: ProfileDetailsBoundary,
    details: ProfileDetails,
    tootsLoadable: ListLoadable<TootPreview>,
    onFavorite: (tootID: String) -> Unit,
    onReblog: (tootID: String) -> Unit,
    onNavigationToTootDetails: (id: String) -> Unit,
    onNext: (index: Int) -> Unit,
    origin: BackwardsNavigationState,
    onNavigateToWebpage: (URL) -> Unit,
    onShare: (URL) -> Unit,
    bottomAreaAvailabilityNestedScrollConnection: BottomAreaAvailabilityNestedScrollConnection,
    modifier: Modifier = Modifier,
    isTopBarDropdownMenuExpanded: Boolean = false,
    initialFirstVisibleTimelineItemIndex: Int = 0
) {
    val clipboardManager = LocalClipboardManager.current
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var isTopBarDropdownExpanded by remember(isTopBarDropdownMenuExpanded) {
        mutableStateOf(isTopBarDropdownMenuExpanded)
    }
    val timelineState = rememberLazyListState(initialFirstVisibleTimelineItemIndex)

    ProfileDetails(
        topAppBarScrollBehavior,
        title = { AutoSizeText(details.username) },
        actions = {
            Box {
                IconButton(onClick = { isTopBarDropdownExpanded = true }) {
                    Icon(OrcaTheme.Icons.MoreVert, contentDescription = "More")
                }

                DropdownMenu(
                    isTopBarDropdownExpanded,
                    onDismissal = { isTopBarDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Open in browser") },
                        onClick = {
                            onNavigateToWebpage(details.url)
                            isTopBarDropdownExpanded = false
                        },
                        leadingIcon = {
                            Icon(
                                OrcaTheme.Icons.OpenInBrowser,
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
                            Icon(OrcaTheme.Icons.Link, contentDescription = "Share")
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Share") },
                        onClick = {
                            onShare(details.url)
                            isTopBarDropdownExpanded = false
                        },
                        leadingIcon = {
                            Icon(OrcaTheme.Icons.Share, contentDescription = "Share")
                        }
                    )
                }
            }
        },
        timelineState,
        timeline = { shouldNestScrollToTopAppBar, padding ->
            Timeline(
                tootsLoadable,
                onFavorite,
                onReblog,
                onShare,
                onClick = onNavigationToTootDetails,
                onNext,
                Modifier
                    .statusBarsPadding()
                    .nestedScroll(bottomAreaAvailabilityNestedScrollConnection)
                    .`if`(shouldNestScrollToTopAppBar) {
                        nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                    },
                timelineState,
                contentPadding = padding
            ) {
                Header(details)
            }
        },
        floatingActionButton = { details.FloatingActionButton(navigator) },
        origin,
        modifier
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ProfileDetails(
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    timelineState: LazyListState,
    timeline: @Composable (shouldNestScrollToTopAppBar: Boolean, padding: PaddingValues) -> Unit,
    floatingActionButton: @Composable () -> Unit,
    origin: BackwardsNavigationState,
    modifier: Modifier = Modifier
) {
    val isHeaderHidden by remember(timelineState) {
        derivedStateOf {
            timelineState.firstVisibleItemIndex > 0
        }
    }

    LaunchedEffect(isHeaderHidden) {
        if (!isHeaderHidden) {
            timelineState.animateScrollToItem(0)
        }
    }

    Box(modifier) {
        Scaffold(floatingActionButton = floatingActionButton) {
            timeline(isHeaderHidden, it)
        }

        AnimatedVisibility(
            visible = isHeaderHidden,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it }
        ) {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                Modifier.testTag(PROFILE_DETAILS_TOP_BAR_TAG),
                navigationIcon = { origin.NavigationButton() },
                actions,
                topAppBarScrollBehavior,
                title
            )
        }
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun LoadingProfileDetailsPreview() {
    OrcaTheme {
        ProfileDetails(
            BackwardsNavigationState.Unavailable,
            BottomAreaAvailabilityNestedScrollConnection.empty
        )
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun LoadedProfileDetailsWithoutTootsPreview() {
    OrcaTheme {
        ProfileDetails(
            Loadable.Loaded(ProfileDetails.sample),
            tootPreviewsLoadable = ListLoadable.Empty()
        )
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun LoadedProfileDetailsWithTootsPreview() {
    OrcaTheme {
        ProfileDetails(
            Loadable.Loaded(ProfileDetails.sample),
            tootPreviewsLoadable = TootPreview.samples.toSerializableList().toListLoadable()
        )
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun LoadedProfileDetailsWithExpandedTopBarDropdownMenuPreview() {
    OrcaTheme {
        ProfileDetails(
            Loadable.Loaded(ProfileDetails.sample),
            tootPreviewsLoadable = TootPreview.samples.toSerializableList().toListLoadable(),
            isTopBarDropdownMenuExpanded = true,
            initialFirstVisibleTimelineItemIndex = 1
        )
    }
}
