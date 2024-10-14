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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSameInstanceAs
import assertk.assertions.isTrue
import assertk.assertions.prop
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ServiceController

@RunWith(RobolectricTestRunner::class)
internal class ObservableServiceExtensionsTests {
  @Test
  fun addsObserverBeforeTesting() {
    var isObserverAdded = false
    runObservableServiceTest(
      object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
          isObserverAdded = true
        }
      }
    ) {}
    assertThat(isObserverAdded).isTrue()
  }

  @Test
  fun removesObserverAfterTesting() {
    lateinit var controller: ServiceController<ObservableService>
    var observerNotificationCount = 0
    runObservableServiceTest(
      object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
          observerNotificationCount++
        }
      }
    ) {
      controller = this
    }
    controller.create()
    assertThat(observerNotificationCount).isEqualTo(1)
    controller.destroy()
  }

  @Test
  fun getsDestroyedAfterTesting() {
    lateinit var service: ObservableService
    runObservableServiceTest(object : LifecycleObserver {}) { service = get() }
    assertThat(service)
      .prop(ObservableService::lifecycle)
      .prop(Lifecycle::currentState)
      .isSameInstanceAs(Lifecycle.State.DESTROYED)
  }
}
