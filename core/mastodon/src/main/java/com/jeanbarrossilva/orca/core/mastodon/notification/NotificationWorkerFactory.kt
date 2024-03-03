/*
 * Copyright © 2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.mastodon.notification

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.mastodon.instance.MastodonInstance
import com.jeanbarrossilva.orca.core.mastodon.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.mastodon.notification.server.NotificationServer
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.authenticationLock
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.std.injector.Injector

/**
 * [WorkerFactory] that handles the creation of [NotificationWorker]s.
 *
 * **NOTE**: The creation process relies solely on the [CoreModule] that is expected to have been
 * registered into the [Injector], whose injected [InstanceProvider] should provide a
 * [MastodonInstance]. If either criteria fails to be met when [createWorker] is called, an
 * [Injector.ModuleNotRegisteredException] or a [ClassCastException] will be thrown.
 *
 * @see Injector.register
 * @see CoreModule.inject
 * @see CoreModule.instanceProvider
 * @see InstanceProvider.provide
 */
object NotificationWorkerFactory : WorkerFactory() {
  override fun createWorker(
    appContext: Context,
    workerClassName: String,
    workerParameters: WorkerParameters
  ): ListenableWorker? {
    return if (workerClassName == NotificationWorker::class.qualifiedName) {
      createWorkerFromInjectedMastodonCoreModule(appContext, workerParameters)
    } else {
      null
    }
  }

  /**
   * Creates a [NotificationWorker] with some of the structures that have been injected into the
   * [CoreModule] that has been registered in the [Injector].
   *
   * @param context [Context]
   * @see CoreModule.inject
   * @see Injector.register
   */
  private fun createWorkerFromInjectedMastodonCoreModule(
    context: Context,
    parameters: WorkerParameters
  ): NotificationWorker {
    @Suppress("WorkerHasAPublicModifier")
    return object : NotificationWorker(context, parameters) {
      override fun createServer(): NotificationServer {
        val module = Injector.from<CoreModule>()
        val authenticationLock = module.authenticationLock()
        val instance = module.instanceProvider().provide() as SomeHttpInstance
        return NotificationServer(authenticationLock, instance.client)
      }
    }
  }
}
