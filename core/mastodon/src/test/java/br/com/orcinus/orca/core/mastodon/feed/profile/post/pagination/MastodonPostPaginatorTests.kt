/*
 * Copyright © 2024–2025 Orcinus
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
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.cause
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.prop
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.page.Pages
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.type.KTypeCreator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.type.kTypeCreatorOf
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.runAuthenticatedRequesterTest
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.sample.feed.profile.post.content.SampleTermMuter
import br.com.orcinus.orca.core.sample.instance.SampleInstanceProvider
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf
import io.ktor.http.HttpMethod
import java.net.URI
import kotlin.test.Test
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.DelicateCoroutinesApi

internal class MastodonPostPaginatorTests {
  @Test
  fun throwsWhenCountingPagesFromInvalidPage() = runMastodonPostPaginatorTest {
    assertFailure { countPagesOrThrow(Pages.NONE, 0) }.isInstanceOf<Pages.InvalidException>()
  }

  @Test
  fun throwsWhenCountingPagesToInvalidPage() = runMastodonPostPaginatorTest {
    assertFailure { countPagesOrThrow(Pages.NONE) }.isInstanceOf<Pages.InvalidException>()
  }

  @Test
  fun countsPages() = runMastodonPostPaginatorTest {
    assertThat(this).transform("countPages") { it.countPagesOrThrow(2) }.isEqualTo(3)
  }

  @Test
  fun throwsWhenPaginatingToInvalidPage() = runMastodonPostPaginatorTest {
    assertFailure { paginateTo(Pages.NONE) }.isInstanceOf<Pages.InvalidException>()
  }

  @Test
  fun sendsAGetRequestUponPagination() =
    runMastodonPostPaginatorTest({ method, _, _ -> assertThat(method).isEqualTo(HttpMethod.Get) }) {
      paginateToAndAwait(0)
    }

  @Test
  fun paginatesToInitialRoute() {
    lateinit var requestRoute: URI
    runMastodonPostPaginatorTest({ _, _, route -> requestRoute = route }) {
      paginateToAndAwait(0)
      assertThat(::routes).prop(Routes::initial).transform("matches") { it matches requestRoute }
    }
  }

  @Test
  fun refreshesInInitialRoute() = runMastodonPostPaginatorTest {
    repeat(2) { paginateToAndAwait(0) }
    assertThat(::routes).prop(Routes::initial).prop(RouteSpec::hitCount).isEqualTo(2)
  }

  @Test
  fun refreshesInPreviousRoute() = runMastodonPostPaginatorTest {
    paginateToAndAwait(1)
    repeat(2) { paginateToAndAwait(0) }
    assertThat(::routes).prop(Routes::current).isNotNull().prop(RouteSpec::hitCount).isEqualTo(3)
  }

  @Test
  fun refreshesInNextRoute() = runMastodonPostPaginatorTest {
    repeat(2) { paginateToAndAwait(1) }
    assertThat(::routes).prop(Routes::current).isNotNull().prop(RouteSpec::hitCount).isEqualTo(2)
  }

  @Test
  fun doesNotPaginateBackwardsWhenPreviousRouteIsNotLinked() =
    runMastodonPostPaginatorTest(previous = { page -> if (page < 1) previous else null }) {
      paginateToAndAwait(1)
      paginateTo(0).test {
        awaitItem()
        assertFailure { awaitItem() }.cause().isNotNull().isInstanceOf<CancellationException>()
      }
    }

  @Test
  fun doesNotPaginateForwardsWhenNextRouteIsNotLinked() =
    runMastodonPostPaginatorTest(next = { null }) {
      paginateTo(1).test {
        awaitItem()
        assertFailure { awaitItem() }.cause().isNotNull().isInstanceOf<CancellationException>()
      }
    }

  @Test
  fun canPaginateInTheGlobalScope() = runAuthenticatedRequesterTest {
    Injector.register(
      CoreModule(
        lazyInjectionOf { SampleInstanceProvider(NoOpSampleImageLoader.Provider) },
        lazyInjectionOf { it },
        lazyInjectionOf { SampleTermMuter() }
      )
    )

    @OptIn(DelicateCoroutinesApi::class) @Suppress("DEPRECATION")
    object : MastodonPostPaginator<Any>(), KTypeCreator<Any> by kTypeCreatorOf() {
        override val requester = this@runAuthenticatedRequesterTest.requester

        override fun HostedURLBuilder.buildInitialRoute() = build()

        override fun Any.toPosts() = emptyList<Post>()
      }
      .paginateTo(0)
      .test {
        awaitItem()
        awaitComplete()
      }

    Injector.unregister<CoreModule>()
  }
}
