/*
 * Copyright © 2023–2025 Orcinus
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

import androidx.annotation.EmptySuper
import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.page.Page
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.page.Pages
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.type.KTypeCreator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.type.kTypeCreatorOf
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.std.markdown.style.`if`
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.LinkHeader
import io.ktor.http.toURI
import java.net.URI
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

/** `rel` parameter of the route to the previous page linked in the header of a response. */
internal inline val @Suppress("UnusedReceiverParameter") LinkHeader.Rel.Previous
  get() = "prev"

/**
 * Paginates through [Post]s.
 *
 * Mastodon API pagination starts from an initial route and is performed subsequently by sending a
 * `GET` HTTP request to one of the links that are included as headers of the previous response. For
 * more information, refer to the
 * [official documentation](https://docs.joinmastodon.org/api/guidelines/#pagination).
 *
 * ##### Definition of page
 *
 * In the documentation and throughout the source, "page" is simply denoted as a natural [Int]
 * index. The term does not refer to a [List] of [Post]s, but to such index through which such a
 * [List] can be fetched via an HTTP request to the Mastodon API.
 *
 * ##### Definition of pagination
 *
 * Conceptually, pagination is p₀ = 0, p₀ → λⁿ pₙ — in either increasing or decreasing order, where
 * p₀, pₙ ∈ ℕ, with p₀ as the initial page and pₙ as the target one. Each iteration emits the
 * [Post]s in the respective page to a [Flow] when and while it is collected.
 *
 * ##### Coldness
 *
 * A call to [paginateTo] does not paginate; rather, it merely returns the [Flow] to which the
 * [Post]s in each page will be emitted _when_ and _while_ collected, _scheduling_ a pagination
 * operation based on the target page. After the initial collection, all pagination requests are
 * executed while collection is taking place.
 *
 * This behavior prevents performing pagination when there is no one listening to the emitted
 * [Post]s (that is: no [FlowCollector]s collecting the returned [Flow]), which would, otherwise,
 * result in missed pages; thus, incomplete emissions; and potential waste of resources by HTTP
 * requests whose responses are unconsumed.
 *
 * E. g.,
 * ```kotlin
 * paginateTo(1)
 * println("Requested, but not collected.")
 * var page = 0
 * paginateTo(2).collect { posts: List<Post> -> println("Collected at page ${page++}: $posts") }
 * ```
 *
 * When requesting for the first time (`paginateTo(1)`), the returned [Flow] is not collected. If it
 * was hot instead of cold, pagination would have been performed right away, and the [Flow] returned
 * by the second request (`paginateTo(2)`) would only emit the [Post]s in page 2, given that the
 * current page would already be 1 due to the previous preemptive pagination. Running it would,
 * then, result in the printing of
 *
 * ```
 * Requested, but not collected.
 * Collected at page 0: […]
 * ```
 *
 * (which is also misleading: the page of the collected [Post]s is not 0, but 2).
 *
 * With it being cold, however, it results in
 *
 * ```
 * Requested, but not collected.
 * Collected at page 0: […]
 * Collected at page 1: […]
 * Collected at page 2: […]
 * ```
 *
 * To subsequent [FlowCollector]s, the [Post]s in the current page (2) and all other ones in pages
 * to which pagination is further requested are emitted.
 *
 * ##### DTO-to-[Post]s conversions
 *
 * An instance of this class also acts as a [KTypeCreator] because of its generic nature: since its
 * type argument [T] cannot be reified (at least not as of Kotlin 2.0.0), the [HttpResponse] needs
 * to know how to actually convert the received payload into a [T]. This tends to result in a
 * somewhat manual process if there are, for example, type arguments that need to be passed in;
 * otherwise, this behavior can simply be delegated to a default [KTypeCreator], obtainable through
 * the [kTypeCreatorOf] factory method.
 *
 * @param T DTO that is returned by the API.
 * @property coroutineScope [CoroutineScope] in which paginations are performed and their respective
 *   HTTP requests for fetching the [Post]s in the page are sent.
 * @see buildInitialRoute
 */
internal abstract class MastodonPostPaginator<T : Any>(private val coroutineScope: CoroutineScope) :
  KTypeCreator<T> {
  /** [Requester] by which HTTP requests that require authentication can be performed. */
  private val authenticatedRequester by lazy { requester.authenticated() }

  /**
   * Workaround for the inability to reference the initial route builder method which is, at the
   * same time, a member and an extension. Since it does not depend on the response to a previous
   * request, allows for the instantiation of a single router instead of one for each
   * [FlowCollector] of [postsFlow].
   *
   * @see HostedURLBuilder.buildInitialRoute
   * @see createSubsequentRouter
   */
  private val initialRouter: HostedURLBuilder.() -> URI = { buildInitialRoute() }

  /**
   * Receives each page sent upon pagination.
   *
   * A rendezvous [Channel] is preferred over a [SharedFlow] because the latter employs conflation,
   * preventing slow collections from receiving all emitted values and allowing them only to receive
   * the latest ones. This is an issue specifically for pagination in which pₙ ≠ p₀, since each page
   * in the range is sent immediately, one after another.
   */
  private val pageChannel = Channel<Int>()

  /** Page set upon pagination; essentially, p₀. */
  private var currentPage by atomic(Pages.NONE)

  /** [Flow] to which [Post]s obtained from pagination are emitted. */
  private val postsFlow =
    pageChannel
      .receiveAsFlow()
      .onEach(Pages::validate)
      .catch { cause -> cause.takeUnless { it is Pages.InvalidException }?.let { throw it } }
      .runningFold<_, Pagination?>(null) { previousPagination, page ->
        @Suppress("UNCHECKED_CAST")
        val router =
          previousPagination
            ?.let { createSubsequentRouter(previousPagination, page) }
            .`if`({ this === Unrouted }) {
              return@runningFold null
            } as (HostedURLBuilder.() -> URI)?
            ?: initialRouter

        Pagination(page, coroutineScope.async { authenticatedRequester.post(router) })
      }
      .filterNotNull()
      .onEach(::onWillPaginate)
      .map { it.responseDeferred.await().body(this@MastodonPostPaginator).toPosts() }
      .shareIn(coroutineScope + Job(), SharingStarted.Eagerly)

  /** [Requester] by which requests will be performed. */
  protected abstract val requester: Requester

  /** Page from which the next pagination will start. */
  @VisibleForTesting
  inline val initialPage
    @Page get() = if (currentPage == Pages.NONE) 0 else currentPage

  /**
   * Denotes that no route was linked in the previous response to a request for obtaining posts in a
   * given page. Allows, primarily, for distinguishing between whether the initial route should be
   * returned or that nothing be done due to the absence of such linked route.
   *
   * @see initialRouter
   * @see createSubsequentRouter
   */
  @Suppress("RemoveEmptyClassBody") private object Unrouted {}

  /** [MastodonPostPaginator] that shares [Post]s emitted upon pagination in the [GlobalScope]. */
  @DelicateCoroutinesApi
  @Deprecated(
    "Specify the coroutine scope in which the flow containing the paginated posts is shared."
  )
  constructor() : this(GlobalScope)

  init {
    @OptIn(InternalCoroutinesApi::class)
    coroutineScope.coroutineContext.job.invokeOnCompletion(onCancelling = true) {
      pageChannel.close(it)
      currentPage = Pages.NONE
    }
  }

  /**
   * Requests an iteration from the initial page to the given one.
   *
   * @param page Page to which pagination should be inclusively performed.
   * @throws Pages.InvalidException If the [page] is invalid.
   * @see Pages.validate
   */
  @Throws(Pages.InvalidException::class)
  fun paginateTo(@Page page: Int): Flow<List<Post>> {
    Pages.validate(page)
    return channelFlow {
      val initialPage = this@MastodonPostPaginator.initialPage
      val pages = if (initialPage <= page) initialPage..page else initialPage downTo page
      val pageCount = countPages(initialPage, targetPage = pages.last)
      coroutineScope {
        launch { postsFlow.take(pageCount).collect(::send) }
        launch { pages.forEach { setPage(it) } }
      }
    }
  }

  /**
   * Returns the amount of pages included in the given range.
   *
   * @param targetPage Last page in the range (inclusive).
   * @throws Pages.InvalidException If the [targetPage] is invalid.
   * @see Pages.validate
   */
  @Throws(Pages.InvalidException::class)
  @VisibleForTesting
  fun countPagesOrThrow(@Page targetPage: Int): Int {
    Pages.validate(targetPage)
    return countPages(initialPage, targetPage)
  }

  /**
   * Returns the amount of pages included in the given range.
   *
   * @param initialPage Page from which pagination would start (inclusive).
   * @param targetPage Last page in the range (inclusive).
   * @throws Pages.InvalidException If either page is invalid.
   * @see Pages.validate
   */
  @Throws(Pages.InvalidException::class)
  @VisibleForTesting
  fun countPagesOrThrow(@Page initialPage: Int, @Page targetPage: Int): Int {
    Pages.validate(initialPage)
    Pages.validate(targetPage)
    return countPages(initialPage, targetPage)
  }

  /** Creates an [URI] to which the initial request should be sent. */
  protected abstract fun HostedURLBuilder.buildInitialRoute(): URI

  /** Converts the DTO returned by the API into [Post]s. */
  protected abstract fun T.toPosts(): List<Post>

  /**
   * Callback called whenever pagination will be performed.
   *
   * @param pagination Metadata of the pagination to be performed.
   */
  @EmptySuper protected open suspend fun onWillPaginate(pagination: Pagination) = Unit

  /**
   * Produces a router of a route of a page preceded by another one. The returned value might be
   * [Unrouted], which would denote either that the final page has been reached or that the Mastodon
   * API erroneously did not provide the previous/next route — with the first scenario being the
   * most empirically common.
   *
   * @param previousPagination Determines, alongside the [currentPage], the router that will be
   *   returned for providing the [URI] (one of the linked ones, that of the previous response when
   *   a refresh has been requested or neither) to `GET` to next.
   * @param currentPage Page at which which we currently are, to be compared with the previous one.
   *   Passing it in as a parameter rather than calling the getter of the instance variable (whose
   *   value is atomic) prevents unnecessary synchronization.
   * @see initialRouter
   */
  private suspend fun createSubsequentRouter(
    previousPagination: Pagination,
    currentPage: Int
  ): Any {
    /**
     * Produces a router based on one of the [URI]s linked in the response to the previous request.
     * May be [Unrouted], which would indicate that either the final page has been reached or
     * Mastodon has erroneously _not_ provided the previous/next route (although the latter seems
     * empirically uncommon).
     *
     * @param rel Description of the relationship between the current page and the one whose router
     *   will be obtained. According to the Mastodon API documentation as of v1, can be either
     *   [LinkHeader.Rel.Previous] or [LinkHeader.Rel.Next].
     */
    suspend fun createLinkedRouter(rel: String) =
      previousPagination.responseDeferred
        .await()
        .headers
        .filterIsLink()
        .find { it.parameter(LinkHeader.Parameters.Rel) == rel }
        ?.uri
        ?.let<_, HostedURLBuilder.() -> URI> { { URI(it) } }
        ?: Unrouted

    /** Produces a router for performing a refresh. */
    suspend fun createRefreshRouter(): HostedURLBuilder.() -> URI {
      val response = previousPagination.responseDeferred.await()
      return { response.request.url.toURI() }
    }

    return if (currentPage < previousPagination.page) {
      createLinkedRouter(LinkHeader.Rel.Previous)
    } else if (currentPage == previousPagination.page) {
      createRefreshRouter()
    } else {
      createLinkedRouter(LinkHeader.Rel.Next)
    }
  }

  /**
   * Sets [page] as the current one by sending it to the [Channel] and changing the atomic property.
   *
   * @param page Page to be set.
   * @see currentPage
   * @see pageChannel
   */
  private suspend fun setPage(@Page page: Int) {
    pageChannel.send(page)
    currentPage = page
  }

  /**
   * Returns the amount of pages included in the given range. Differs from the `countPagesOrThrow`
   * methods in that no validations are performed: because [paginateTo] passes in an always-valid
   * [initialPage] and validates the target one by itself, it is presupposed that the pages are
   * natural [Int]s and, thus, no [Pages.InvalidException]s are thrown.
   *
   * @param initialPage Page from which pagination would start (inclusive).
   * @param targetPage Last page in the range (inclusive).
   * @see Pages.validate
   */
  private fun countPages(@Page initialPage: Int, @Page targetPage: Int) =
    if (initialPage == targetPage) 1 else initialPage + targetPage + 1
}
