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
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.each
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotZero
import assertk.assertions.isSameAs
import assertk.assertions.isTrue
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
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MastodonNotificationTests {
  private val actor = Actor.Authenticated.sample
  private val createdAtAsZonedDateTime = ZonedDateTime.now()
  private val createdAt = MastodonNotification.createdAt(createdAtAsZonedDateTime)

  @Test
  fun throwsWhenCreatingFromIncompleteMap() {
    assertThat(MastodonNotification.Type.entries).each { typeAssert ->
      typeAssert.given { type ->
        for (element in MastodonNotification.Serializer.elements) {
          assertFailure {
              MastodonNotification.from(
                MastodonNotification(
                    /* id = */ "0",
                    type,
                    createdAt,
                    MastodonAccount.default,
                    MastodonStatus.default
                  )
                  .toMap()
                  .apply { remove(element.name) }
              )
            }
            .isInstanceOf<SerializationException>()
        }
      }
    }
  }

  @Test
  fun throwsWhenCreatingFromMapWithUnknownField() {
    assertThat(MastodonNotification.Type.entries).each { typeAssert ->
      typeAssert.given { type ->
        assertFailure {
            MastodonNotification.from(
              MastodonNotification(
                  /* id = */ "0",
                  type,
                  createdAt,
                  MastodonAccount.default,
                  MastodonStatus.default
                )
                .toMap()
                .apply { put("🌫️", "😕") }
            )
          }
          .isInstanceOf<SerializationException>()
      }
    }
  }

  @Test
  fun createsFromMap() {
    assertThat(MastodonNotification.Type.entries).each { typeAssert ->
      typeAssert.given { type ->
        val notification =
          MastodonNotification(
            /* id = */ "0",
            type,
            createdAt,
            MastodonAccount.default,
            MastodonStatus.default
          )
        assertThat(
            MastodonNotification.from(
              Json.encodeToJsonElement(MastodonNotification.Serializer.instance, notification)
                .jsonObject
                .mapValues { it.value.toString() }
            )
          )
          .isEqualTo(notification)
      }
    }
  }

  @Test
  fun convertsZonedDateTimeIntoCreatedAtString() {
    assertThat(MastodonNotification.createdAt(createdAtAsZonedDateTime)).isEqualTo(createdAt)
  }

  @Test
  fun normalizedIDIsHashCodeOfOriginalOneWhenItIsNotDigitOnly() {
    assertThat(
        MastodonNotification(
          /* id = */ "🪫",
          MastodonNotification.Type.FOLLOW,
          /* createdAt = */ "",
          MastodonAccount.default,
          /* status = */ null
        )
      )
      .prop(MastodonNotification::getNormalizedID)
      .isEqualTo("🪫".hashCode())
  }

  @Test
  fun normalizedIDIsOriginalOneConvertedIntoAnIntegerWhenItIsDigitOnly() {
    assertThat(
        MastodonNotification(
          /* id = */ "8102024",
          MastodonNotification.Type.FOLLOW,
          /* createdAt = */ "",
          MastodonAccount.default,
          /* status = */ null
        )
      )
      .prop(MastodonNotification::getNormalizedID)
      .isEqualTo(8102024)
  }

  @Test
  fun getsContentTitleWhenCoreModuleIsNotRequiredToBeRegistered() {
    runTest {
      assertThat(
          MastodonNotification.Type.entries.filterNot(
            MastodonNotification.Type::isCoreModuleRegistrationUponContentTitleObtainanceRequired
          ),
          "types"
        )
        .each { typeAssert ->
          typeAssert.given { type ->
            val parent =
              MastodonNotification(
                /* id = */ "",
                type,
                createdAt,
                MastodonAccount.default,
                MastodonStatus.default
              )
            type.getContentTitleAsync(context, this, parent).toCompletableFuture().get()
          }
        }
    }
  }

  @Test
  fun throwsWhenCoreModuleIsRequiredToBeRegisteredWhenContentTitleIsObtained() {
    runTest {
      assertThat(
          MastodonNotification.Type.entries.filter(
            MastodonNotification.Type::isCoreModuleRegistrationUponContentTitleObtainanceRequired
          ),
          "types"
        )
        .each { typeAssert ->
          typeAssert.given { type ->
            val parent =
              MastodonNotification(
                /* id = */ "",
                type,
                createdAt,
                MastodonAccount.default,
                MastodonStatus.default
              )
            assertFailsWith<Injector.ModuleNotRegisteredException> {
              type.getContentTitleAsync(context, this, parent).toCompletableFuture().get()
            }
          }
        }
    }
  }

  @Test
  fun convertsIntoMap() {
    assertThat(MastodonNotification.Type.entries).each { typeAssert ->
      typeAssert.given { type ->
        assertThat(
            MastodonNotification(
              /* id = */ "0",
              type,
              createdAt,
              MastodonAccount.default,
              MastodonStatus.default
            )
          )
          .prop(MastodonNotification::toMap)
          .isEqualTo(
            mapOf(
              "id" to "\"0\"",
              "type" to "\"${type.name}\"",
              "createdAt" to "\"$createdAt\"",
              "account" to Json.encodeToJsonElement(MastodonAccount.default).toString(),
              "status" to Json.encodeToJsonElement(MastodonStatus.default).toString()
            )
          )
      }
    }
  }

  @Test
  fun notificationChannelIsThatOfItsType() {
    runTest {
      assertThat(MastodonNotification.Type.entries, "types").each { typeAssert ->
        typeAssert.given { type ->
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
          assertThat(
              MastodonNotification(
                /* id = */ "",
                type,
                createdAt,
                MastodonAccount.default,
                MastodonStatus.default
              )
            )
            .transform("toNotificationAsync") { transformationParent ->
              transformationParent.toNotificationAsync(context, this@runTest)
            }
            .prop(CompletionStage<Notification>::toCompletableFuture)
            .prop(CompletableFuture<Notification>::get)
            .prop(Notification::getChannelId)
            .isSameAs(type.channelID)
          if (type.isCoreModuleRegistrationUponContentTitleObtainanceRequired) {
            Injector.unregister<CoreModule>()
          }
        }
      }
    }
  }

  @Test
  fun notificationIsCancelledAutomatically() {
    runTest {
      assertThat(MastodonNotification.Type.entries, "types").each { typeAssert ->
        typeAssert.given { type ->
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
          assertThat(
              MastodonNotification(
                /* id = */ "",
                type,
                createdAt,
                MastodonAccount.default,
                MastodonStatus.default
              )
            )
            .transform("toNotificationAsync") { transformationParent ->
              transformationParent.toNotificationAsync(context, this@runTest)
            }
            .prop(CompletionStage<Notification>::toCompletableFuture)
            .prop(CompletableFuture<Notification>::get)
            .prop(Notification::flags)
            .transform("is auto-cancel enabled", Notification.FLAG_AUTO_CANCEL::and)
            .isNotZero()
          if (type.isCoreModuleRegistrationUponContentTitleObtainanceRequired) {
            Injector.unregister<CoreModule>()
          }
        }
      }
    }
  }

  @Test
  fun notificationTitleIsThatObtainableThroughItsType() {
    runTest {
      assertThat(MastodonNotification.Type.entries, "types").each { typeAssert ->
        typeAssert.given { type ->
          val mastodonNotification =
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
          assertThat(mastodonNotification)
            .transform("toNotificationAsync") { transformationMastodonNotification ->
              transformationMastodonNotification.toNotificationAsync(context, this@runTest)
            }
            .prop(CompletionStage<Notification>::toCompletableFuture)
            .prop(CompletableFuture<Notification>::get)
            .prop(Notification::extras)
            .transform("getString(Notification.EXTRA_TITLE)") {
              it.getString(Notification.EXTRA_TITLE)
            }
            .isEqualTo(
              type
                .getContentTitleAsync(context, this, mastodonNotification)
                .toCompletableFuture()
                .get()
            )
          if (type.isCoreModuleRegistrationUponContentTitleObtainanceRequired) {
            Injector.unregister<CoreModule>()
          }
        }
      }
    }
  }

  @Test
  fun notificationIsTimestamped() {
    runTest {
      assertThat(MastodonNotification.Type.entries, "types").each { typeAssert ->
        typeAssert.given { type ->
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
          assertThat(
              MastodonNotification(
                /* id = */ "",
                type,
                createdAt,
                MastodonAccount.default,
                MastodonStatus.default
              )
            )
            .transform("toNotificationAsync") { transformationParent ->
              transformationParent.toNotificationAsync(context, this@runTest)
            }
            .prop(CompletionStage<Notification>::toCompletableFuture)
            .prop(CompletableFuture<Notification>::get)
            .all {
              prop(Notification::extras)
                .transform("getBoolean(Notification.EXTRA_SHOW_WHEN)") {
                  it.getBoolean(Notification.EXTRA_SHOW_WHEN)
                }
                .isTrue()
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
