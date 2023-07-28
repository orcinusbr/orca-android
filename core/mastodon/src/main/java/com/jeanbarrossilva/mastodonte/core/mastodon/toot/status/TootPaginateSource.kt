package com.jeanbarrossilva.mastodonte.core.mastodon.toot.status

import com.chrynan.paginate.core.BasePaginateSource
import com.chrynan.paginate.core.PageDirection
import com.chrynan.paginate.core.PageInfo
import com.chrynan.paginate.core.PagedResult
import com.jeanbarrossilva.mastodonte.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.mastodonte.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.mastodonte.core.toot.Toot
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Url

abstract class TootPaginateSource internal constructor() : BasePaginateSource<Url, Toot>() {
    private var index = 0

    protected abstract val route: String

    override suspend fun fetch(
        count: Int,
        key: Url?,
        direction: PageDirection,
        currentPageCount: Int
    ): PagedResult<Url, Toot> {
        val response = getStatusesResponse(key)
        val headerLinks = response.headers.links
        val previousUrl = headerLinks?.first()?.uri?.let(::Url)
        val nextUrl = headerLinks?.get(1)?.uri?.let(::Url)
        val statuses = response.body<List<Status>>().map { it.toToot() }
        val pageInfo = PageInfo(
            index,
            hasPreviousPage = index > 0,
            hasNextPage = nextUrl != null,
            firstKey = previousUrl,
            lastKey = nextUrl
        )
        updateIndex(direction)
        return PagedResult(pageInfo, statuses)
    }

    internal suspend fun paginateTo(page: Int, count: Int = DEFAULT_COUNT) {
        var current = currentPage?.info?.index ?: -1
        while (current != page) {
            if (current < page) {
                next(count)
                current++
            } else {
                previous(count)
                current--
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
