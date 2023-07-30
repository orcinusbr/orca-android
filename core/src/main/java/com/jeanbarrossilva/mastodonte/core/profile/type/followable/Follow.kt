package com.jeanbarrossilva.mastodonte.core.profile.type.followable

import java.io.Serializable

/** Status that indicates in which "following" state a user is related to another. **/
abstract class Follow private constructor() : Serializable {
    /** Name that describes the visibility for which this [Follow] status is intended. **/
    protected abstract val visibilityName: String

    /** Public [Follow] status. **/
    abstract class Public private constructor() : Follow() {
        override val visibilityName = "public"

        companion object {
            /**
             * [Follow] status in which a user doesn't receive any updates related to the other's
             * activity.
             **/
            fun unfollowed(): Public {
                return object : Public() {
                    override fun toString(): String {
                        return "Follow.Public.unfollowed"
                    }

                    override fun toggled(): Follow {
                        return following()
                    }

                    override fun next(): Follow {
                        return following()
                    }
                }
            }

            /** [Follow] status in which a user has subscribed to receive updates from another. **/
            fun following(): Public {
                return object : Public() {
                    override fun toString(): String {
                        return "Follow.Public.following"
                    }

                    override fun toggled(): Follow {
                        return unfollowed()
                    }

                    override fun next(): Follow? {
                        return null
                    }
                }
            }
        }
    }

    /** Private [Follow] status. **/
    abstract class Private private constructor() : Follow() {
        override val visibilityName = "private"

        companion object {
            /**
             * [Follow] status in which a user doesn't receive any updates related to the other's
             * activity.
             **/
            fun unfollowed(): Private {
                return object : Private() {
                    override fun toString(): String {
                        return "Follow.Private.unfollowed"
                    }

                    override fun toggled(): Follow {
                        return requested()
                    }

                    override fun next(): Follow {
                        return requested()
                    }
                }
            }

            /**
             * [Follow] status in which a user has requested to follow another and is waiting for it to
             * be accepted or denied.
             **/
            fun requested(): Private {
                return object : Private() {
                    override fun toString(): String {
                        return "Follow.Private.requested"
                    }

                    override fun toggled(): Follow {
                        return unfollowed()
                    }

                    override fun next(): Follow {
                        return following()
                    }
                }
            }

            /** [Follow] status in which a user has subscribed to receive updates from another. **/
            fun following(): Private {
                return object : Private() {
                    override fun toString(): String {
                        return "Follow.Private.following"
                    }

                    override fun toggled(): Follow {
                        return unfollowed()
                    }

                    override fun next(): Follow? {
                        return null
                    }
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return toString() == other?.toString()
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }

    /** Provides the [Follow] status that's contrary to this one. **/
    internal abstract fun toggled(): Follow

    /** Provides the [Follow] status that succeeds this one. **/
    internal abstract fun next(): Follow?

    companion object {
        /**
         * [IllegalArgumentException] thrown if a blank [String] is given when parsing it into a
         * [Follow].
         *
         * @see of
         **/
        class BlankStringException internal constructor() :
            IllegalArgumentException("Cannot parse a blank String.")

        /**
         * [IllegalArgumentException] thrown if the given [String] is not a valid [Follow]
         * representation.
         *
         * @param string [Follow] representation that's invalid.
         **/
        class InvalidFollowString internal constructor(string: String) : IllegalArgumentException(
            "\"$string\" does not match any of the available Follow String representations."
        )

        /**
         * Parses the [string] into a [Follow].
         *
         * [string] must be the result of calling [toString] on one of the existing [Follow]s
         * (e. g., `Follow.of("${Follow.Public.unfollowed()}")` returns [Public.unfollowed]). Any
         * leading or trailing whitespace it may contain will be ignored.
         *
         * @param string [String] to be parsed into a [Follow].
         * @return [Follow] whose result of [toString] equals to the [string] (minus surrounding
         * whitespaces).
         * @throws BlankStringException If the [string] is blank.
         * @throws InvalidFollowString If the [string] is not a valid [Follow] representation.
         **/
        fun of(string: String): Follow {
            val formattedString = string.trim()
            formattedString.ifBlank { throw BlankStringException() }
            return when (formattedString) {
                Public.unfollowed().toString() -> Public.unfollowed()
                Public.following().toString() -> Public.following()
                Private.unfollowed().toString() -> Private.unfollowed()
                Private.requested().toString() -> Private.requested()
                Private.following().toString() -> Private.following()
                else -> throw InvalidFollowString(formattedString)
            }
        }

        /**
         * Requires both statuses to have the same visibility: either [public][Follow.Public] or
         * [private][Follow.Private].
         *
         * @param expected Status that indicates which visibility [actual]'s should match.
         * @param actual Status whose visibility should match [expected]'s.
         * @return [actual] as [T].
         * @throws IllegalArgumentException If [expected] and [actual] have different visibilities.
         * @see visibilityName
         **/
        fun <T : Follow> requireVisibilityMatch(expected: T, actual: Follow): T {
            val isCohesive = actual.visibilityName == expected.visibilityName
            return if (isCohesive) {
                @Suppress("UNCHECKED_CAST")
                actual as T
            } else {
                throw IllegalArgumentException(
                    "$actual is a ${actual.visibilityName} status instead of a " +
                        "${expected.visibilityName} one."
                )
            }
        }
    }
}
