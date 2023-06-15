package com.jeanbarrossilva.mastodonte.core.profile

/** Status that indicates in which "following" state a user is related to another. **/
abstract class Follow private constructor() {
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
