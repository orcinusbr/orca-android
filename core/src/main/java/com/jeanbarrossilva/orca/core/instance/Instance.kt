package com.jeanbarrossilva.orca.core.instance

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider

/** An [Instance] with a generic [Authenticator]. **/
typealias SomeInstance = Instance<*>

/**
 * Site at which the user is, from which all operations can be performed.
 *
 * @param T [Authenticator] to authenticate the user with.
 **/
interface Instance<T : Authenticator> {
    /** [Instance]-specific [Authenticator] through which authentication can be done. **/
    val authenticator: T

    /**
     * [Instance]-specific [AuthenticationLock] by which features can be locked or unlocked by an
     * authentication "wall".
     **/
    val authenticationLock: AuthenticationLock<T>

    /** [Instance]-specific [FeedProvider] that provides the [Toot]s in the timeline. **/
    val feedProvider: FeedProvider

    /** [Instance]-specific [ProfileProvider] for providing [Profile]s. **/
    val profileProvider: ProfileProvider

    /** [Instance]-specific [ProfileSearcher] by which search for [Profile]s can be made. **/
    val profileSearcher: ProfileSearcher

    /** [Instance]-specific [TootProvider] that provides [Toot]s. **/
    val tootProvider: TootProvider

    companion object
}
