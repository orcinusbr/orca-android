package com.jeanbarrossilva.orca.core.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import java.io.Serializable
import java.net.URL

/**
 * User that's authored a [Toot].
 *
 * @param id Unique identifier.
 * @param avatarURL [URL] that leads to the avatar image.
 * @param name Name to be displayed.
 * @param account Unique identifier within an instance.
 * @param profileURL [URL] that leads to this [Author]'s profile.
 **/
data class Author(
    val id: String,
    val avatarURL: URL,
    val name: String,
    val account: Account,
    val profileURL: URL
) : Serializable {
    companion object
}
