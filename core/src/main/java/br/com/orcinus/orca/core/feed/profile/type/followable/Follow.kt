/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.feed.profile.type.followable

import br.com.orcinus.orca.core.InternalCoreApi
import java.io.Serializable

/** Status that indicates in which "following" state a user is related to another. */
abstract class Follow private constructor() : Serializable {
  /** Name that describes the visibility for which this [Follow] status is intended. */
  protected abstract val visibilityName: String

  /** Public [Follow] status. */
  abstract class Public private constructor() : Follow() {
    override val visibilityName = "public"

    companion object {
      /** [Follow] returned by [unfollowed]. */
      @JvmStatic private lateinit var _unfollowed: Public

      /** [Follow] returned by [following]. */
      @JvmStatic private lateinit var _following: Public

      /**
       * [Follow] status in which a user doesn't receive any updates related to the other's
       * activity.
       */
      @InternalCoreApi
      @JvmStatic
      fun unfollowed(): Public {
        return if (::_unfollowed.isInitialized) {
          _unfollowed
        } else {
          _unfollowed =
            object : Public() {
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
          _unfollowed
        }
      }

      /** [Follow] status in which a user has subscribed to receive updates from another. */
      @InternalCoreApi
      @JvmStatic
      fun following(): Public {
        return if (::_following.isInitialized) {
          _following
        } else {
          _following =
            object : Public() {
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
          _following
        }
      }
    }
  }

  /** Private [Follow] status. */
  abstract class Private private constructor() : Follow() {
    override val visibilityName = "private"

    companion object {
      /** [Follow] returned by [unfollowed]. */
      @JvmStatic private lateinit var _unfollowed: Private

      /** [Follow] returned by [requested]. */
      @JvmStatic private lateinit var _requested: Private

      /** [Follow] returned by [following]. */
      @JvmStatic private lateinit var _following: Private

      /**
       * [Follow] status in which a user doesn't receive any updates related to the other's
       * activity.
       */
      @InternalCoreApi
      @JvmStatic
      fun unfollowed(): Private {
        return if (::_unfollowed.isInitialized) {
          _unfollowed
        } else {
          _unfollowed =
            object : Private() {
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
          _unfollowed
        }
      }

      /**
       * [Follow] status in which a user has requested to follow another and is waiting for it to be
       * accepted or denied.
       */
      @InternalCoreApi
      @JvmStatic
      fun requested(): Private {
        return if (::_requested.isInitialized) {
          _requested
        } else {
          _requested =
            object : Private() {
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
          _requested
        }
      }

      /** [Follow] status in which a user has subscribed to receive updates from another. */
      @InternalCoreApi
      @JvmStatic
      fun following(): Private {
        return if (::_following.isInitialized) {
          _following
        } else {
          _following =
            object : Private() {
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
          _following
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

  /** Provides the [Follow] status that's contrary to this one. */
  internal abstract fun toggled(): Follow

  /** Provides the [Follow] status that succeeds this one. */
  internal abstract fun next(): Follow?

  companion object {
    /**
     * [IllegalArgumentException] thrown if a blank [String] is given when parsing it into a
     * [Follow].
     *
     * @see of
     */
    class BlankStringException internal constructor() :
      IllegalArgumentException("Cannot parse a blank String.")

    /**
     * [IllegalArgumentException] thrown if the given [String] is not a valid [Follow]
     * representation.
     *
     * @param string [Follow] representation that's invalid.
     */
    class InvalidFollowString internal constructor(string: String) :
      IllegalArgumentException(
        "\"$string\" does not match any of the available Follow String representations."
      )

    /**
     * Parses the [string] into a [Follow].
     *
     * [string] must be the result of calling [toString] on one of the existing [Follow]s (e. g.,
     * `Follow.of("${Follow.Public.unfollowed()}")` returns [Public.unfollowed]). Any leading or
     * trailing whitespace it may contain will be ignored.
     *
     * @param string [String] to be parsed into a [Follow].
     * @return [Follow] whose result of [toString] equals to the [string] (minus surrounding
     *   whitespaces).
     * @throws BlankStringException If the [string] is blank.
     * @throws InvalidFollowString If the [string] is not a valid [Follow] representation.
     */
    @InternalCoreApi
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
     */
    @InternalCoreApi
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
