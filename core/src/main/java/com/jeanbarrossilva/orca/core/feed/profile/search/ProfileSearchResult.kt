package com.jeanbarrossilva.orca.core.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import java.net.URL

/**
 * Result of a profile search.
 *
 * @param id Unique identifier.
 * @param account Unique identifier within an instance.
 * @param avatarURL [URL] that leads to the avatar image.
 * @param name Name to be displayed.
 **/
data class ProfileSearchResult(
    val id: String,
    val account: Account,
    val avatarURL: URL,
    val name: String
) {
    companion object
}
