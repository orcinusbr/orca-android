/*
 * Copyright © 2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination

import app.cash.turbine.test
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.page.Page
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.page.Pages
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.type.KTypeCreator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.type.kTypeCreatorOf
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.RequesterTestScope
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.runAuthenticatedRequesterTest
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import io.ktor.client.engine.mock.respond
import io.ktor.client.statement.request
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.LinkHeader
import io.ktor.http.toURI
import java.net.URI
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/** [RouteSpec] from which both the header and the route are obtainable. */
private abstract class InternalRouteSpec : RouteSpec() {
  /** `rel` parameter of the header. If `null`, the [header] is also `null`. */
  protected abstract val rel: String?

  override fun matches(route: URI) = route() == route

  override fun toString() = "RouteSpec(route=${route()})"

  /** Specified route as it currently is. If `null`, the [header] is also `null`. */
  abstract fun route(): URI?

  /** Header linking to the specified route. `null` if either the [rel] or the [route] is `null`. */
  fun header(): LinkHeader? {
    val rel = rel
    val route = route()
    return if (rel != null && route != null) LinkHeader("$route", rel) else null
  }
}

/** Single implementation of [Routes]. */
private data class RoutesImpl(
  override val initial: InternalRouteSpec,
  override val previous: InternalRouteSpec?,
  override val current: InternalRouteSpec?,
  override val next: InternalRouteSpec?
) :
  Routes(),
  Map<String, InternalRouteSpec?> by LinkedHashMap<String, InternalRouteSpec?>(
      /* initialCapacity = */ 4
    )
    .apply({
      set(RoutesImpl::initial.name, initial)
      set(RoutesImpl::previous.name, previous)
      set(RoutesImpl::current.name, current)
      set(RoutesImpl::next.name, next)
    })

/**
 * Single implementation of [MastodonPostPaginatorScope].
 *
 * @param requesterScope [RequesterTestScope] in which the [Post]s of each page are shared. Also
 *   acts as a delegate of this class' [CoroutineScope]-like subclassed functionality.
 * @param authenticationLock [AuthenticationLock] that authenticates the HTTP requests performed
 *   upon pagination.
 */
private class MastodonPostPaginatorScopeImpl(
  requesterScope: RequesterTestScope<Requester>,
  authenticationLock: SomeAuthenticationLock
) : MastodonPostPaginatorScope(requesterScope, authenticationLock)

/** Specification of a route. */
internal sealed class RouteSpec {
  /**
   * Returns whether the given [route] matches this specification.
   *
   * @param route Route to match against.
   */
  abstract infix fun matches(route: URI): Boolean

  /**
   * Amount of times pagination HTTP requests sent to a route matching this specification have been
   * responded to.
   *
   * @see matches
   */
  abstract fun hitCount(): Int
}

/** Collection of route specifications. */
internal sealed class Routes {
  /** Specification of the route to which the first pagination request is sent. */
  abstract val initial: RouteSpec

  /**
   * Specification of the route to which a pagination request prior to the most recent one has been
   * sent.
   */
  abstract val previous: RouteSpec?

  /** Specification of the route to which the last pagination has been performed. */
  abstract val current: RouteSpec?

  /**
   * Specification of the route to which pagination requests that succeed the initial one are sent.
   */
  abstract val next: RouteSpec?
}

/**
 * Scope in which [MastodonPostPaginator]-specific tests can be run. Alongside the runner method,
 * offers facilities for performing pagination, providing responses to HTTP requests and counting
 * those sent to a specific route, which allows for ensuring behavior correctness of such class.
 *
 * @param authenticationLock [AuthenticationLock] that authenticates the HTTP requests performed
 *   upon pagination.
 * @property requesterScope [RequesterTestScope] in which the [Post]s of each page are shared. Also
 *   acts as a delegate of this class' [CoroutineScope]-like subclassed functionality.
 * @see runMastodonPostPaginatorTest
 */
internal sealed class MastodonPostPaginatorScope(
  private val requesterScope: RequesterTestScope<Requester>,
  authenticationLock: SomeAuthenticationLock
) :
  MastodonPostPaginator<Any>(authenticationLock, requesterScope),
  KTypeCreator<Any> by kTypeCreatorOf(),
  CoroutineScope by requesterScope,
  AutoCloseable {
  /** Routes associated to the amount of requests sent to them. */
  private val requestCounting = hashMapOf<URI, Int>()

  /** Most recent route to which pagination has been performed. */
  private var currentRoute: URI? = null

  /** Route of the page to which the first pagination is performed. */
  private inline val initialRoute
    get() = HostedURLBuilder.from(requester.baseURI).buildInitialRoute()

  /** Route to which a pagination request has been sent prior to the most recent one. */
  private inline val previousRoute: URI?
    get() =
      when (val initialPage = initialPage) {
        0 -> null
        1 -> initialRoute
        else -> createNextRouteAt(initialPage - 1)
      }

  /** Page of the next pagination. */
  private inline val nextPage
    get() = initialPage + 1

  /** Route of the page to which subsequent pagination is performed by default. */
  private inline val nextRoute: URI
    get() = createNextRouteAt(nextPage)

  final override val requester = requesterScope.requester

  /** Route specification collection available from this [MastodonPostPaginatorScope]. */
  val routes: Routes =
    RoutesImpl(
      RouteSpecImpl(::initialRoute, rel = null),
      RouteSpecImpl(::previousRoute, LinkHeader.Rel.Previous),
      RouteSpecImpl(::currentRoute, rel = null),
      RouteSpecImpl(::nextRoute, LinkHeader.Rel.Next)
    )

  /**
   * Single implementation of [RouteSpec].
   *
   * @property route Obtains the specified route as it currently is.
   * @property rel `rel` parameter of the header. If `null`, the [header] is also `null`.
   */
  private inner class RouteSpecImpl(private val route: () -> URI?, override val rel: String?) :
    InternalRouteSpec() {
    override fun route() = route.invoke()

    override fun hitCount() =
      with(requestCounting) { filterKeys(::matches).mapNotNull { (route, _) -> get(route) } }.sum()
  }

  final override fun HostedURLBuilder.buildInitialRoute() = requesterScope.route(this)

  final override fun Any.toPosts() = emptyList<Post>()

  final override suspend fun onWillPaginate(pagination: Pagination) {
    val route = pagination.responseDeferred.await().request.url.toURI()
    currentRoute = route
    requestCounting.compute(route) { _, requestCount -> requestCount?.inc() ?: 1 }
  }

  /**
   * Paginates from the initial page to the given target one, suspending until the pagination
   * finishes.
   *
   * @param page Page until which pagination should be inclusively performed.
   * @throws AssertionError If the backing [Flow] emits less than `countPagesOrThrow(page)` [List]s,
   *   completes abruptly or throws.
   * @throws Pages.InvalidException If the [page] is invalid.
   * @see Pages.validate
   */
  @Throws(AssertionError::class, Pages.InvalidException::class)
  suspend fun paginateToAndAwait(@Page page: Int) {
    paginateTo(page).test {
      repeat(countPagesOrThrow(page)) { awaitItem() }
      awaitComplete()
    }
  }

  override fun close() = requestCounting.clear()

  /**
   * Creates a route that succeeds the initial one.
   *
   * @param page Page for which the route to be created is. It is implied to be a valid one; thus,
   *   it is not validated.
   * @see Pages.validate
   */
  private fun createNextRouteAt(@Page page: Int) =
    HostedURLBuilder.from(requester.baseURI).path("next").query().parameter("page", "$page").build()
}

/**
 * Runs a [MastodonPostPaginator] test.
 *
 * @param onRequest Callback called for each pagination whenever a request is performed, before it
 *   is responded to.
 * @param previous Produces the route to which a request is sent when paginating backwards.
 * @param next Produces the route to which a request is sent when paginating forward.
 * @param body Actual testing on the instantiated [MastodonPostPaginator].
 */
@OptIn(ExperimentalContracts::class)
internal fun runMastodonPostPaginatorTest(
  onRequest: (method: HttpMethod, page: Int, route: URI) -> Unit = { _, _, _ -> },
  previous: Routes.(page: Int) -> RouteSpec? = { this.previous },
  next: Routes.(page: Int) -> RouteSpec? = { this.next },
  body: suspend MastodonPostPaginatorScope.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  lateinit var paginatorScope: MastodonPostPaginatorScopeImpl
  runAuthenticatedRequesterTest({ requestData ->
    val page = paginatorScope.initialPage
    val route = requestData.url.toURI()
    onRequest(requestData.method, page, route)
    respond(
      content = "",
      headers =
        Headers.build {
          with(paginatorScope.routes as RoutesImpl) {
              copy(
                previous = previous(page) as InternalRouteSpec?,
                next = next(page) as InternalRouteSpec?
              )
            }
            .mapValues { (_, routeSpec) -> routeSpec?.header() }
            .forEach { (_, header) -> header?.let { append(HttpHeaders.Link, "$header") } }
        }
    )
  }) { lock ->
    paginatorScope = MastodonPostPaginatorScopeImpl(requesterScope = this, lock)
    paginatorScope.use { paginatorScope -> paginatorScope.body() }
  }
}
