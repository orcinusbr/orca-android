/*
 * Copyright © 2023-2024 Orcinus
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
import br.com.orcinus.orca.core.mastodon.instance.SomeMastodonInstance
import br.com.orcinus.orca.core.mastodon.network.requester.client.MastodonClient
import br.com.orcinus.orca.core.mastodon.network.requester.client.authenticateAndGet
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.ext.coroutines.getValue
import br.com.orcinus.orca.ext.coroutines.setValue
import br.com.orcinus.orca.std.injector.Injector
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponse
import kotlin.jvm.optionals.getOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

/**
 * Requests and paginates through [Post]s.
 *
 * One noticeable fact is that a [MastodonPostPaginator] is also a [KTypeCreator]. Such inheritance
 * is necessary because of its generic nature: since its type argument cannot be reified (at least
 * not as of Kotlin 1.9.21), the [HttpResponse] needs to know how to actually convert the received
 * payload into a [T]. Tends to result in a somewhat manual process if there are, for example, type
 * arguments that need to be passed in; otherwise, this behavior can simply be delegated to a
 * default [KTypeCreator], obtainable through the [kTypeCreatorOf] creator method.
 *
 * @param T DTO that is returned by the API.
 */
internal abstract class MastodonPostPaginator<T : Any> : KTypeCreator<T> {
  /** Last [HttpResponse] that's been received. */
  private var lastResponse: HttpResponse? = null

  /** [MutableStateFlow] with the index of the page that's the current one. */
  private var pageFlow = MutableStateFlow(0)

  /**
   * [Flow] to which the [Post]s within the current page are emitted.
   *
   * @see page
   */
  private val postsFlow =
    pageFlow
      .compareNotNull { previous, current -> previous.getOrNull()?.compareTo(current) ?: 0 }
      .map { it == 0 }
      .associateWith { lastResponse?.headers?.links?.firstOrNull()?.uri }
      .map({ (uri, isRefreshing) -> isRefreshing || uri == null }) { route to it.second }
      .mapNotNull { (uri, _) -> uri?.let { client.authenticateAndGet(it) } }
      .onEach { lastResponse = it }
      .map { it.body(this).toPosts() }

  /** [MastodonClient] through which the [HttpRequest]s will be performed. */
  private val client
    get() =
      (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance).client

  /**
   * Index of the page that's the current one.
   *
   * @see pageFlow
   */
  private var page by pageFlow

  /** URI [String] to which the initial [HttpRequest] should be sent. */
  protected abstract val route: String

  /**
   * Iterates from the current page to the given one.
   *
   * @param page Page until which pagination should be performed.
   * @return [Flow] that receives the [Post]s of all the pages through which we've been through in
   *   the pagination process.
   * @throws IllegalArgumentException If the given page is before the current one.
   */
  @Throws(IllegalArgumentException::class)
  fun paginateTo(page: Int): Flow<List<Post>> {
    iterate(page)
    return postsFlow
  }

  /** Converts the DTO returned by the API into [Post]s. */
  protected abstract fun T.toPosts(): List<Post>

  /**
   * Goes through each page between the current and the given one.
   *
   * @param page Destination page.
   * @throws IllegalArgumentException If the given [page] is before the current one.
   */
  @Throws(IllegalArgumentException::class)
  private fun iterate(page: Int) {
    if (page < this.page) {
      throw IllegalArgumentException(
        "Cannot iterate backwards (current page is ${this.page} and the given one is $page)."
      )
    }
    while (page > this.page) {
      this.page++
    }
  }
}
