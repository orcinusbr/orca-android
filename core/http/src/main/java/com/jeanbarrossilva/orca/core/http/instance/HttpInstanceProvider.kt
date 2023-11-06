package com.jeanbarrossilva.orca.core.http.instance

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticator
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizer
import com.jeanbarrossilva.orca.core.http.auth.authorization.viewmodel.HttpAuthorizationViewModel
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.instance.SomeInstance
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.termMuter
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.injector.Injector
import java.net.URL

/**
 * [InstanceProvider] that provides a [ContextualHttpInstance].
 *
 * @param context [Context] through which the [Domain] of the [ContextualHttpInstance] will
 *   retrieved.
 * @param actorProvider [ActorProvider] that provides the [Actor].
 * @param authenticationLock [AuthenticationLock] that will lock authentication-dependent
 *   functionality behind a "wall".
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image] will be loaded.
 */
class HttpInstanceProvider(
  private val context: Context,
  private val actorProvider: ActorProvider,
  private val authenticationLock: AuthenticationLock<HttpAuthenticator>,
  private val imageLoaderProvider: ImageLoader.Provider<URL>
) : InstanceProvider {
  private val module by lazy { Injector.from<CoreModule>() }
  private val authorizer by lazy { HttpAuthorizer(context) }
  private val authenticator by lazy { HttpAuthenticator(context, authorizer, actorProvider) }

  private val termMuter by lazy { module.termMuter() }

  override fun provide(): SomeInstance {
    return ContextualHttpInstance(
      context,
      HttpAuthorizationViewModel.getInstanceDomain(context),
      authorizer,
      authenticator,
      actorProvider,
      authenticationLock,
      termMuter,
      imageLoaderProvider
    )
  }
}
