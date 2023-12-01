/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.core.mastodon

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.post.content.TermMuter
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

/**
 * [CoreModule] into which Mastodon-specific core structures are injected.
 *
 * @param termMuter [TermMuter] by which terms will be muted.
 * @param authenticationLock [AuthenticationLock] that will lock authentication-dependent
 *   functionality behind a "wall".
 * @param instanceProvider [InstanceProvider] that will provide the [Instance] in which the
 *   currently [authenticated][Actor.Authenticated] [Actor] is.
 */
open class MastodonCoreModule(
  @Inject internal val instanceProvider: Module.() -> InstanceProvider,
  @Inject internal val authenticationLock: Module.() -> SomeAuthenticationLock,
  @Inject internal val termMuter: Module.() -> TermMuter
) : CoreModule(instanceProvider, authenticationLock, termMuter)
