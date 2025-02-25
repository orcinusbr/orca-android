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

package br.com.orcinus.orca.core.mastodon.instance.requester.resumption

import assertk.all
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotSameInstanceAs
import assertk.assertions.isSameInstanceAs
import assertk.assertions.prop
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.RequesterTestScope
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.memory.InMemoryRequestDao
import br.com.orcinus.orca.core.mastodon.instance.requester.runRequesterTest
import br.com.orcinus.orca.std.injector.module.Module
import io.ktor.client.request.forms.formData
import kotlin.test.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlinx.coroutines.delay

internal class ResumableRequesterTests {
  @Test
  fun returnsADistinctRequesterWhenConvertedWhileAlreadyBeingAResumableOneAndWithADistinctElapsedTimeProviderAndTheSameRequestDao() =
    runRequesterTest {
      val requestDao = InMemoryRequestDao()
      val firstElapsedTimeProvider = ResumableRequester.ElapsedTimeProvider(Duration::ZERO)
      val resumableRequester = requester.resumable(firstElapsedTimeProvider, requestDao)
      val anotherElapsedTimeProvider = ResumableRequester.ElapsedTimeProvider(Duration::ZERO)
      assertThat(this)
        .prop(RequesterTestScope<Requester>::requester)
        .transform("resumable") { it.resumable(anotherElapsedTimeProvider, requestDao) }
        .isNotSameInstanceAs(resumableRequester)
    }

  @Test
  fun returnsADistinctRequesterWhenConvertedWhileAlreadyBeingAResumableOneAndWithTheSameElapsedTimeProviderAndADistinctRequestDao() =
    runRequesterTest {
      val firstRequestDao = InMemoryRequestDao()
      val elapsedTimeProvider = ResumableRequester.ElapsedTimeProvider(Duration::ZERO)
      val resumableRequester = requester.resumable(elapsedTimeProvider, firstRequestDao)
      val anotherRequestDao = InMemoryRequestDao()
      assertThat(this)
        .prop(RequesterTestScope<Requester>::requester)
        .transform("resumable") { it.resumable(elapsedTimeProvider, anotherRequestDao) }
        .isNotSameInstanceAs(resumableRequester)
    }

  @Test
  fun returnsTheSameRequesterWhenConvertedWhileAlreadyBeingAResumableOneAndWithTheSameElapsedTimeProviderAndRequestDao() =
    runRequesterTest {
      val elapsedTimeProvider = ResumableRequester.ElapsedTimeProvider(Duration::ZERO)
      val requestDao = InMemoryRequestDao()
      assertThat(this)
        .prop(RequesterTestScope<Requester>::requester)
        .transform("resumable") { it.resumable(elapsedTimeProvider, requestDao) }
        .all {
          given { firstRequester ->
            transform("resumable") { secondRequester ->
                secondRequester.resumable(elapsedTimeProvider, requestDao)
              }
              .isSameInstanceAs(firstRequester)
          }
        }
    }

  @Test
  fun throwsWhenConvertingARequesterIntoAResumableOneWithoutHavingInjectedAContextGlobally() {
    lateinit var requester: Requester
    runRequesterTest { requester = this.requester }
    assertFailure { requester.resumable() }.isInstanceOf<Module.DependencyNotInjectedException>()
  }

  @Test
  fun resumesResumableDeleteRequestWhenItIsInterrupted() {
    assertThat(resumptionCountOf { requester.delete(route) }).isEqualTo(1)
  }

  @Test
  fun resumesResumableGetRequestWhenItIsInterrupted() {
    assertThat(resumptionCountOf { requester.get(route) }).isEqualTo(1)
  }

  @Test
  fun resumesResumablePostRequestWithBodyParametersWhenItIsInterrupted() {
    assertThat(resumptionCountOf { requester.post(route) }).isEqualTo(1)
  }

  @Test
  fun resumesResumablePostRequestWithHeaderParametersWhenItIsInterrupted() {
    assertThat(resumptionCountOf { requester.post(route, formData()) }).isEqualTo(1)
  }

  @Test
  fun reusesDeleteRequest() {
    assertThat(
        responseCountOf {
          requester.delete(route)
          requester.delete(route)
        }
      )
      .isEqualTo(1)
  }

  @Test
  fun reusesGetRequest() {
    assertThat(
        responseCountOf {
          requester.get(route)
          requester.get(route)
        }
      )
      .isEqualTo(1)
  }

  @Test
  fun reusesPostRequest() {
    assertThat(
        responseCountOf {
          requester.post(route)
          requester.post(route)
        }
      )
      .isEqualTo(1)
  }

  @Test
  fun stalesGetRequestAfterItsTimeToLiveHasPassed() {
    assertThat(
        responseCountOf {
          requester.get(route)
          delay(ResumableRequester.timeToLive + 1.nanoseconds)
          requester.get(route)
        }
      )
      .isEqualTo(2)
  }

  @Test
  fun stalesDeleteRequestAfterItsTimeToLiveHasPassed() {
    assertThat(
        responseCountOf {
          requester.delete(route)
          delay(ResumableRequester.timeToLive + 1.nanoseconds)
          requester.delete(route)
        }
      )
      .isEqualTo(2)
  }

  @Test
  fun stalesPostRequestAfterItsTimeToLiveHasPassed() {
    assertThat(
        responseCountOf {
          requester.post(route)
          delay(ResumableRequester.timeToLive + 1.nanoseconds)
          requester.post(route)
        }
      )
      .isEqualTo(2)
  }
}
