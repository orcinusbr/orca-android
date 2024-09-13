/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.instance.requester

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.isSuccess

/** Logs messages with different severity levels. */
abstract class Logger @InternalRequesterApi internal constructor() {
  /** [Logger] that forwards logs to the Android [Log]. */
  object Android : Logger() {
    /** Tag by which sent logs will be tagged. */
    const val TAG = "Requester"

    override fun info(info: String) {
      Log.i(TAG, info)
    }

    override fun error(error: String) {
      Log.e(TAG, error)
    }
  }

  /**
   * Observes and logs each [HttpResponse] sent to the [HttpClient].
   *
   * @param clientConfig [HttpClientConfig] whose [HttpResponse]s will be logged.
   */
  @InternalRequesterApi
  internal fun start(clientConfig: HttpClientConfig<*>) {
    clientConfig.ResponseObserver {
      val message = it.format()
      val logging = if (it.status.isSuccess()) ::info else ::error
      logging(message)
    }
    clientConfig.HttpResponseValidator {
      handleResponseExceptionWithRequest { cause, _ -> cause.message?.let(::error) }
    }
  }

  /**
   * Logs additional information.
   *
   * @param info [String] with the information to be logged.
   */
  @InternalRequesterApi protected abstract fun info(info: String)

  /**
   * Logs the occurrence of an error.
   *
   * @param error [String] describing the error to be logged.
   */
  @InternalRequesterApi protected abstract fun error(error: String)

  /**
   * Converts this [HttpResponse] into a formatted [String] which contains relevant information
   * regarding both the [HttpRequest] that originated this [HttpResponse] and this [HttpResponse]
   * itself.
   */
  @InternalRequesterApi
  internal suspend fun HttpResponse.format(): String {
    val requestContent = request.content
    val requestFormDataParamsAsString =
      if (requestContent is FormDataContent) " (${requestContent.formData})" else ""
    return """
      ${status.value} on ${request.method.value} ${request.url} $requestFormDataParamsAsString
      $headers
      ${bodyAsText()}
    """
      .trimIndent()
  }
}
