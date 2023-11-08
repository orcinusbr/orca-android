package com.jeanbarrossilva.orca.core.module

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.TermMuter
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

/**
 * [Module] into which core-level structures are injected.
 *
 * @param instanceProvider [InstanceProvider] that will provide the [Instance] in which the
 *   currently [authenticated][Actor.Authenticated] [Actor] is.
 * @param authenticationLock [AuthenticationLock] that will lock authentication-dependent
 *   functionality behind a "wall".
 * @param termMuter [TermMuter] by which terms will be muted.
 */
open class CoreModule(
  @Inject internal val instanceProvider: Module.() -> InstanceProvider,
  @Inject internal val authenticationLock: Module.() -> SomeAuthenticationLock,
  @Inject internal val termMuter: Module.() -> TermMuter
) : Module()
