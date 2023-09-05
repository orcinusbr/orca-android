package com.jeanbarrossilva.orca.feature.tootdetails

import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample
import com.jeanbarrossilva.orca.feature.tootdetails.ui.header.Header
import com.jeanbarrossilva.orca.feature.tootdetails.ui.header.formatted
import com.jeanbarrossilva.orca.feature.tootdetails.viewmodel.TootDetailsViewModel
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.extensions.backwardsNavigationArrow
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBar
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBarDefaults
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.theme.reactivity.BottomAreaAvailabilityNestedScrollConnection
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.platform.theme.reactivity.rememberBottomAreaAvailabilityNestedScrollConnection
import com.jeanbarrossilva.orca.platform.ui.AccountFormatter
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.formatted
import java.io.Serializable
import java.net.URL
import java.time.ZonedDateTime

@Immutable
internal data class TootDetails(
    val id: String,
    val avatarURL: URL,
    val name: String,
    private val account: Account,
    val text: AnnotatedString,
    val highlight: Highlight?,
    private val publicationDateTime: ZonedDateTime,
    private val commentCount: Int,
    val isFavorite: Boolean,
    private val favoriteCount: Int,
    val isReblogged: Boolean,
    private val reblogCount: Int,
    val url: URL
) : Serializable {
    val formattedPublicationDateTime = publicationDateTime.formatted
    val formattedUsername = AccountFormatter.username(account)
    val formattedCommentCount = commentCount.formatted
    val formattedFavoriteCount = favoriteCount.formatted
    val formattedReblogCount = reblogCount.formatted

    companion object {
        val sample
            @Composable get() = TootDetails(
                Toot.sample.id,
                Toot.sample.author.avatarURL,
                Toot.sample.author.name,
                Toot.sample.author.account,
                TootPreview.sample.text,
                TootPreview.sample.highlight,
                Toot.sample.publicationDateTime,
                Toot.sample.commentCount,
                Toot.sample.isFavorite,
                Toot.sample.favoriteCount,
                Toot.sample.isReblogged,
                Toot.sample.reblogCount,
                Toot.sample.url
            )
    }
}

@Composable
internal fun TootDetails(
    viewModel: TootDetailsViewModel,
    boundary: TootDetailsBoundary,
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
    modifier: Modifier = Modifier
) {
    val tootLoadable by viewModel.detailsLoadableFlow.collectAsState()
    val commentsLoadable by viewModel.commentsLoadableFlow.collectAsState()
    val bottomAreaAvailabilityNestedScrollConnection =
        rememberBottomAreaAvailabilityNestedScrollConnection(onBottomAreaAvailabilityChangeListener)

    TootDetails(
        tootLoadable,
        commentsLoadable,
        onHighlightClick = boundary::navigateTo,
        onFavorite = viewModel::favorite,
        onReblog = viewModel::reblog,
        onShare = viewModel::share,
        onNavigateToDetails = boundary::navigateToTootDetails,
        onNext = viewModel::loadCommentsAt,
        onBackwardsNavigation = boundary::pop,
        bottomAreaAvailabilityNestedScrollConnection,
        modifier
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TootDetails(
    tootLoadable: Loadable<TootDetails>,
    commentsLoadable: ListLoadable<TootPreview>,
    onHighlightClick: (URL) -> Unit,
    onFavorite: (tootID: String) -> Unit,
    onReblog: (tootID: String) -> Unit,
    onShare: (URL) -> Unit,
    onNavigateToDetails: (tootID: String) -> Unit,
    onNext: (index: Int) -> Unit,
    onBackwardsNavigation: () -> Unit,
    bottomAreaAvailabilityNestedScrollConnection: BottomAreaAvailabilityNestedScrollConnection,
    modifier: Modifier = Modifier
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.scrollBehavior

    Scaffold(
        modifier,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackwardsNavigation) {
                        Icon(
                            OrcaTheme.Icons.backwardsNavigationArrow,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = topAppBarScrollBehavior
            ) {
                AutoSizeText("Toot")
            }
        }
    ) {
        Timeline(
            commentsLoadable,
            onHighlightClick,
            onFavorite,
            onReblog,
            onShare,
            onClick = onNavigateToDetails,
            onNext,
            Modifier
                .nestedScroll(bottomAreaAvailabilityNestedScrollConnection)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            contentPadding = it
        ) {
            when (tootLoadable) {
                is Loadable.Loading ->
                    Header()
                is Loadable.Loaded ->
                    Header(
                        tootLoadable.content,
                        onHighlightClick = {
                            tootLoadable.content.highlight?.url?.run(onHighlightClick)
                        },
                        onFavorite = { onFavorite(tootLoadable.content.id) },
                        onReblog = { onReblog(tootLoadable.content.id) },
                        onShare = { onShare(tootLoadable.content.url) }
                    )
                is Loadable.Failed ->
                    Unit
            }
        }
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun LoadingTootDetailsPreview() {
    OrcaTheme {
        TootDetails(Loadable.Loading(), commentsLoadable = ListLoadable.Loading())
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun LoadedTootDetailsWithoutComments() {
    OrcaTheme {
        TootDetails(Loadable.Loaded(TootDetails.sample), commentsLoadable = ListLoadable.Empty())
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun LoadedTootDetailsPreview() {
    OrcaTheme {
        TootDetails(Loadable.Loaded(TootDetails.sample), commentsLoadable = ListLoadable.Loading())
    }
}

@Composable
private fun TootDetails(
    tootLoadable: Loadable<TootDetails>,
    commentsLoadable: ListLoadable<TootPreview>,
    modifier: Modifier = Modifier
) {
    TootDetails(
        tootLoadable,
        commentsLoadable,
        onHighlightClick = { },
        onFavorite = { },
        onReblog = { },
        onShare = { },
        onNavigateToDetails = { },
        onNext = { },
        onBackwardsNavigation = { },
        BottomAreaAvailabilityNestedScrollConnection.empty,
        modifier
    )
}
