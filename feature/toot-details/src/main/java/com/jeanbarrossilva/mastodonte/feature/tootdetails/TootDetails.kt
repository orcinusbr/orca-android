package com.jeanbarrossilva.mastodonte.feature.tootdetails

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.mastodonte.core.profile.toot.Account
import com.jeanbarrossilva.mastodonte.core.profile.toot.at
import com.jeanbarrossilva.mastodonte.feature.tootdetails.ui.header.Header
import com.jeanbarrossilva.mastodonte.feature.tootdetails.ui.header.formatted
import com.jeanbarrossilva.mastodonte.feature.tootdetails.viewmodel.TootDetailsViewModel
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.backwardsNavigationArrow
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.Timeline
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.TootPreview
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.formatted
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.loadingTootPreviews
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.relative
import java.io.Serializable
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Objects
import java.util.UUID

internal data class TootDetails(
    val id: String,
    val avatarURL: URL,
    val name: String,
    private val account: Account,
    val body: AnnotatedString,
    private val publicationDateTime: ZonedDateTime,
    private val commentCount: Int,
    private val favoriteCount: Int,
    private val reblogCount: Int,
    val url: URL
) : Serializable {
    val formattedPublicationDateTime = publicationDateTime.formatted
    val formattedUsername = "@${account.username}"
    val formattedCommentCount = commentCount.formatted
    val formattedFavoriteCount = favoriteCount.formatted
    val formattedReblogCount = reblogCount.formatted

    override fun equals(other: Any?): Boolean {
        return Objects.equals(this, other)
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            avatarURL,
            name,
            account,
            body,
            publicationDateTime,
            commentCount,
            favoriteCount,
            reblogCount,
            url
        )
    }

    fun toTootPreview(): TootPreview {
        return TootPreview(
            avatarURL,
            name,
            formattedUsername,
            timeSincePublication = publicationDateTime.relative,
            body,
            formattedCommentCount,
            formattedFavoriteCount,
            formattedReblogCount
        )
    }

    companion object {
        private val samplePublicationDateTime =
            ZonedDateTime.of(2003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3"))

        @Suppress("SpellCheckingInspection")
        val sample = TootDetails(
            id = "${UUID.randomUUID()}",
            TootPreview.sample.avatarURL,
            TootPreview.sample.name,
            "jeanbarrossilva" at "mastodon.social",
            TootPreview.sample.body,
            samplePublicationDateTime,
            commentCount = 1_024,
            favoriteCount = 2_048,
            reblogCount = 512,
            URL("https://mastodon.social/@christianselig/110492858891694580")
        )
    }
}

@Composable
fun TootDetails(
    viewModel: TootDetailsViewModel,
    navigator: TootDetailsNavigator,
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
    detailsLoadable: Loadable<TootDetails>,
    commentsLoadable: ListLoadable<TootDetails>,
    onFavorite: (id: String) -> Unit,
    onReblog: (id: String) -> Unit,
    onShare: (URL) -> Unit,
    onNavigateToDetails: (id: String) -> Unit,
    onNext: (index: Int) -> Unit,
    onBackwardsNavigation: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (detailsLoadable) {
        is Loadable.Loading ->
            TootDetails(onBackwardsNavigation, modifier)
        is Loadable.Loaded ->
            TootDetails(
                detailsLoadable.content,
                commentsLoadable,
                onFavorite,
                onReblog,
                onShare,
                onNavigateToDetails,
                onNext,
                onBackwardsNavigation,
                modifier
            )
        is Loadable.Failed ->
            Unit
    }
}

@Composable
private fun TootDetails(onBackwardsNavigation: () -> Unit, modifier: Modifier = Modifier) {
    TootDetails(
        header = { Header() },
        comments = { loadingTootPreviews() },
        onNext = { },
        onBackwardsNavigation,
        modifier
    )
}

@Composable
private fun TootDetails(
    details: TootDetails,
    commentsLoadable: ListLoadable<TootDetails>,
    onFavorite: (id: String) -> Unit,
    onReblog: (id: String) -> Unit,
    onShare: (URL) -> Unit,
    onNavigateToDetails: (id: String) -> Unit,
    onNext: (index: Int) -> Unit,
    onBackwardsNavigation: () -> Unit,
    modifier: Modifier = Modifier
) {
    TootDetails(
        header = { Header(details, onShare = { onShare(details.url) }) },
        comments = {
            when (
                @Suppress("NAME_SHADOWING")
                val commentsLoadable = commentsLoadable
            ) {
                is ListLoadable.Loading ->
                    loadingTootPreviews()
                is ListLoadable.Populated ->
                    items(commentsLoadable.content) {
                        TootPreview(
                            it.toTootPreview(),
                            onFavorite = { onFavorite(it.id) },
                            onReblog = { onReblog(it.id) },
                            onShare = { onShare(it.url) },
                            onClick = { onNavigateToDetails(it.id) }
                        )
                    }
                is ListLoadable.Empty, is ListLoadable.Failed ->
                    Unit
            }
        },
        onNext,
        onBackwardsNavigation,
        modifier
    )
}

@Composable
private fun TootDetails(
    header: @Composable () -> Unit,
    comments: LazyListScope.() -> Unit,
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
        Timeline(onNext, contentPadding = it) {
            item { header() }
            comments()
        }
    }
}

@Composable
@Preview
private fun LoadingTootDetailsPreview() {
    MastodonteTheme {
        TootDetails(onBackwardsNavigation = { })
    }
}

@Composable
@Preview
private fun LoadedTootDetailsPreview() {
    MastodonteTheme {
        TootDetails(
            TootDetails.sample,
            commentsLoadable = ListLoadable.Loading(),
            onFavorite = { },
            onReblog = { },
            onShare = { },
            onNavigateToDetails = { },
            onNext = { },
            onBackwardsNavigation = { }
        )
    }
}
