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

import android.os.Build
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isZero
import br.com.orcinus.orca.ext.testing.assertThat
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
internal class NotificationLockTests {
  @Config(sdk = [Build.VERSION_CODES.TIRAMISU, Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
  @Test
  fun requestsPermissionWhenUnlockingInAnApiLevelInWhichItIsSupported() {
    var permissionRequestCount = 0
    NotificationLockBuilder()
      .requestPermission { permissionRequestCount++ }
      .build(context)
      .requestUnlock()
    assertThat(permissionRequestCount).isEqualTo(1)
  }

  @Config(sdk = [Build.VERSION_CODES.TIRAMISU, Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
  @Test
  fun doesNotRequestPermissionAgainBeforeIntervalInAnApiLevelInWhichItIsSupported() {
    var permissionRequestCount = 0
    var elapsedTime = Duration.ZERO
    NotificationLockBuilder()
      .getElapsedTime { elapsedTime }
      .requestPermission { permissionRequestCount++ }
      .build(context)
      .apply {
        requestUnlock()
        for (index in
          (NotificationLock.permissionRequestIntervalThreshold.inWholeMilliseconds until 1)) {
          elapsedTime += NotificationLock.permissionRequestIntervalThreshold - index.milliseconds

          requestUnlock()
          assertThat(permissionRequestCount).isEqualTo(1)
        }
      }
  }

  @Config(sdk = [Build.VERSION_CODES.TIRAMISU, Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
  @Test
  fun requestsPermissionAgainAfterIntervalInAnApiLevelInWhichItIsSupported() {
    var permissionRequestCount = 0
    var elapsedTime = Duration.ZERO
    NotificationLockBuilder()
      .getElapsedTime { elapsedTime }
      .requestPermission { permissionRequestCount++ }
      .build(context)
      .apply {
        requestUnlock()
        elapsedTime = NotificationLock.permissionRequestIntervalThreshold
        requestUnlock()
      }
    assertThat(permissionRequestCount).isEqualTo(2)
  }

  @Test
  fun shutsServicesDown() {
    NotificationLockBuilder()
      .build(context)
      .apply { repeat(2) { requestUnlock() } }
      .shutServicesDown()
    assertThat<NotificationService>().bindingCount().isZero()
  }
}
