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

package br.com.orcinus.orca.core.mastodon.auth.authorization.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.mastodon.R
import br.com.orcinus.orca.core.mastodon.auth.Mastodon
import br.com.orcinus.orca.core.mastodon.auth.authorization.OnAccessTokenRequestListener
import br.com.orcinus.orca.core.mastodon.instance.MastodonInstance
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import java.net.URI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * [AndroidViewModel] that provides the [uri] to be opened in the browser for authenticating the
 * user.
 *
 * @param application [Application] that allows [Context]-specific behavior.
 * @param onAccessTokenRequestListener [OnAccessTokenRequestListener] to be notified when an access
 *   token is requested.
 */
internal class MastodonAuthorizationViewModel
private constructor(
  application: Application,
  private val onAccessTokenRequestListener: OnAccessTokenRequestListener
) : AndroidViewModel(application) {
  /** [MutableStateFlow] to which the [String] representation of the [Domain] will be sent. */
  private val domainMutableFlow = MutableStateFlow("")

  /** [Application] with which this [MastodonAuthorizationViewModel] was created. */
  private val application
    @JvmName("_application") get() = getApplication<Application>()

  /** [StateFlow] version of the [domainMutableFlow]. */
  val domainFlow = domainMutableFlow.asStateFlow()

  /** [URI] to be opened in order to authorize. */
  val uri
    get() = createURI(application, Domain(domainFlow.value))

  /**
   * Emits [domain] to the [domainFlow].
   *
   * @param domain [String] representation of the [Domain] to be emitted.
   */
  fun setDomain(domain: String) {
    domainMutableFlow.value = domain
  }

  /**
   * Persists the current [Domain], injects the derived [MastodonInstance] and notifies the
   * [onAccessTokenRequestListener].
   */
  fun authorize() {
    persistDomain()
    onAccessTokenRequestListener.onAccessTokenRequest()
  }

  /** Persists the current value of the [domainFlow]. */
  private fun persistDomain() {
    getPreferences(application).edit { putString(INSTANCE_DOMAIN_PREFERENCE_KEY, domainFlow.value) }
  }

  companion object {
    /**
     * Key through which the [Domain] of the [Instance] in which the user has been authorized can be
     * retrieved.
     */
    private const val INSTANCE_DOMAIN_PREFERENCE_KEY = "instance-domain"

    /**
     * Creates a [ViewModelProvider.Factory] that provides a [MastodonAuthorizationViewModel].
     *
     * @param application [Application] for [Context]-specific behavior.
     * @param onAccessTokenRequestListener [OnAccessTokenRequestListener] to be notified when an
     *   access token is requested.
     */
    fun createFactory(
      application: Application,
      onAccessTokenRequestListener: OnAccessTokenRequestListener
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        initializer { MastodonAuthorizationViewModel(application, onAccessTokenRequestListener) }
      }
    }

    /**
     * Creates an authorization [URI] for the given [domain].
     *
     * @param context [Context] by which the redirect URI will be provided.
     * @param domain [Domain] for which the [URI] will be created.
     */
    internal fun createURI(context: Context, domain: Domain): URI {
      return with(context) {
        HostedURLBuilder.from(domain.uri)
          .path("oauth")
          .path("authorize")
          .query()
          .parameter("response_type", "code")
          .parameter("client_id", Mastodon.CLIENT_ID)
          .parameter("redirect_uri", getString(R.string.redirect_uri, getString(R.string.scheme)))
          .parameter("scope", Mastodon.SCOPES)
          .build()
      }
    }

    /**
     * Gets the [Domain] that's been persisted when the user was authorized.
     *
     * @throws IllegalStateException If this method is called before authorization has completed.
     */
    @Throws(IllegalStateException::class)
    fun getInstanceDomain(context: Context): Domain {
      return getPreferences(context)
        .getString(INSTANCE_DOMAIN_PREFERENCE_KEY, null)
        .let(::checkNotNull)
        .let(::Domain)
    }

    /**
     * Gets the [SharedPreferences] related to the authorization process.
     *
     * @param context [Context] from which the [SharedPreferences] will be obtained.
     */
    private fun getPreferences(context: Context): SharedPreferences {
      return context.getSharedPreferences("http-authorization", Context.MODE_PRIVATE)
    }
  }
}
