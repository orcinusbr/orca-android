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

import android.app.Application
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Intent
import android.security.keystore.KeyGenParameterSpec
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.extracting
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import assertk.assertions.prop
import assertk.assertions.single
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.notification.service.NotificationService
import br.com.orcinus.orca.core.mastodon.notification.service.NotificationServiceTestScope
import br.com.orcinus.orca.core.mastodon.notification.service.OnMessageReceiptListener
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.injector.Injector
import com.google.common.collect.ImmutableList
import io.mockk.clearMocks
import io.mockk.clearStaticMockk
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.SecretKeySpec
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowApplication
import org.unifiedpush.android.connector.FailedReason
import org.unifiedpush.android.connector.UnifiedPush
import org.unifiedpush.android.connector.data.PushEndpoint
import org.unifiedpush.android.connector.data.PushMessage

@RunWith(RobolectricTestRunner::class)
internal class NotificationReceiverTests {
  private val receiver = NotificationReceiver()
  private val endpoint = PushEndpoint(NotificationServiceTestScope.endpoint, pubKeySet = null)
  private val keyStore = mockk<KeyStore>()
  private val keyGenerator = mockk<KeyGenerator>()

  @BeforeTest
  fun setUp() {
    mockkStatic(KeyGenerator::class, KeyStore::class, UnifiedPush::class)
    every { keyStore.load(/* param = */ null) } returns Unit
    every { KeyStore.getInstance("AndroidKeyStore") } returns keyStore
    every { keyGenerator.init(any<KeyGenParameterSpec>()) } returns Unit
    every { keyGenerator.generateKey() } returns
      (SecretKeyFactory.getInstance("AES") as SecretKeyFactory).generateSecret(
        SecretKeySpec(ByteArray(size = 16, Int::toByte), "AES/GCM/NoPadding")
      ) as SecretKey
    every { KeyGenerator.getInstance("AES", "AndroidKeyStore") } returns keyGenerator
    every { UnifiedPush.getDistributors(context) } returns listOf("orca")
    NotificationReceiver.register(context, receiver, Actor.Authenticated.sample)
  }

  @Test
  fun registers() =
    // Needless for the receiver to be registered here, as it already has been in the setup.
    assertThatShadowApplication()
      .prop<_, ImmutableList<ShadowApplication.Wrapper>, _>(
        ShadowApplication::getRegisteredReceivers
      )
      .extracting<_, BroadcastReceiver>(ShadowApplication.Wrapper::getBroadcastReceiver)
      .contains(receiver)

  @Test
  fun setsUpUnifiedPush() =
    assertThat(UnifiedPush)
      .transform("getSavedDistributor") { it.getSavedDistributor(context) }
      .isEqualTo("orca")

  @Test
  fun stopsBoundServicesWhenRegistrationFails() {
    receiver.onNewEndpoint(context, endpoint, instance = "")
    receiver.onRegistrationFailed(context, FailedReason.INTERNAL_ERROR, instance = "")
    assertThat<NotificationService>().isLastlyStopped()
  }

  @Test
  fun bindsServiceWhenANewEndpointIsReceived() {
    receiver.onNewEndpoint(context, endpoint, instance = "")
    assertThatShadowApplication()
      .prop<_, List<Intent>, _>(ShadowApplication::getAllStartedServices)
      .single()
      .prop(Intent::getComponent)
      .isNotNull()
      .prop(ComponentName::getClassName)
      .isEqualTo(NotificationService::class.qualifiedName)
  }

  @Test
  fun notifiesInjectedListenerWhenMessageIsReceived() {
    var hasNotified = false
    Injector.injectImmediately<OnMessageReceiptListener> { hasNotified = true }
    receiver.onMessage(
      context,
      PushMessage(content = byteArrayOf(), decrypted = true),
      instance = ""
    )
    assertThat(hasNotified).isTrue()
  }

  @Test
  fun stopsBoundServicesWhenUnregistering() {
    receiver.onNewEndpoint(context, endpoint, instance = "")
    receiver.onUnregistered(context, instance = "")
    assertThat<NotificationService>().isLastlyStopped()
  }

  @AfterTest
  fun tearDown() {
    UnifiedPush.forceRemoveDistributor(context)
    Injector.clear()
    context.unregisterReceiver(receiver)
    clearMocks(keyStore, keyGenerator)
    clearStaticMockk(KeyGenerator::class, KeyStore::class, UnifiedPush::class)
  }

  private fun assertThatShadowApplication() =
    assertThat<ShadowApplication>(
      shadowOf(ApplicationProvider.getApplicationContext<Application>())
    )
}
