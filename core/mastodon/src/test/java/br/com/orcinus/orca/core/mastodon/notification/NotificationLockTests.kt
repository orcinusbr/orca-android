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

import android.Manifest
import android.app.Application
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.each
import assertk.assertions.isInstanceOf
import br.com.orcinus.orca.ext.testing.assertThat
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
internal class NotificationLockTests {
  private class RequestActivity : ComponentActivity()

  @Test
  fun throwsWhenConstructedInActivityInStartOrPosteriorState() =
    launchActivity<RequestActivity>().use { scenario ->
      assertThat(Lifecycle.State.entries)
        .transform(">= ${Lifecycle.State.STARTED}") { states ->
          states.filter { state -> state >= Lifecycle.State.STARTED }
        }
        .each { assert ->
          assert.given { state ->
            scenario.moveToState(state)?.onActivity { activity: RequestActivity ->
              assertFailure { NotificationLock(activity) }.isInstanceOf<IllegalStateException>()
            }
          }
        }
    }

  @Config(
    sdk =
      [
        Build.VERSION_CODES.P,
        Build.VERSION_CODES.Q,
        Build.VERSION_CODES.R,
        Build.VERSION_CODES.S,
        Build.VERSION_CODES.S_V2
      ]
  )
  @Test
  fun bindsServiceWhenUnlockingInAnApiLevelInWhichThePermissionIsUnsupported() {
    launchActivity<RequestActivity>()
      .moveToState(Lifecycle.State.CREATED)
      ?.onActivity { NotificationLock(it).unlock() }
      ?.close()
    assertThat<NotificationService>().isBound()
    shadowOf(ApplicationProvider.getApplicationContext<Application>())?.clearStartedServices()
  }

  @Config(sdk = [Build.VERSION_CODES.TIRAMISU, Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
  @Test
  fun requestsPermissionWhenUnlockingInAnApiLevelInWhichItIsSupported() {
    val launcher = spyk<ActivityResultLauncher<String>>()
    every { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) } returns Unit
    launchActivity<RequestActivity>()
      .moveToState(Lifecycle.State.CREATED)
      ?.onActivity { activity -> NotificationLock(activity, launcher).unlock() }
      ?.close()
    verify(exactly = 1) { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) }
    clearMocks(launcher)
  }
}
