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

package br.com.orcinus.orca.core.mastodon.notification

import android.app.Notification
import assertk.all
import assertk.assertThat
import assertk.assertions.each
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.MastodonCoreModule
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import br.com.orcinus.orca.core.mastodon.instance.SampleMastodonInstanceProvider
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.sample.auth.actor.SampleActorProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.content.SampleTermMuter
import br.com.orcinus.orca.core.test.auth.AuthenticationLock
import br.com.orcinus.orca.core.test.auth.Authenticator
import br.com.orcinus.orca.core.test.auth.AuthorizerBuilder
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.injection.immediateInjectionOf
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MastodonNotificationExtensionsTests {
  @Test
  fun convertsIntoNotification() {
    val actor = Actor.Authenticated.sample
    val createdAtAsZonedDateTime = ZonedDateTime.now()
    val createdAt = createdAtAsZonedDateTime.format(DateTimeFormatter.ISO_INSTANT)
    runTest {
      assertThat(MastodonNotification.Type.entries, "types").each { typeAssert ->
        typeAssert.given { type ->
          val parent =
            MastodonNotification(
              /* id = */ "",
              type,
              createdAt,
              MastodonAccount.default,
              MastodonStatus.default
            )
          if (type.isCoreModuleRegistrationUponContentTitleObtainanceRequired) {
            val authorizer = AuthorizerBuilder().build()
            val actorProvider = SampleActorProvider()
            val authenticator = Authenticator(actorProvider, authorizer) { actor }
            val authenticationLock = AuthenticationLock(authenticator, actorProvider)
            val module =
              MastodonCoreModule(
                lazyInjectionOf {
                  SampleMastodonInstanceProvider(authorizer, authenticator, authenticationLock)
                },
                immediateInjectionOf(authenticationLock),
                lazyInjectionOf { SampleTermMuter() }
              )
            Injector.register<CoreModule>(module)
          }
          assertThat(parent)
            .transform("toNotification") { runBlocking { parent.toNotification(context) } }
            .all {
              prop(Notification::extras)
                .transform("getString(Notification.EXTRA_TITLE)") { notification ->
                  notification.getString(Notification.EXTRA_TITLE)
                }
                .isEqualTo(
                  type
                    .getContentTitleAsync(context, this@runTest, parent)
                    .toCompletableFuture()
                    .get()
                )
              prop(Notification::`when`)
                .isEqualTo(createdAtAsZonedDateTime.toInstant().toEpochMilli())
            }
          if (type.isCoreModuleRegistrationUponContentTitleObtainanceRequired) {
            Injector.unregister<CoreModule>()
          }
        }
      }
    }
  }
}
