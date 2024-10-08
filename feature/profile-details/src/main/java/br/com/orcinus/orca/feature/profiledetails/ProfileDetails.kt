/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.feature.profiledetails

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.composite.timeline.LoadingTimeline
import br.com.orcinus.orca.composite.timeline.Timeline
import br.com.orcinus.orca.composite.timeline.TimelineDefaults
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.post.time.RelativeTimeProvider
import br.com.orcinus.orca.composite.timeline.post.time.rememberRelativeTimeProvider
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.type.editable.SampleEditableProfile
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.SampleFollowService
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.feature.profiledetails.navigation.BackwardsNavigationState
import br.com.orcinus.orca.feature.profiledetails.navigation.NavigationButton
import br.com.orcinus.orca.feature.profiledetails.ui.Header
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import br.com.orcinus.orca.platform.autos.kit.menu.DropdownMenu
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.presenter.ErrorPresentation
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.presenter.SnackbarPresenter
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.presenter.rememberSnackbarPresenter
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBarDefaults as _TopAppBarDefaults
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.`if`
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import br.com.orcinus.orca.platform.autos.overlays.refresh.Refresh
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.SomeImageLoader
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import java.io.Serializable
import java.net.URI

internal const val ProfileDetailsTopBarTag = "profile-details-top-bar"

internal sealed class ProfileDetails : Serializable {
  protected abstract val account: Account

  abstract val id: String
  abstract val avatarLoader: SomeImageLoader
  abstract val name: String
  abstract val bio: AnnotatedString
  abstract val uri: URI

  val formattedAccount
    get() = account.toString()

  val username
    get() = account.username.toString()

  data class Default(
    override val id: String,
    override val avatarLoader: SomeImageLoader,
    override val name: String,
    override val account: Account,
    override val bio: AnnotatedString,
    override val uri: URI
  ) : ProfileDetails() {
    companion object
  }

  data class Editable(
    override val id: String,
    override val avatarLoader: SomeImageLoader,
    override val name: String,
    override val account: Account,
    override val bio: AnnotatedString,
    override val uri: URI
  ) : ProfileDetails() {
    @Composable
    override fun FloatingActionButton(navigator: ProfileDetailsBoundary, modifier: Modifier) {
      FloatingActionButton(onClick = {}) {
        Icon(AutosTheme.iconography.edit.filled.asImageVector, contentDescription = "Edit")
      }
    }

    companion object
  }

  data class Followable(
    override val id: String,
    override val avatarLoader: SomeImageLoader,
    override val name: String,
    override val account: Account,
    override val bio: AnnotatedString,
    override val uri: URI,
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
      Button(onClick = onStatusToggle, modifier.testTag(MainActionButtonTag)) { Text(status.label) }
    }

    companion object {
      @Suppress("ConstPropertyName")
      const val MainActionButtonTag = "followable-profile-details-main-action-button"
    }
  }

  @Composable
  fun MainActionButton() {
    MainActionButton(Modifier)
  }

  @Composable open fun MainActionButton(modifier: Modifier) {}

  @Composable
  fun FloatingActionButton(navigator: ProfileDetailsBoundary) {
    FloatingActionButton(navigator, Modifier)
  }

  @Composable
  open fun FloatingActionButton(navigator: ProfileDetailsBoundary, modifier: Modifier) {}

  companion object {
    val sample: ProfileDetails
      @Composable
      get() {
        val coroutineScope = rememberCoroutineScope()
        val profileProvider = SampleProfileProvider()
        val followService = SampleFollowService(profileProvider)
        return SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
          .withDefaultProfiles()
          .withDefaultPosts()
          .build()
          .profileProvider
          .provideCurrent<SampleEditableProfile>()
          .toProfileDetails(coroutineScope, followService, AutosTheme.colors)
      }
  }
}

@Composable
internal fun ProfileDetails(
  viewModel: ProfileDetailsViewModel,
  boundary: ProfileDetailsBoundary,
  backwardsNavigationState: BackwardsNavigationState,
  onNavigationToPostDetails: (id: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val detailsLoadable by viewModel.detailsLoadableFlow.collectAsState()
  val postsLoadable by viewModel.postPreviewsLoadableFlow.collectAsState()
  var isTimelineRefreshing by remember { mutableStateOf(false) }

  ProfileDetails(
    boundary,
    detailsLoadable,
    postsLoadable,
    isTimelineRefreshing,
    onTimelineRefresh = {
      isTimelineRefreshing = true
      viewModel.requestRefresh { isTimelineRefreshing = false }
    },
    onFavorite = viewModel::favorite,
    onRepost = viewModel::repost,
    onNavigationToPostDetails,
    onNext = viewModel::loadPostsAt,
    backwardsNavigationState,
    boundary::navigateTo,
    onShare = viewModel::share,
    modifier
  )
}

@Composable
@VisibleForTesting
internal fun ProfileDetails(
  profileDetailsLoadable: Loadable<ProfileDetails>,
  postPreviewsLoadable: ListLoadable<PostPreview>,
  modifier: Modifier = Modifier,
  isTopBarDropdownMenuExpanded: Boolean = false,
  initialFirstVisibleTimelineItemIndex: Int = 0,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider()
) {
  ProfileDetails(
    profileDetailsLoadable,
    postPreviewsLoadable,
    isTimelineRefreshing = false,
    onTimelineRefresh = {},
    modifier,
    isTopBarDropdownMenuExpanded,
    initialFirstVisibleTimelineItemIndex,
    relativeTimeProvider
  )
}

@Composable
@VisibleForTesting
internal fun ProfileDetails(
  detailsLoadable: Loadable<ProfileDetails>,
  postPreviewsLoadable: ListLoadable<PostPreview>,
  isTimelineRefreshing: Boolean,
  onTimelineRefresh: () -> Unit,
  modifier: Modifier = Modifier,
  isTopBarDropdownMenuExpanded: Boolean = false,
  initialFirstVisibleTimelineItemIndex: Int = 0,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider()
) {
  ProfileDetails(
    ProfileDetailsBoundary.empty,
    detailsLoadable,
    postPreviewsLoadable,
    isTimelineRefreshing,
    onTimelineRefresh,
    onFavorite = {},
    onRepost = {},
    onNavigationToPostDetails = {},
    onNext = {},
    BackwardsNavigationState.Unavailable,
    onNavigateToWebpage = {},
    onShare = {},
    modifier,
    isTopBarDropdownMenuExpanded,
    initialFirstVisibleTimelineItemIndex,
    relativeTimeProvider
  )
}

@Composable
private fun ProfileDetails(
  boundary: ProfileDetailsBoundary,
  detailsLoadable: Loadable<ProfileDetails>,
  postPreviewsLoadable: ListLoadable<PostPreview>,
  isTimelineRefreshing: Boolean,
  onTimelineRefresh: () -> Unit,
  onFavorite: (postID: String) -> Unit,
  onRepost: (postID: String) -> Unit,
  onNavigationToPostDetails: (id: String) -> Unit,
  onNext: (index: Int) -> Unit,
  origin: BackwardsNavigationState,
  onNavigateToWebpage: (URI) -> Unit,
  onShare: (URI) -> Unit,
  modifier: Modifier = Modifier,
  isTopBarDropdownMenuExpanded: Boolean = false,
  initialFirstVisibleTimelineItemIndex: Int = 0,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider()
) {
  when (detailsLoadable) {
    is Loadable.Loading -> ProfileDetails(onNext, origin, modifier)
    is Loadable.Loaded ->
      ProfileDetails(
        boundary,
        detailsLoadable.content,
        postPreviewsLoadable,
        isTimelineRefreshing,
        onTimelineRefresh,
        onFavorite,
        onRepost,
        onNavigationToPostDetails,
        onNext,
        origin,
        onNavigateToWebpage,
        onShare,
        modifier,
        isTopBarDropdownMenuExpanded,
        initialFirstVisibleTimelineItemIndex,
        relativeTimeProvider
      )
    is Loadable.Failed -> ProfileDetails(detailsLoadable.error, onNext, modifier)
  }
}

@Composable
private fun ProfileDetails(
  onNext: (index: Int) -> Unit,
  origin: BackwardsNavigationState,
  modifier: Modifier = Modifier
) {
  @OptIn(ExperimentalMaterial3Api::class)
  val topAppBarScrollBehavior = _TopAppBarDefaults.scrollBehavior

  @OptIn(ExperimentalMaterial3Api::class)
  ProfileDetails(
    topAppBarScrollBehavior,
    title = { MediumTextualPlaceholder() },
    actions = {},
    timelineState = rememberLazyStaggeredGridState(),
    timeline = { shouldNestScrollToTopAppBar ->
      LoadingTimeline(
        onNext,
        (Modifier as Modifier).`if`(shouldNestScrollToTopAppBar) {
          nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
        }
      ) {
        Header()
      }
    },
    floatingActionButton = {},
    rememberSnackbarPresenter(),
    origin,
    modifier
  )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ProfileDetails(
  boundary: ProfileDetailsBoundary,
  details: ProfileDetails,
  postsLoadable: ListLoadable<PostPreview>,
  isTimelineRefreshing: Boolean,
  onTimelineRefresh: () -> Unit,
  onFavorite: (postID: String) -> Unit,
  onRepost: (postID: String) -> Unit,
  onNavigationToPostDetails: (id: String) -> Unit,
  onNext: (index: Int) -> Unit,
  origin: BackwardsNavigationState,
  onNavigateToWebpage: (URI) -> Unit,
  onShare: (URI) -> Unit,
  modifier: Modifier = Modifier,
  isTopBarDropdownMenuExpanded: Boolean = false,
  initialFirstVisibleTimelineItemIndex: Int = 0,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider()
) {
  val clipboardManager = LocalClipboardManager.current
  val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
  var isTopBarDropdownExpanded by
    remember(isTopBarDropdownMenuExpanded) { mutableStateOf(isTopBarDropdownMenuExpanded) }
  val snackbarPresenter = rememberSnackbarPresenter()
  val timelineState = rememberLazyStaggeredGridState(initialFirstVisibleTimelineItemIndex)

  ProfileDetails(
    topAppBarScrollBehavior,
    title = { AutoSizeText(details.username) },
    actions = {
      Box {
        HoverableIconButton(onClick = { isTopBarDropdownExpanded = true }) {
          Icon(
            AutosTheme.iconography.expand.asImageVector,
            contentDescription = stringResource(R.string.feature_profile_details_more)
          )
        }

        DropdownMenu(isTopBarDropdownExpanded, onDismissal = { isTopBarDropdownExpanded = false }) {
          DropdownMenuItem(
            text = { Text(stringResource(R.string.feature_profile_details_open_in_browser)) },
            onClick = {
              onNavigateToWebpage(details.uri)
              isTopBarDropdownExpanded = false
            },
            leadingIcon = {
              Icon(
                AutosTheme.iconography.link.asImageVector,
                contentDescription = stringResource(R.string.feature_profile_details_external_link)
              )
            }
          )

          DropdownMenuItem(
            text = { Text(stringResource(R.string.feature_profile_details_copy_url)) },
            onClick = {
              clipboardManager.setText(AnnotatedString("${details.uri}"))
              isTopBarDropdownExpanded = false
            },
            leadingIcon = {
              Icon(
                AutosTheme.iconography.link.asImageVector,
                contentDescription = stringResource(R.string.feature_profile_details_share)
              )
            }
          )

          DropdownMenuItem(
            text = { Text(stringResource(R.string.feature_profile_details_share)) },
            onClick = {
              onShare(details.uri)
              isTopBarDropdownExpanded = false
            },
            leadingIcon = {
              Icon(
                AutosTheme.iconography.share.outlined.asImageVector,
                contentDescription = stringResource(R.string.feature_profile_details_share)
              )
            }
          )
        }
      }
    },
    timelineState,
    timeline = { shouldNestScrollToTopAppBar ->
      Timeline(
        postsLoadable,
        onFavorite,
        onRepost,
        onShare,
        onClick = onNavigationToPostDetails,
        onNext,
        Modifier.statusBarsPadding().`if`(shouldNestScrollToTopAppBar) {
          nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
        },
        timelineState,
        contentPadding =
          PaddingValues(bottom = if (details is ProfileDetails.Editable) 56.dp else 0.dp),
        Refresh(isTimelineRefreshing, onTimelineRefresh),
        relativeTimeProvider
      ) {
        Header(details)
      }
    },
    floatingActionButton = { details.FloatingActionButton(boundary) },
    snackbarPresenter,
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
  timelineState: LazyStaggeredGridState,
  timeline: @Composable (shouldNestScrollToTopAppBar: Boolean) -> Unit,
  floatingActionButton: @Composable () -> Unit,
  snackbarPresenter: SnackbarPresenter,
  origin: BackwardsNavigationState,
  modifier: Modifier = Modifier
) {
  val isHeaderHidden by
    remember(timelineState) { derivedStateOf { timelineState.firstVisibleItemIndex > 0 } }

  LaunchedEffect(isHeaderHidden) {
    if (!isHeaderHidden) {
      timelineState.animateScrollToItem(0)
    }
  }

  Box(modifier) {
    Scaffold(floatingActionButton = floatingActionButton, snackbarPresenter = snackbarPresenter) {
      navigable { timeline(isHeaderHidden) }
    }

    AnimatedVisibility(
      visible = isHeaderHidden,
      enter = slideInVertically { -it },
      exit = slideOutVertically { -it }
    ) {
      @OptIn(ExperimentalMaterial3Api::class)
      TopAppBar(
        title,
        Modifier.testTag(ProfileDetailsTopBarTag),
        navigationIcon = { origin.NavigationButton() },
        actions = actions,
        scrollBehavior = topAppBarScrollBehavior
      )
    }
  }
}

@Composable
private fun ProfileDetails(
  error: Throwable,
  onNext: (index: Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val snackbarPresenter = rememberSnackbarPresenter()

  Scaffold(modifier, snackbarPresenter = snackbarPresenter) {
    navigable {
      ErrorPresentation(
        error,
        refreshListener = { onNext(TimelineDefaults.InitialSubsequentPaginationIndex) },
        snackbarPresenter
      )
    }
  }
}

@Composable
@MultiThemePreview
private fun LoadingProfileDetailsPreview() {
  AutosTheme { ProfileDetails(onNext = {}, BackwardsNavigationState.Unavailable) }
}

@Composable
@MultiThemePreview
private fun LoadedProfileDetailsWithoutPostsPreview() {
  AutosTheme {
    ProfileDetails(
      Loadable.Loaded(ProfileDetails.sample),
      postPreviewsLoadable = ListLoadable.Empty()
    )
  }
}

@Composable
@MultiThemePreview
private fun LoadedProfileDetailsWithPostsPreview() {
  AutosTheme {
    ProfileDetails(
      Loadable.Loaded(ProfileDetails.sample),
      PostPreview.createSamples(
          SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
            .withDefaultProfiles()
            .withDefaultPosts()
            .build()
            .postProvider
        )
        .toSerializableList()
        .toListLoadable()
    )
  }
}

@Composable
@MultiThemePreview
private fun LoadedProfileDetailsWithExpandedTopBarDropdownMenuPreview() {
  AutosTheme {
    ProfileDetails(
      Loadable.Loaded(ProfileDetails.sample),
      PostPreview.createSamples(
          SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
            .withDefaultProfiles()
            .withDefaultPosts()
            .build()
            .postProvider
        )
        .toSerializableList()
        .toListLoadable(),
      isTopBarDropdownMenuExpanded = true,
      initialFirstVisibleTimelineItemIndex = 1
    )
  }
}
