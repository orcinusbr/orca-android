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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination

import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.type.KTypeCreator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.type.kTypeCreatorOf
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.LinkHeader
import io.ktor.http.toURI
import java.net.URI
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan

/**
 * Requests and paginates through [Post]s.
 *
 * Mastodon API's pagination starts from an initial route and is performed subsequently by sending a
 * `GET` HTTP request to one of the links that are included as headers of the previous response. For
 * more information, refer to the
 * [official documentation](https://docs.joinmastodon.org/api/guidelines/#pagination).
 *
 * An instance of this class also acts as a [KTypeCreator] because of its generic nature: since its
 * type argument cannot be reified (at least not as of Kotlin 1.9.21), the [HttpResponse] needs to
 * know how to actually convert the received payload into a [T]. This tends to result in a somewhat
 * manual process if there are, for example, type arguments that need to be passed in; otherwise,
 * this behavior can simply be delegated to a default [KTypeCreator], obtainable through the
 * [kTypeCreatorOf] creator method.
 *
 * @param T DTO that is returned by the API.
 */
internal abstract class MastodonPostPaginator<T : Any> : KTypeCreator<T> {
  /** [MutableSharedFlow] with the index of the page that's the current one. */
  private val pageFlow =
    MutableSharedFlow<Int>(replay = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST).apply {
      tryEmit(0)
    }

  /** Page at which the [Post]s lastly emitted to the [postsFlow] are. */
  private val currentPage
    get() = pageFlow.replayCache.last()

  /** [Requester] by which requests will be performed. */
  protected abstract val requester: Requester

  /** [Flow] to which the [Post]s within the current page are emitted. */
  private val postsFlow =
    pageFlow
      .scan(null as Pair<Int, HttpResponse>?) { previous, currentPage ->
        currentPage to
          requester
            .authenticated()
            .get({
              previous?.let { (previousPage, previousResponse) ->
                /**
                 * Obtains one of the [URI]s present in the link header of the response to the
                 * previous request.
                 *
                 * @param rel Description of the relationship between the current page and the one
                 *   whose [URI] will be obtained. According to the Mastodon API documentation as of
                 *   v1, can be either `"prev"` or [LinkHeader.Rel.Next].
                 * @throws IllegalStateException If the previous response does not have the related
                 *   link header.
                 */
                @Throws(IllegalStateException::class)
                fun getLinkedURI(rel: String): URI {
                  return previousResponse.headers
                    .filterIsLink()
                    .find { it.parameter(LinkHeader.Parameters.Rel) == rel }
                    ?.uri
                    ?.let(::URI)
                    ?: error("Header with \"$rel\" link was not included in the previous response.")
                }

                /** Obtains the [URI] to which a request would simply perform a refresh. */
                fun getRefreshURI(): URI {
                  return previousResponse.request.url.toURI()
                }

                if (currentPage < previousPage) {
                  getLinkedURI("prev")
                } else if (currentPage == previousPage) {
                  getRefreshURI()
                } else {
                  getLinkedURI(LinkHeader.Rel.Next)
                }
              }
                ?: buildInitialRoute()
            })
      }
      .drop(1)
      .filterNotNull()
      .map { (_, response) -> response.body(this).toPosts() }
      .buffer(Channel.RENDEZVOUS)

  /**
   * Iterates from the current page to the given one.
   *
   * @param page Page until which pagination should be performed.
   * @throws IndexOutOfBoundsException If the [page] is negative.
   */
  @Throws(IndexOutOfBoundsException::class)
  suspend fun paginateTo(page: Int): Flow<List<Post>> {
    if (page < 0) {
      throw IndexOutOfBoundsException("Cannot paginate to a negative page ($page).")
    }
    while (page < currentPage) {
      val previousPage = currentPage.dec()
      pageFlow.emit(previousPage)
    }
    while (page > currentPage) {
      val nextPage = currentPage.inc()
      pageFlow.emit(nextPage)
    }
    return postsFlow
  }

  /** Creates an [URI] to which the initial request should be sent. */
  protected abstract fun HostedURLBuilder.buildInitialRoute(): URI

  /** Converts the DTO returned by the API into [Post]s. */
  protected abstract fun T.toPosts(): List<Post>
}
