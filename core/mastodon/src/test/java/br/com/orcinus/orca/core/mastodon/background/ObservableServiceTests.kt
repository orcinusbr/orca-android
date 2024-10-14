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

package br.com.orcinus.orca.core.mastodon.background

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import assertk.assertThat
import assertk.assertions.isTrue
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ServiceController

@RunWith(RobolectricTestRunner::class)
internal class ObservableServiceTests {
  @Test
  fun defaultLifecycleObserverIsNotifiedOfCreation() {
    var isNotified = false
    runObservableServiceTest(
      object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
          super.onCreate(owner)
          isNotified = true
        }
      },
      body = ServiceController<ObservableService>::create
    )
    assertThat(isNotified).isTrue()
  }

  @Test
  fun defaultLifecycleObserverIsNotifiedOfStartup() {
    var isNotified = false
    runObservableServiceTest(
      object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
          super.onStart(owner)
          isNotified = true
        }
      }
    ) {
      startCommand(/* flags = */ 0, /* startId = */ 0)
    }
    assertThat(isNotified).isTrue()
  }

  @Test
  fun defaultLifecycleObserverIsNotifiedOfDestruction() {
    var isNotified = false
    runObservableServiceTest(
      object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
          super.onDestroy(owner)
          isNotified = true
        }
      },
      body = ServiceController<ObservableService>::destroy
    )
    assertThat(isNotified).isTrue()
  }

  @Test
  fun eventBasedObserverIsNotifiedOfCreation() {
    var isNotified = false
    runObservableServiceTest(
      object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
          isNotified = event == Lifecycle.Event.ON_CREATE
        }
      },
      body = ServiceController<ObservableService>::create
    )
    assertThat(isNotified).isTrue()
  }

  @Test
  fun eventBasedObserverIsNotifiedOfStartup() {
    var isNotified = false
    runObservableServiceTest(
      object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
          isNotified = event == Lifecycle.Event.ON_START
        }
      }
    ) {
      startCommand(/* flags = */ 0, /* startId = */ 0)
    }
    assertThat(isNotified).isTrue()
  }

  @Test
  fun eventBasedObserverIsNotifiedOfDestruction() {
    var isNotified = false
    runObservableServiceTest(
      object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
          isNotified = event == Lifecycle.Event.ON_DESTROY
        }
      },
      body = ServiceController<ObservableService>::destroy
    )
    assertThat(isNotified).isTrue()
  }
}
