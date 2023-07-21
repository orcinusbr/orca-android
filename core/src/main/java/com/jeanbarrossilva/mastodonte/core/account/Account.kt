package com.jeanbarrossilva.mastodonte.core.account

import java.io.Serializable

/**
 * Identifies the user within an [instance].
 *
 * @param username Unique name that can be modified.
 * @param instance Mastodon instance from which the user is.
 **/
data class Account internal constructor(val username: String, val instance: String) : Serializable {
    /** [IllegalArgumentException] thrown if the [username] is blank. **/
    class BlankUsernameException internal constructor() :
        IllegalArgumentException("An account cannot have a blank username.")

    /**
     * [IllegalArgumentException] thrown if the [username] contains any of the [illegalCharacters].
     *
     * @param illegalCharacters [Char]s that make the [username] illegal.
     **/
    class IllegalUsernameException internal constructor(illegalCharacters: List<Char>) :
        IllegalArgumentException("Username cannot contain: $illegalCharacters")

    /** [IllegalArgumentException] thrown if the [instance] is blank. **/
    class BlankInstanceException internal constructor() :
        IllegalArgumentException("An account cannot have a blank instance.")

    /**
     * [IllegalArgumentException] thrown if the [instance] contains any of the [illegalCharacters].
     *
     * @param illegalCharacters [Char]s that make the [instance] illegal.
     **/
    class IllegalInstanceException internal constructor(illegalCharacters: List<Char>) :
        IllegalArgumentException("Instance cannot contain: $illegalCharacters")

    /** [IllegalArgumentException] thrown if the [instance] doesn't have a domain. **/
    inner class InstanceWithoutDomainException internal constructor() :
        IllegalArgumentException("The instance \"$instance\" doesn't have a domain.")

    init {
        ensureUsernameNonBlankness()
        ensureUsernameLegality()
        ensureInstanceNonBlankness()
        ensureInstanceLegality()
        ensureInstanceDomainPresence()
    }

    override fun toString(): String {
        return "$username@$instance"
    }

    /**
     * Ensures that the [username] is not blank.
     *
     * @throws BlankUsernameException If the [username] is blank.
     **/
    private fun ensureUsernameNonBlankness() {
        if (username.isBlank()) {
            throw BlankUsernameException()
        }
    }

    /**
     * Ensures that the [username] is legal.
     *
     * @throws IllegalUsernameException If the [username] is illegal.
     **/
    private fun ensureUsernameLegality() {
        ensureLegalityOf(username) {
            throw IllegalUsernameException(it)
        }
    }

    /**
     * Ensures that the instance is not blank.
     *
     * @throws BlankInstanceException If the [instance] is blank.
     **/
    private fun ensureInstanceNonBlankness() {
        if (instance.isBlank()) {
            throw BlankInstanceException()
        }
    }

    /**
     * Ensures that the [instance] is legal.
     *
     * @throws IllegalInstanceException If the [instance] is illegal.
     **/
    private fun ensureInstanceLegality() {
        ensureLegalityOf(instance) {
            throw IllegalInstanceException(it)
        }
    }

    /**
     * Ensures that the [instance] has a domain.
     *
     * @throws InstanceWithoutDomainException If the [instance] doesn't have a domain.
     **/
    private fun ensureInstanceDomainPresence() {
        if (!instance.containsDomain) {
            throw InstanceWithoutDomainException()
        }
    }

    companion object {
        /** [Char]s that neither the [username] nor the [instance] should contain. **/
        private val illegalCharacters = arrayOf('@')

        /** Whether the [String] contains a domain. **/
        private val String.containsDomain
            get() = split('.').getOrNull(1)?.isNotBlank() ?: false

        /** Whether this [String] contains none of the [illegalCharacters]. **/
        private val String.isLegal
            get() = illegalCharacters.none { it in this }

        /**
         * Verifies whether the username is valid.
         *
         * @param username Username whose validity will be verified.
         **/
        fun isUsernameValid(username: String): Boolean {
            return username.isNotBlank() && username.isLegal
        }

        /**
         * Verifies whether the instance is valid.
         *
         * @param instance Instance whose validity will be verified.
         **/
        fun isInstanceValid(instance: String): Boolean {
            return instance.isNotBlank() && instance.isLegal && instance.containsDomain
        }

        /**
         * Ensures that the [string] doesn't contain any of the [illegalCharacters].
         *
         * @param string [String] to check against.
         * @param onIllegality Callback called if the [string] is considered to be illegal.
         **/
        private fun ensureLegalityOf(
            string: String,
            onIllegality: (illegal: List<Char>) -> Unit
        ) {
            val illegal = illegalCharacters.filter { it in string }
            val isIllegal = illegal.any { it in string }
            if (isIllegal) {
                onIllegality(illegal)
            }
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
