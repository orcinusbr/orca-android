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

package com.jeanbarrossilva.orca.core.feed.profile.post.provider

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.DeletablePost
import com.jeanbarrossilva.orca.core.feed.profile.post.Post

/**
 * Creates a [DeletablePost] from this [Post] if its [Author] is identified by the
 * [authenticated][Actor.Authenticated] [Actor]'s ID; otherwise, returns this one.
 *
 * @param authenticationLock [AuthenticationLock] by which the ID of the [Actor] will be provided
 *   and compared to the [Author]'s.
 * @see Post.author
 * @see Author.id
 * @see Actor.Authenticated.id
 */
internal suspend fun Post.asDeletable(authenticationLock: SomeAuthenticationLock): Post {
  /**
   * TODO: Not require the actor to be authenticated in order to return either this post or a
   *   deletable one from it. May be troublesome when allowing unauthenticated ones to browse
   *   through the federated feed.
   */
  return authenticationLock.scheduleUnlock { if (it.id == author.id) asDeletable() else this }
}
