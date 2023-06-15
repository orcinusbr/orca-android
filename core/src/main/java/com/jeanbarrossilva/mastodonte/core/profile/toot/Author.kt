package com.jeanbarrossilva.mastodonte.core.profile.toot

import java.net.URL

/**
 * User that's authored a [Toot].
 *
 * @param id Unique identifier.
 * @param avatarURL [URL] that leads to the avatar image.
 * @param name Name to be displayed.
 * @param account Absolute account, containing the username and the instance.
 * @param profileURL [URL] that leads to this [Author]'s profile.
 **/
data class Author(
    val id: String,
    val avatarURL: URL,
    val name: String,
    val account: String,
    val profileURL: URL
)
