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

package com.jeanbarrossilva.orca.core.feed.profile.account

import com.jeanbarrossilva.orca.core.instance.domain.Domain

/**
 * Creates an [Account] with the receiver [String] as the [username][Account.username] and [domain]
 * as the [Account.domain].
 *
 * @param domain Mastodon instance from which the user is.
 */
infix fun String.at(domain: String): Account {
  return Account(username = this, Domain(domain))
}
