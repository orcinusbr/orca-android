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

import android.app.Service
import android.content.Intent
import androidx.lifecycle.LifecycleObserver
import br.com.orcinus.orca.platform.testing.context
import org.robolectric.android.controller.ServiceController

/**
 * Prepares a test environment by automatically instantiating an [ObservableService] and invoking
 * the specified [body] on its controller. At the end, the [observer] is removed, any connections
 * made to the [Service] are unbound and it is destroyed.
 *
 * @param observer [LifecycleObserver] to be added before the [body] is run and removed after it
 *   returns.
 * @param body Tests the [ObservableService].
 * @see ServiceController
 */
internal fun runObservableServiceTest(
  observer: LifecycleObserver,
  body: ServiceController<ObservableService>.() -> Unit
) {
  val intent = Intent(context, NoOpObservableService::class.java)
  val service: ObservableService = NoOpObservableService().apply { lifecycle.addObserver(observer) }
  val controller = ServiceController.of(service, intent)
  try {
    controller.body()
  } finally {
    service.lifecycle.removeObserver(observer)
    controller.unbind()?.destroy()
  }
}
