package com.jeanbarrossilva.mastodonte.core.account

import java.io.Serializable

/**
 * Identifies the user within an [instance].
 *
 * An [Account] can be instantiated either through the [at] extension function or by parsing a given
 * [String] with [of].
 *
 * @param username Unique name that can be modified.
 * @param instance Mastodon instance from which the user is.
 * @throws BlankUsernameException If the [username] is blank.
 * @throws IllegalUsernameException If the [username] contains any of the [illegalCharacters].
 * @throws BlankInstanceException If the [instance] is blank.
 * @throws IllegalInstanceException If the [instance] contains any of the [illegalCharacters].
 * @throws InstanceWithoutDomainException If the [instance] doesn't have a domain.
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
        return username + SEPARATOR + instance
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
        /** [Char] that separates the [username] from the [instance]. **/
        private const val SEPARATOR = '@'

        /** [Char]s that neither the [username] nor the [instance] should contain. **/
        private val illegalCharacters = arrayOf(' ', SEPARATOR)

        /** Whether the [String] contains a domain. **/
        private val String.containsDomain
            get() = split('.').getOrNull(1)?.isNotBlank() ?: false

        /** Whether this [String] contains none of the [illegalCharacters]. **/
        private val String.isLegal
            get() = illegalCharacters.none { it in this }

        /**
         * [IllegalArgumentException] thrown if a blank [String] is given when parsing it into an
         * [Account].
         *
         * @see of
         **/
        class BlankStringException internal constructor() :
            IllegalArgumentException("Cannot parse a blank String.")

        /**
         * Parses the [string] into an [Account].
         *
         * It should be formatted as `"{username}@{instance}"`, with "{username}" and "{instance}"
         * replaced by the actual data they represent; it can also be given as `"{username}"`,
         * without the instance, if a [fallbackInstance] is specified.
         *
         * Any leading or trailing whitespace in both the [string] and the [fallbackInstance] will
         * be ignored.
         *
         * @param string [String] to be parsed.
         * @param fallbackInstance Instance to fallback to if the [string] only has a username.
         * @throws BlankStringException If the [string] is blank.
         * @throws BlankUsernameException If the username is blank.
         * @throws IllegalUsernameException If the username contains any of the
         * [illegalCharacters].
         * @throws BlankInstanceException If the instance is not specified in the [string] and the
         * [fallbackInstance] is `null`.
         * @throws IllegalInstanceException If the instance contains any of the
         * [illegalCharacters].
         * @throws InstanceWithoutDomainException If the instance doesn't have a domain.
         **/
        fun of(string: String, fallbackInstance: String? = null): Account {
            val formattedString = string.trim().ifEmpty { throw BlankStringException() }
            val split = formattedString.split(SEPARATOR)
            val username = split.first()
            val containsInstance = split.size > 1
            val formattedFallbackInstance = fallbackInstance?.trim().orEmpty()
            val instance = if (containsInstance) split[1].trim() else formattedFallbackInstance
            return Account(username, instance)
        }

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
