package com.jeanbarrossilva.mastodonte.feature.tootdetails

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.mapNotNull
import com.jeanbarrossilva.mastodonte.core.feed.profile.account.Account
import com.jeanbarrossilva.mastodonte.core.feed.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.toot.sample
import com.jeanbarrossilva.mastodonte.feature.tootdetails.ui.header.Header
import com.jeanbarrossilva.mastodonte.feature.tootdetails.ui.header.formatted
import com.jeanbarrossilva.mastodonte.feature.tootdetails.viewmodel.TootDetailsViewModel
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.backwardsNavigationArrow
import com.jeanbarrossilva.mastodonte.platform.ui.AccountFormatter
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.Timeline
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.TootPreview
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.formatted
import java.io.Serializable
import java.net.URL
import java.time.ZonedDateTime

@Immutable
internal data class TootDetails(
    val id: String,
    val avatarURL: URL,
    val name: String,
    private val account: Account,
    val body: AnnotatedString,
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

    fun toTootPreview(): TootPreview {
        return TootPreview(
            id,
            avatarURL,
            name,
            account,
            body,
            publicationDateTime,
            commentCount,
            isFavorite,
            favoriteCount,
            isReblogged,
            reblogCount,
            url
        )
    }

    companion object {
        val sample = TootDetails(
            Toot.sample.id,
            Toot.sample.author.avatarURL,
            Toot.sample.author.name,
            Toot.sample.author.account,
            TootPreview.sample.body,
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
    navigator: TootDetailsBoundary,
    modifier: Modifier = Modifier
) {
    val tootLoadable by viewModel.detailsLoadableFlow.collectAsState()
    val commentsLoadable by viewModel.commentsLoadableFlow.collectAsState()

    TootDetails(
        tootLoadable,
        commentsLoadable,
        onFavorite = viewModel::favorite,
        onReblog = viewModel::reblog,
        onShare = viewModel::share,
        onNavigateToDetails = navigator::navigateToTootDetails,
        onNext = viewModel::loadCommentsAt,
        onBackwardsNavigation = navigator::pop,
        modifier
    )
}

@Composable
private fun TootDetails(
    tootLoadable: Loadable<TootDetails>,
    commentsLoadable: ListLoadable<TootDetails>,
    onFavorite: (tootID: String) -> Unit,
    onReblog: (tootID: String) -> Unit,
    onShare: (URL) -> Unit,
    onNavigateToDetails: (tootID: String) -> Unit,
    onNext: (index: Int) -> Unit,
    onBackwardsNavigation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            CenterAlignedTopAppBar(
                title = { Text("Toot") },
                navigationIcon = {
                    IconButton(onClick = onBackwardsNavigation) {
                        Icon(
                            MastodonteTheme.Icons.backwardsNavigationArrow,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        Timeline(
            commentsLoadable.mapNotNull(TootDetails::toTootPreview),
            onFavorite,
            onReblog,
            onShare,
            onClick = onNavigateToDetails,
            onNext,
            contentPadding = it
        ) {
            when (tootLoadable) {
                is Loadable.Loading ->
                    Header()
                is Loadable.Loaded ->
                    Header(
                        tootLoadable.content,
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
private fun TootDetails(
    onBackwardsNavigation: () -> Unit,
    modifier: Modifier = Modifier,
    timeline: @Composable (padding: PaddingValues) -> Unit
) {
    Scaffold(
        modifier,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            CenterAlignedTopAppBar(
                title = { Text("Toot") },
                navigationIcon = {
                    IconButton(onClick = onBackwardsNavigation) {
                        Icon(
                            MastodonteTheme.Icons.backwardsNavigationArrow,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = timeline
    )
}

@Composable
@Preview
private fun LoadingTootDetailsPreview() {
    MastodonteTheme {
        TootDetails(Loadable.Loading(), commentsLoadable = ListLoadable.Loading())
    }
}

@Composable
@Preview
private fun LoadedTootDetailsPreview() {
    MastodonteTheme {
        TootDetails(Loadable.Loaded(TootDetails.sample), commentsLoadable = ListLoadable.Loading())
    }
}

@Composable
private fun TootDetails(
    tootLoadable: Loadable<TootDetails>,
    commentsLoadable: ListLoadable<TootDetails>,
    modifier: Modifier = Modifier
) {
    TootDetails(
        tootLoadable,
        commentsLoadable,
        onFavorite = { },
        onReblog = { },
        onShare = { },
        onNavigateToDetails = { },
        onNext = { },
        onBackwardsNavigation = { },
        modifier
    )
}
