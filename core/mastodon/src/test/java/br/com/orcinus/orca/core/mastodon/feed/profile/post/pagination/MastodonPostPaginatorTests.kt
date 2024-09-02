/*
 * Copyright © 2024 Orcinus
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

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.type.KTypeCreator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.type.kTypeCreatorOf
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.runAuthenticatedRequesterTest
import br.com.orcinus.orca.core.mastodon.instance.requester.runRequesterTest
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondOk
import io.ktor.http.HttpHeaders
import io.ktor.http.LinkHeader
import io.ktor.http.headersOf
import io.ktor.http.toURI
import java.net.URI
import kotlin.test.Test
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take

internal class MastodonPostPaginatorTests {
  @Test(expected = IndexOutOfBoundsException::class)
  fun throwsWhenPaginatingToANegativePage() {
    runAuthenticatedRequesterTest {
      object : MastodonPostPaginator<Unit>(), KTypeCreator<Unit> by kTypeCreatorOf() {
          override val requester = this@runAuthenticatedRequesterTest.requester

          override fun HostedURLBuilder.buildInitialRoute(): URI {
            return route()
          }

          override fun Unit.toPosts(): List<Post> {
            return emptyList()
          }
        }
        .paginateTo(-1)
    }
  }

  @Test
  fun firstRequestIsSentToInitialRoute() {
    lateinit var requestURI: URI
    runRequesterTest({
      requestURI = it.url.toURI()
      respondOk()
    }) {
      lateinit var baseURI: URI
      object : MastodonPostPaginator<Unit>(), KTypeCreator<Unit> by kTypeCreatorOf() {
          override val requester = this@runRequesterTest.requester

          override fun HostedURLBuilder.buildInitialRoute(): URI {
            baseURI = build()
            return path("initial").build()
          }

          override fun Unit.toPosts(): List<Post> {
            return emptyList()
          }
        }
        .paginateTo(0)
        .take(1)
        .collect()
      assertThat(requestURI).isEqualTo(baseURI.resolve("initial"))
    }
  }

  @Test
  fun refreshesInInitialRoute() {
    lateinit var initialURI: URI
    var initialRouteRequestCount = 0
    runRequesterTest({
      val requestURI = it.url.toURI()
      if (requestURI == initialURI) {
        initialRouteRequestCount++
      }
      respondOk()
    }) {
      object : MastodonPostPaginator<Unit>(), KTypeCreator<Unit> by kTypeCreatorOf() {
          override val requester = this@runRequesterTest.requester

          override fun HostedURLBuilder.buildInitialRoute(): URI {
            initialURI = path("initial").build()
            return initialURI
          }

          override fun Unit.toPosts(): List<Post> {
            return emptyList()
          }
        }
        .run { repeat(2) { paginateTo(0).take(1).collect() } }
      assertThat(initialRouteRequestCount).isEqualTo(2)
    }
  }

  @Test(IllegalStateException::class)
  fun throwsWhenPaginatingToNextRouteAndHeaderWithALinkToItIsNotIncludedInThePreviousResponse() {
    runAuthenticatedRequesterTest {
      object : MastodonPostPaginator<Unit>(), KTypeCreator<Unit> by kTypeCreatorOf() {
          override val requester = this@runAuthenticatedRequesterTest.requester

          override fun HostedURLBuilder.buildInitialRoute(): URI {
            return route()
          }

          override fun Unit.toPosts(): List<Post> {
            return emptyList()
          }
        }
        .paginateTo(1)
        .take(1)
        .collect()
    }
  }

  @Test
  fun refreshesInNextRoute() {
    lateinit var initialURI: URI
    var nextRouteRequestCount = 0
    runRequesterTest({
      val requestURI = it.url.toURI()
      val nextURI = HostedURLBuilder.from(initialURI).path("next").build()
      if (requestURI == nextURI) {
        nextRouteRequestCount++
      }
      respond(
        content = "",
        headers = headersOf(HttpHeaders.Link, "${LinkHeader("$nextURI", LinkHeader.Rel.Next)}")
      )
    }) {
      object : MastodonPostPaginator<Unit>(), KTypeCreator<Unit> by kTypeCreatorOf() {
          override val requester = this@runRequesterTest.requester

          override fun HostedURLBuilder.buildInitialRoute(): URI {
            initialURI = route()
            return initialURI
          }

          override fun Unit.toPosts(): List<Post> {
            return emptyList()
          }
        }
        .run { repeat(2) { paginateTo(1).take(1).collect() } }
      assertThat(nextRouteRequestCount).isEqualTo(2)
    }
  }
}
