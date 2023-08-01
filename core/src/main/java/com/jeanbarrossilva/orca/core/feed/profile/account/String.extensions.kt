package com.jeanbarrossilva.orca.core.feed.profile.account

/**
 * Creates an [Account] with the receiver [String] as the [username][Account.username] and
 * [instance] as the [Account.instance].
 *
 * @param instance Mastodon instance from which the user is.
 **/
infix fun String.at(instance: String): Account {
    return Account(username = this, instance)
}
