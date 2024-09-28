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

/** Status that indicates in which "following" state a user is related to another. */
@InternalCoreApi
sealed class Follow private constructor() {
  /** Public [Follow] status. */
  sealed class Public : Follow() {
    /**
     * [Follow] status in which a user doesn't receive any updates related to the other's activity.
     */
    class Unfollowed internal constructor() : Public() {
      override fun toString(): String {
        return "Follow.Public.Unfollowed"
      }

      override fun toggled(): Following {
        return following()
      }

      override fun next(): Following {
        return following()
      }
    }

    /** [Follow] status in which a user receives updates from another in the feed. */
    class Following internal constructor() : Public() {
      override fun toString(): String {
        return "Follow.Public.Following"
      }

      override fun toggled(): Unfollowed {
        return unfollowed()
      }

      override fun next(): Subscribed {
        return subscribed()
      }
    }

    /**
     * [Follow] status in which a user is notified of updates from another user apart from receiving
     * them in the feed.
     */
    class Subscribed internal constructor() : Public() {
      override fun toString(): String {
        return "Follow.Public.Subscribed"
      }

      override fun toggled(): Unfollowed {
        return unfollowed()
      }

      override fun next(): Unfollowed {
        return unfollowed()
      }
    }

    companion object {
      /** [Follow] returned by [unfollowed]. */
      @JvmStatic private lateinit var _unfollowed: Unfollowed

      /** [Follow] returned by [following]. */
      @JvmStatic private lateinit var _following: Following

      /** [Follow] returned by [subscribed]. */
      @JvmStatic private lateinit var _subscribed: Subscribed

      /**
       * [Follow] status in which a user doesn't receive any updates related to the other's
       * activity.
       */
      @JvmStatic
      fun unfollowed(): Unfollowed {
        return if (::_unfollowed.isInitialized) {
          _unfollowed
        } else {
          _unfollowed = Unfollowed()
          _unfollowed
        }
      }

      /** [Follow] status in which a user receives updates from another in the feed. */
      @JvmStatic
      fun following(): Following {
        return if (::_following.isInitialized) {
          _following
        } else {
          _following = Following()
          _following
        }
      }

      /**
       * [Follow] status in which a user is notified of updates from another user apart from
       * receiving them in the feed.
       */
      @JvmStatic
      fun subscribed(): Subscribed {
        return if (::_subscribed.isInitialized) {
          _subscribed
        } else {
          _subscribed = Subscribed()
          _subscribed
        }
      }
    }
  }

  /** Private [Follow] status. */
  sealed class Private : Follow() {
    /**
     * [Follow] status in which a user doesn't receive any updates related to the other's activity.
     */
    class Unfollowed internal constructor() : Private() {
      override fun toString(): String {
        return "Follow.Private.Unfollowed"
      }

      override fun toggled(): Requested {
        return requested()
      }

      override fun next(): Requested {
        return requested()
      }
    }

    /**
     * [Follow] status in which a user has requested to follow another and is waiting for it to be
     * accepted or denied.
     */
    class Requested internal constructor() : Private() {
      override fun toString(): String {
        return "Follow.Private.Requested"
      }

      override fun toggled(): Unfollowed {
        return unfollowed()
      }

      override fun next(): Following {
        return following()
      }
    }

    /** [Follow] status in which a user receives updates from another in the feed. */
    open class Following internal constructor() : Private() {
      override fun toString(): String {
        return "Follow.Private.Following"
      }

      override fun toggled(): Unfollowed {
        return unfollowed()
      }

      override fun next(): Subscribed {
        return subscribed()
      }
    }

    /**
     * [Follow] status in which a user is notified of updates from another user apart from receiving
     * them in the feed.
     */
    class Subscribed internal constructor() : Private() {
      override fun toString(): String {
        return "Follow.Private.Subscribed"
      }

      override fun toggled(): Unfollowed {
        return unfollowed()
      }

      override fun next(): Unfollowed {
        return unfollowed()
      }
    }

    companion object {
      /** [Follow] returned by [unfollowed]. */
      @JvmStatic private lateinit var _unfollowed: Unfollowed

      /** [Follow] returned by [requested]. */
      @JvmStatic private lateinit var _requested: Requested

      /** [Follow] returned by [following]. */
      @JvmStatic private lateinit var _following: Following

      /** [Follow] returned by [subscribed]. */
      @JvmStatic private lateinit var _subscribed: Subscribed

      /**
       * [Follow] status in which a user doesn't receive any updates related to the other's
       * activity.
       */
      @JvmStatic
      fun unfollowed(): Unfollowed {
        return if (::_unfollowed.isInitialized) {
          _unfollowed
        } else {
          _unfollowed = Unfollowed()
          _unfollowed
        }
      }

      /**
       * [Follow] status in which a user has requested to follow another and is waiting for it to be
       * accepted or denied.
       */
      @JvmStatic
      fun requested(): Requested {
        return if (::_requested.isInitialized) {
          _requested
        } else {
          _requested = Requested()
          _requested
        }
      }

      /** [Follow] status in which a user receives updates from another in the feed. */
      @JvmStatic
      fun following(): Following {
        return if (::_following.isInitialized) {
          _following
        } else {
          _following = Following()
          _following
        }
      }

      /**
       * [Follow] status in which a user is notified of updates from another user apart from
       * receiving them in the feed.
       */
      @JvmStatic
      fun subscribed(): Subscribed {
        return if (::_subscribed.isInitialized) {
          _subscribed
        } else {
          _subscribed = Subscribed()
          _subscribed
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
  @InternalCoreApi abstract fun toggled(): Follow

  /** Provides the [Follow] status that succeeds this one. */
  internal abstract fun next(): Follow

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
    @JvmStatic
    fun of(string: String): Follow {
      val formattedString = string.trim()
      formattedString.ifBlank { throw BlankStringException() }
      return when (formattedString) {
        Public.unfollowed().toString() -> Public.unfollowed()
        Public.following().toString() -> Public.following()
        Public.subscribed().toString() -> Public.subscribed()
        Private.unfollowed().toString() -> Private.unfollowed()
        Private.requested().toString() -> Private.requested()
        Private.following().toString() -> Private.following()
        Private.subscribed().toString() -> Private.subscribed()
        else -> throw InvalidFollowString(formattedString)
      }
    }
  }
}
