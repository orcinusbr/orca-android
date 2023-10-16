package com.jeanbarrossilva.orca.core.module

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoaderProvider
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

/**
 * [Module] into which core-level structures are injected.
 *
 * @param authorizer [Authorizer] by which the user can be authorized.
 * @param authenticator [Authenticator] by which the user can be authenticated.
 * @param actorProvider [ActorProvider] that provides the [Actor].
 * @param authenticationLock [AuthenticationLock] that will lock authentication-dependent
 *   functionality behind a "wall".
 * @param termMuter [TermMuter] by which terms will be muted.
 * @param instanceProvider [InstanceProvider] that will provide the [Instance] in which the
 *   currently [authenticated][Actor.Authenticated] [Actor] is.
 * @param imageLoaderProvider [ImageLoader.Provider] by which an [ImageLoader] will be provided.
 */
open class CoreModule(
  @Inject internal val authorizer: Module.() -> Authorizer,
  @Inject internal val authenticator: Module.() -> Authenticator,
  @Inject internal val actorProvider: Module.() -> ActorProvider,
  @Inject internal val authenticationLock: Module.() -> SomeAuthenticationLock,
  @Inject internal val termMuter: Module.() -> TermMuter,
  @Inject internal val instanceProvider: Module.() -> InstanceProvider,
  @Inject internal val imageLoaderProvider: Module.() -> SomeImageLoaderProvider
) : Module()
