package com.jeanbarrossilva.orca.core.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import java.io.Serializable
import java.net.URL

/**
 * Result of a profile search.
 *
 * @param id Unique identifier.
 * @param account Unique identifier within an instance.
 * @param avatarLoader [ImageLoader] that loads the avatar.
 * @param name Name to be displayed.
 * @param url [URL] that leads to the profile.
 */
data class ProfileSearchResult(
  val id: String,
  val account: Account,
  val avatarLoader: SomeImageLoader,
  val name: String,
  val url: URL
) : Serializable {
  companion object
}
