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
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isZero
import assertk.assertions.prop
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.page.Pages
import java.net.URI
import kotlin.test.Test

internal class MastodonPostPaginatorScopeTests {
  @Test
  fun runsBodyOnce() {
    var bodyRunCount = 0
    runMastodonPostPaginatorTest { bodyRunCount++ }
    assertThat(bodyRunCount, name = "bodyRunCount").isEqualTo(1)
  }

  @Test
  fun paginatesAndAwaits() = runMastodonPostPaginatorTest {
    paginateToAndAwait(2_048)
    assertThat(initialPage).isEqualTo(2_048)
  }

  @Test
  fun callsCallbackOnEachRequest() {
    var responseCount = 0
    runMastodonPostPaginatorTest({ _, _, _ -> responseCount++ }) {
      paginateToAndAwait(256)
      assertThat(responseCount, name = "responseCount").isEqualTo(countPagesOrThrow(0, 256))
    }
  }

  @Test
  fun decreasesPageQueryParameterOnPreviousRouteByDefault() {
    val targetPage = 256
    var isPaginatingBackwards = false
    var lastBackwardsPage = Pages.NONE
    runMastodonPostPaginatorTest({ _, page, route ->
      if (lastBackwardsPage != Pages.NONE && lastBackwardsPage != 1) {
        assertThat(route, name = "route")
          .prop(URI::getQuery)
          .isNotNull()
          .transform("split") { query -> query.split('=') }
          .prop(List<String>::last)
          .prop(String::toInt)
          .isEqualTo(lastBackwardsPage - 1)
      }
      if (isPaginatingBackwards) {
        lastBackwardsPage = page
      }
    }) {
      val pageCount = countPagesOrThrow(targetPage)
      paginateTo(targetPage).test {
        repeat(pageCount) { awaitItemOrThrowCause() }
        awaitComplete()
      }
      paginateTo(0).test {
        isPaginatingBackwards = true
        repeat(pageCount) { awaitItemOrThrowCause() }
        awaitComplete()
      }
    }
  }

  @Test
  fun increasesPageQueryParameterOnNextRouteByDefault() {
    runMastodonPostPaginatorTest({ _, page, route ->
      if (page > 0) {
        assertThat(route, name = "route")
          .prop(URI::getQuery)
          .isNotNull()
          .transform("split") { query -> query.split('=') }
          .prop(List<String>::last)
          .prop(String::toInt)
          .isEqualTo(page)
      }
    }) {
      paginateTo(2).test {
        repeat(countPagesOrThrow(2)) { awaitItemOrThrowCause() }
        awaitComplete()
      }
    }
  }

  @Test
  fun initialRouteHasNoHitsByDefault() = runMastodonPostPaginatorTest {
    assertThat(::routes).prop(Routes::initial).prop(RouteSpec::hitCount).isZero()
  }

  @Test
  fun previousRouteHasNoHitsByDefault() = runMastodonPostPaginatorTest {
    assertThat(::routes).prop(Routes::previous).isNotNull().prop(RouteSpec::hitCount).isZero()
  }

  @Test
  fun nextRouteHasNoHitsByDefault() = runMastodonPostPaginatorTest {
    assertThat(::routes).prop(Routes::next).isNotNull().prop(RouteSpec::hitCount).isZero()
  }

  @Test
  fun countsResponses() = runMastodonPostPaginatorTest {
    repeat(256) { paginateToAndAwait(0) }
    assertThat(::routes).prop(Routes::initial).prop(RouteSpec::hitCount).isEqualTo(256)
    repeat(256) { paginateToAndAwait(1) }
    assertThat(::routes).prop(Routes::current).isNotNull().prop(RouteSpec::hitCount).isEqualTo(256)
  }
}
