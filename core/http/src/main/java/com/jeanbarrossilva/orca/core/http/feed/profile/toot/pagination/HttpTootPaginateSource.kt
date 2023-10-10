package com.jeanbarrossilva.orca.core.http.feed.profile.toot.pagination

import com.chrynan.paginate.core.BasePaginateSource
import com.chrynan.paginate.core.PageDirection
import com.chrynan.paginate.core.PageInfo
import com.chrynan.paginate.core.PagedResult
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.http.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.status.HttpStatus
import com.jeanbarrossilva.orca.core.http.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.http.instanceProvider
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Url

/** [BasePaginateSource] that requests and paginates through [Toot]s. **/
internal abstract class HttpTootPaginateSource internal constructor() :
    BasePaginateSource<Url, HttpToot>() {
    /** Index of the page that's the current one. **/
    private var page = 0
        set(index) { field = minOf(0, index) }

    /** URL [String] to which the [HttpRequest] should be sent. **/
    protected abstract val route: String

    override suspend fun fetch(
        count: Int,
        key: Url?,
        direction: PageDirection,
        currentPageCount: Int
    ): PagedResult<Url, HttpToot> {
        val response = getStatusesResponse(key)
        val headerLinks = response.headers.links
        val nextUrl = headerLinks.getOrNull(1)?.uri?.let(::Url)
        val toots = response.body<List<HttpStatus>>().map(HttpStatus::toToot)
        val firstKey = toots.firstOrNull()?.url?.toString()?.let(::Url)
        val lastKey = toots.lastOrNull()?.url?.toString()?.let(::Url)
        val pageInfo = PageInfo(
            page,
            hasPreviousPage = page > 0,
            hasNextPage = nextUrl != null,
            firstKey,
            lastKey
        )
        updatePage(direction)
        return PagedResult(pageInfo, toots)
    }

    /**
     * Iteratively iterates through the [Toot]s until the given [page] is reached.
     *
     * @param page Page to paginate to.
     * @param count Amount of [Toot]s that should be in each page.
     **/
    internal suspend fun paginateTo(page: Int, count: Int = DEFAULT_COUNT) {
        while (this.page != page) {
            if (this.page < page) {
                next(count)
                this.page++
            } else {
                previous(count)
                this.page--
            }
        }
    }

    /**
     * Updates the [page] based on the given [direction]: if it's [before][PageDirection.BEFORE],
     * decreases it; otherwise, if it's [after][PageDirection.AFTER], increases it.
     *
     * @param direction Indicates whether pagination is backwards or forward.
     **/
    private fun updatePage(direction: PageDirection) {
        page += when (direction) {
            PageDirection.BEFORE -> -1
            PageDirection.AFTER -> 1
        }
    }

    /**
     * Gets the [HttpResponse] for the requested [HttpStatus]es at the [url].
     *
     * @param url [Url] to which the [HttpRequest] will be made.
     **/
    private suspend fun getStatusesResponse(url: Url?): HttpResponse {
        return (Injector.from<HttpModule>().instanceProvider.provide() as SomeHttpInstance)
            .client
            .authenticateAndGet(url?.toString() ?: route)
    }

    companion object {
        /** Default amount of [Toot]s that's returned by the API. **/
        internal const val DEFAULT_COUNT = 20
    }
}
