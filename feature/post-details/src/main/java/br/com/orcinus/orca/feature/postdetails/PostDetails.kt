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

package br.com.orcinus.orca.feature.postdetails

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import br.com.orcinus.orca.composite.timeline.Timeline
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.post.figure.Figure
import br.com.orcinus.orca.composite.timeline.stat.details.StatsDetails
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.feature.postdetails.ui.header.Header
import br.com.orcinus.orca.feature.postdetails.ui.header.formatted
import br.com.orcinus.orca.feature.postdetails.viewmodel.PostDetailsViewModel
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBarDefaults
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBarWithBackNavigation
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import br.com.orcinus.orca.platform.autos.overlays.refresh.Refresh
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.std.image.SomeImageLoader
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.list.ListLoadable
import java.io.Serializable
import java.net.URI
import java.time.ZonedDateTime

@Immutable
internal data class PostDetails(
  val id: String,
  val avatarLoader: SomeImageLoader,
  val name: String,
  private val account: Account,
  val text: AnnotatedString,
  val figure: Figure?,
  private val publicationDateTime: ZonedDateTime,
  val stats: StatsDetails,
  val uri: URI
) : Serializable {
  val formattedPublicationDateTime = publicationDateTime.formatted
  val formattedUsername = account.username.toString()

  companion object {
    val sample
      @Composable
      get() =
        SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
          .withDefaultProfiles()
          .withDefaultPosts()
          .build()
          .postProvider
          .provideOneCurrent()
          .toPostDetails()
  }
}

@Composable
internal fun PostDetails(
  viewModel: PostDetailsViewModel,
  onNavigateToDetails: (postID: String) -> Unit,
  onBackwardsNavigation: () -> Unit,
  modifier: Modifier = Modifier
) {
  val postLoadable by viewModel.detailsLoadableFlow.collectAsState()
  val commentsLoadable by viewModel.commentsLoadableFlow.collectAsState()
  var isTimelineRefreshing by remember { mutableStateOf(false) }

  PostDetails(
    postLoadable,
    commentsLoadable,
    isTimelineRefreshing,
    onTimelineRefresh = {
      isTimelineRefreshing = true
      viewModel.requestRefresh { isTimelineRefreshing = false }
    },
    onFavorite = viewModel::favorite,
    onRepost = viewModel::repost,
    onShare = viewModel::share,
    onNavigateToDetails,
    onNext = viewModel::loadCommentsAt,
    onBackwardsNavigation,
    modifier
  )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PostDetails(
  postLoadable: Loadable<PostDetails>,
  commentsLoadable: ListLoadable<PostPreview>,
  isTimelineRefreshing: Boolean,
  onTimelineRefresh: () -> Unit,
  onFavorite: (postID: String) -> Unit,
  onRepost: (postID: String) -> Unit,
  onShare: (URI) -> Unit,
  onNavigateToDetails: (postID: String) -> Unit,
  onNext: (index: Int) -> Unit,
  onBackwardsNavigation: () -> Unit,
  modifier: Modifier = Modifier
) {
  val topAppBarScrollBehavior = TopAppBarDefaults.scrollBehavior

  Scaffold(
    modifier,
    topAppBar = {
      @OptIn(ExperimentalMaterial3Api::class)
      TopAppBarWithBackNavigation(
        onNavigation = onBackwardsNavigation,
        title = { AutoSizeText(stringResource(R.string.feature_post_details)) },
        scrollBehavior = topAppBarScrollBehavior
      )
    }
  ) {
    navigable {
      Timeline(
        commentsLoadable,
        onFavorite,
        onRepost,
        onShare,
        onClick = onNavigateToDetails,
        onNext,
        Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        refresh = Refresh(isTimelineRefreshing, onTimelineRefresh)
      ) {
        when (postLoadable) {
          is Loadable.Loading -> Header()
          is Loadable.Loaded ->
            Header(
              postLoadable.content,
              onFavorite = { onFavorite(postLoadable.content.id) },
              onRepost = { onRepost(postLoadable.content.id) },
              onShare = { onShare(postLoadable.content.uri) }
            )
          is Loadable.Failed -> Unit
        }
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun LoadingPostDetailsPreview() {
  AutosTheme { PostDetails(Loadable.Loading(), commentsLoadable = ListLoadable.Loading()) }
}

@Composable
@MultiThemePreview
private fun LoadedPostDetailsWithoutComments() {
  AutosTheme {
    PostDetails(Loadable.Loaded(PostDetails.sample), commentsLoadable = ListLoadable.Empty())
  }
}

@Composable
@MultiThemePreview
private fun LoadedPostDetailsPreview() {
  AutosTheme {
    PostDetails(Loadable.Loaded(PostDetails.sample), commentsLoadable = ListLoadable.Loading())
  }
}

@Composable
private fun PostDetails(
  postLoadable: Loadable<PostDetails>,
  commentsLoadable: ListLoadable<PostPreview>,
  modifier: Modifier = Modifier
) {
  PostDetails(
    postLoadable,
    commentsLoadable,
    isTimelineRefreshing = false,
    onTimelineRefresh = {},
    onFavorite = {},
    onRepost = {},
    onShare = {},
    onNavigateToDetails = {},
    onNext = {},
    onBackwardsNavigation = {},
    modifier
  )
}
