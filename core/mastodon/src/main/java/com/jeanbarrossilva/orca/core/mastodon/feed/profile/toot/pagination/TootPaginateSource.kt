package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.pagination

import com.chrynan.paginate.core.BasePaginateSource
import com.chrynan.paginate.core.PageDirection
import com.chrynan.paginate.core.PageInfo
import com.chrynan.paginate.core.PagedResult
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status.Status
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Url

abstract class TootPaginateSource internal constructor() : BasePaginateSource<Url, Toot>() {
    private var index = 0
        set(index) { field = minOf(0, index) }

    protected abstract val route: String

    override suspend fun fetch(
        count: Int,
        key: Url?,
        direction: PageDirection,
        currentPageCount: Int
    ): PagedResult<Url, Toot> {
        val response = getStatusesResponse(key)
        val headerLinks = response.headers.links
        val nextUrl = headerLinks.getOrNull(1)?.uri?.let(::Url)
        val toots = response.body<List<Status>>().map(Status::toToot)
        val firstKey = toots.firstOrNull()?.url?.toString()?.let(::Url)
        val lastKey = toots.lastOrNull()?.url?.toString()?.let(::Url)
        val pageInfo = PageInfo(
            index,
            hasPreviousPage = index > 0,
            hasNextPage = nextUrl != null,
            firstKey,
            lastKey
        )
        updateIndex(direction)
        return PagedResult(pageInfo, toots)
    }

    internal suspend fun paginateTo(page: Int, count: Int = DEFAULT_COUNT) {
        while (index != page) {
            if (index < page) {
                next(count)
                index++
            } else {
                previous(count)
                index--
            }
        }
    }

    private fun updateIndex(direction: PageDirection) {
        index += when (direction) {
            PageDirection.BEFORE -> -1
            PageDirection.AFTER -> 1
        }
    }

    private suspend fun getStatusesResponse(url: Url?): HttpResponse {
        return MastodonHttpClient.authenticateAndGet(url?.toString() ?: route)
    }

    companion object {
        internal const val DEFAULT_COUNT = 20
    }
}
