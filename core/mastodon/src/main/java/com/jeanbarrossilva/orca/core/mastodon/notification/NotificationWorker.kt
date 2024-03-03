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
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.mastodon.notification.server.NotificationServer
import kotlinx.coroutines.Job

/**
 * [CoroutineWorker] for listening to updates regarding the [Actor] that reach the server, notifying
 * them of those.
 */
internal abstract class NotificationWorker(context: Context, parameters: WorkerParameters) :
  CoroutineWorker(context, parameters) {
  override suspend fun doWork(): Result {
    val server = createServer().also(::stopServerOnStop)
    return try {
      server.start()
      server.await()
      Result.success()
    } catch (_: AuthenticationLock.FailedAuthenticationException) {
      Result.failure()
    }
  }

  /**
   * Creates a [NotificationServer] that forwards the notifications received by Mastodon, sending
   * them to the device.
   */
  protected abstract fun createServer(): NotificationServer

  /**
   * Stops the [server] when this [NotificationWorker] is stopped.
   *
   * @param server [NotificationServer] to be stopped.
   */
  private fun stopServerOnStop(server: NotificationServer) {
    @Suppress("DEPRECATION") coroutineContext[Job]?.invokeOnCompletion { server.stop() }
  }

  companion object {

    /**
     * Singularly and uniquely enqueues a [NotificationWorker].
     *
     * @param context [Context] for either creating or retrieving the [WorkManager] by which a
     *   [NotificationWorker] will be enqueued.
     */
    fun enqueue(context: Context) {
      val request = OneTimeWorkRequest.from(NotificationWorker::class.java)
      WorkManager.getInstance(context)
        .enqueueUniqueWork("notification", ExistingWorkPolicy.KEEP, request)
    }
  }
}
