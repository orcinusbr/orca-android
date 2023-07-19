package com.jeanbarrossilva.mastodonte.core.toot

import java.io.Serializable

/**
 * Identifies the user within an [instance].
 *
 * @param username Unique name that can be modified.
 * @param instance Mastodon instance from which the user is.
 **/
data class Account internal constructor(val username: String, val instance: String) :
    Serializable {
    init {
        require(username.isNotBlank()) { "An account cannot have a blank username." }
        require(instance.isNotBlank()) { "An account cannot have a blank instance." }
        requireLegalCharacters(username) { "Username cannot contain: $it." }
        requireLegalCharacters(instance) { "Instance cannot contain: $it." }
    }

    companion object {
        /** [Char]s that neither the [username] nor the [instance] should contain. **/
        private val illegalCharacters = arrayOf('@')

        /**
         * [Require][require]s that the [string] doesn't contain any of the [illegalCharacters].
         *
         * @param string [String] to check against.
         * @param message Lazy message to be shown when the [IllegalArgumentException] is thrown.
         * @throws IllegalArgumentException If the [string] has any of the [illegalCharacters].
         **/
        private fun requireLegalCharacters(
            string: String,
            message: (illegal: List<Char>) -> String
        ) {
            val illegal = illegalCharacters.filter { it in string }
            require(illegal.isEmpty()) { message(illegal) }
        }
    }
}

/**
 * Creates an [Account] with the receiver [String] as the [username][Account.username] and
 * [instance] as the [Account.instance].
 *
 * @param instance Mastodon instance from which the user is.
 **/
infix fun String.at(instance: String): Account {
    return Account(username = this, instance)
}
