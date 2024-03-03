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

package com.jeanbarrossilva.orca.core.mastodon.notification.server

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.mastodon.authenticationLock
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndSubmitForm
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoWSD
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequest
import io.ktor.http.Parameters
import io.ktor.util.encodeBase64
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.net.URL
import java.security.KeyPairGenerator
import java.security.spec.ECGenParameterSpec
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * [WebSocket](https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API) server that listens
 * to [Notification]s sent to Mastodon and forwards them to the device.
 *
 * Different than what may be expected (given the client-networking actors in this module), a
 * [NotificationServer] is not a [Ktor](https://ktor.io) server, but a
 * [NanoHTTPD](http://nanohttpd.org) one. Connection is initially tried to be established in the
 * [DEFAULT_PORT]; if it is not available, then it is done so on any other one that is free when the
 * class is instantiated.
 *
 * The process of generating keys (such as that for authentication and the encryption one) that is
 * performed in order to subscribe to the receipt of sent notifications is heavily based on
 * [how it was implemented in the official Mastodon Android app](https://github.com/mastodon/mastodon-android/blob/b4904024c6a9b509eab9f9341de975c84d5b3760/mastodon/src/main/java/org/joinmastodon/android/api/PushSubscriptionManager.java#L123).
 *
 * By starting this server, a [NanoWSD.WebSocket] is automatically opened; similarly, stopping
 * closes it.
 *
 * @param authenticationLock [AuthenticationLock] through which the ID of the
 *   [authenticated][Actor.Authenticated] [Actor] will be obtained for sending the [HttpRequest]
 *   that subscribes to the notifications sent to the Mastodon server.
 * @param httpClient [HttpClient] by which the [HttpRequest] will be performed.
 * @see authenticationKey
 * @see encryptionKey
 * @see start
 * @see NanoWSD.WebSocket.onOpen
 * @see stop
 * @see NanoWSD.WebSocket.close
 */
class NotificationServer
internal constructor(
  private val authenticationLock: SomeAuthenticationLock,
  private val httpClient: HttpClient
) : NanoWSD(defaultOrAvailablePort) {
  /** [CoroutineScope] that is tied to the lifecycle of this [NotificationServer]. */
  private val coroutineScope = CoroutineScope(Dispatchers.Default)

  /**
   * [NanoHTTPD.HTTPSession] with which the [webSocket] is initially opened on start.
   *
   * @see NanoWSD.WebSocket.onOpen
   * @see start
   */
  private val openingSession =
    HTTPSession(
      tempFileManagerFactory.create(),
      ByteArray(HTTPSession.BUFSIZE).inputStream(),
      ByteArrayOutputStream()
    )

  /** [NanoWSD.WebSocket] that is currently open. */
  private var webSocket: WebSocket? = null

  /** Base64-encoded 16-bit authentication key. */
  private val authenticationKey = Random.nextBytes(16).encodeBase64()

  /** Encryption key as a Base64-encoded [String]. */
  private val encryptionKey =
    KeyPairGenerator.getInstance("EC")
      .apply { initialize(ECGenParameterSpec("prime256v1")) }
      .generateKeyPair()
      .public
      .encoded
      .encodeBase64()

  override fun start(timeout: Int, daemon: Boolean) {
    super.start(timeout, daemon)
    openWebSocket(openingSession)
  }

  override fun openWebSocket(handshake: IHTTPSession?): WebSocket {
    return object : WebSocket(handshake) {
        public override fun onOpen() {
          webSocket = this
          forwardReceivedNotifications()
        }

        override fun onClose(
          code: WebSocketFrame.CloseCode?,
          reason: String?,
          initiatedByRemote: Boolean
        ) {
          webSocket = null
        }

        override fun onMessage(message: WebSocketFrame?) {}

        override fun onPong(pong: WebSocketFrame?) {}

        override fun onException(exception: IOException?) {}
      }
      .also { it.onOpen() }
  }

  override fun stop() {
    super.stop()
    webSocket?.close(WebSocketFrame.CloseCode.GoingAway, "Server was stopped.", false)
    coroutineScope.cancel()
  }

  /** Suspends until this [NotificationServer] is stopped. */
  internal suspend fun await() {
    coroutineScope.coroutineContext[Job]?.join()
  }

  /**
   * Listens to [Notification]s sent to the Mastodon server by posting to
   * `api/v1/push/subscription`, adding a web
   * [Push API](https://developer.mozilla.org/en-US/docs/Web/API/Push_API) subscription whose
   * observed events are redirected to this [NotificationServer], which, in turn, forwards them to
   * the device.
   *
   * @throws AuthenticationLock.FailedAuthenticationException If an attempt to schedule an unlock
   *   through the [authenticationLock] fails because the [Actor] was
   *   [unauthenticated][Actor.Unauthenticated] and authentication failed to be performed.
   * @see HttpClient.authenticateAndSubmitForm
   */
  @Throws(AuthenticationLock.FailedAuthenticationException::class)
  private fun forwardReceivedNotifications() {
    coroutineScope.launch {
      authenticationLock.scheduleUnlock {
        httpClient.authenticateAndSubmitForm(
          "api/v1/push/subscription",
          Parameters.build {
            append("data[alerts][favourite]", "true")
            append("data[alerts][follow]", "true")
            append("data[alerts][follow_request]", "true")
            append("data[alerts][mention]", "true")
            append("data[alerts][poll]", "true")
            append("data[alerts][reblog]", "true")
            append("data[alerts][severed_relationships]", "true")
            append("data[alerts][status]", "true")
            append("data[alerts][update]", "true")
            append("data[policy]", "all")
            append("subscription[endpoint]", "${URL("https", HOSTNAME, listeningPort, "")}")
            append("subscription[keys][auth]", authenticationKey)
            append("subscription[keys][p256dh]", encryptionKey)
          }
        )
      }
    }
  }

  companion object {
    /** Host of the connection. */
    private const val HOSTNAME = "localhost"

    /** Port in which connection will be tried by default. */
    private const val DEFAULT_PORT = 8080

    /**
     * [DEFAULT_PORT] or other that is actually available if the default one is already occupied.
     */
    private val defaultOrAvailablePort
      get() = if (isDefaultPortAvailable) DEFAULT_PORT else availablePort

    /** Whether the [DEFAULT_PORT] is available (that is, if it is not currently occupied). */
    private val isDefaultPortAvailable
      get() =
        try {
          Socket(HOSTNAME, DEFAULT_PORT).close()
          false
        } catch (_: IOException) {
          true
        }

    /** Port in which a [NotificationServer] can be run. */
    private val availablePort
      get() = ServerSocket(0).use(ServerSocket::getLocalPort)
  }
}
