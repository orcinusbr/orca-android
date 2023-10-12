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
