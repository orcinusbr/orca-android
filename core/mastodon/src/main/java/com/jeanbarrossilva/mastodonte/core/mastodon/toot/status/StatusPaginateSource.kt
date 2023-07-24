package com.jeanbarrossilva.mastodonte.core.mastodon.toot.status

import com.chrynan.paginate.core.BasePaginateSource
import com.chrynan.paginate.core.PageDirection
import com.chrynan.paginate.core.PageInfo
import com.chrynan.paginate.core.PagedResult
import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.mastodon.Mastodon
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.Url

abstract class StatusPaginateSource : BasePaginateSource<Url, Status>() {
    private var index = 0
    private val urls = hashSetOf<Url>()

    protected abstract val authenticationLock: AuthenticationLock
    protected abstract val route: String

    override suspend fun fetch(
        count: Int,
        key: Url?,
        direction: PageDirection,
        currentPageCount: Int
    ): PagedResult<Url, Status> {
        val response = getStatusesResponse(key)
        val headerLinks = response.headers.links
        val previousUrl = headerLinks?.first()?.uri?.let(::Url)
        val nextUrl = headerLinks?.get(1)?.uri?.let(::Url)
        val statuses = response.body<List<Status>>()
        val pageInfo = PageInfo(
            index,
            hasPreviousPage = index > 0,
            hasNextPage = nextUrl != null,
            firstKey = previousUrl,
            lastKey = nextUrl
        )
        updateIndex(direction)
        key?.run(urls::add)
        return PagedResult(pageInfo, statuses)
    }

    private fun updateIndex(direction: PageDirection) {
        index += when (direction) {
            PageDirection.BEFORE -> -1
            PageDirection.AFTER -> 1
        }
    }

    private suspend fun getStatusesResponse(url: Url?): HttpResponse {
        return Mastodon.HttpClient.get(url ?: Url(route)) {
            authenticationLock.unlock {
                header(HttpHeaders.Authorization, it.accessToken)
            }
        }
    }
}
