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
import android.content.ComponentName
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import assertk.assertions.prop
import assertk.assertions.single
import br.com.orcinus.orca.core.mastodon.notification.service.NotificationService
import br.com.orcinus.orca.core.mastodon.notification.service.NotificationServiceTestScope
import br.com.orcinus.orca.core.mastodon.notification.service.OnMessageReceiptListener
import br.com.orcinus.orca.core.mastodon.test.notification.runNotificationReceiverTest
import br.com.orcinus.orca.ext.testing.assertThat
import br.com.orcinus.orca.std.injector.Injector
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowApplication
import org.unifiedpush.android.connector.FailedReason
import org.unifiedpush.android.connector.data.PushEndpoint
import org.unifiedpush.android.connector.data.PushMessage

@RunWith(RobolectricTestRunner::class)
internal class NotificationReceiverTests {
  private val endpoint = PushEndpoint(NotificationServiceTestScope.endpoint, pubKeySet = null)

  @Test
  fun stopsBoundServicesWhenRegistrationFails() = runNotificationReceiverTest {
    receiver.onNewEndpoint(context, endpoint, instance = "")
    receiver.onRegistrationFailed(context, FailedReason.INTERNAL_ERROR, instance = "")
    assertThat<NotificationService>().isLastlyStopped()
  }

  @Test
  fun bindsServiceWhenANewEndpointIsReceived() = runNotificationReceiverTest {
    receiver.onNewEndpoint(context, endpoint, instance = "")
    assertThat<ShadowApplication>(
        shadowOf(ApplicationProvider.getApplicationContext<Application>())
      )
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
    runNotificationReceiverTest {
      receiver.onMessage(
        context,
        PushMessage(content = byteArrayOf(), decrypted = true),
        instance = ""
      )
    }
    assertThat(hasNotified).isTrue()
  }

  @Test
  fun stopsBoundServicesWhenUnregistering() {
    runNotificationReceiverTest {
      receiver.onNewEndpoint(context, endpoint, instance = "")
      receiver.onUnregistered(context, instance = "")
    }
    assertThat<NotificationService>().isLastlyStopped()
  }
}
