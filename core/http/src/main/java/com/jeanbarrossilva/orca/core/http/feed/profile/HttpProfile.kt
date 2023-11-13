package com.jeanbarrossilva.orca.core.http.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL
import kotlinx.coroutines.flow.Flow

/**
 * [Profile] whose [Toot]s are obtained through pagination performed by the [tootPaginator].
 *
 * @param tootPaginatorProvider [HttpProfileTootPaginator.Provider] by which a
 *   [HttpProfileTootPaginator] for paginating through the [Toot]s will be provided.
 */
internal data class HttpProfile(
  private val tootPaginatorProvider: HttpProfileTootPaginator.Provider,
  override val id: String,
  override val account: Account,
  override val avatarLoader: SomeImageLoader,
  override val name: String,
  override val bio: StyledString,
  override val followerCount: Int,
  override val followingCount: Int,
  override val url: URL
) : Profile {
  private lateinit var tootPaginator: HttpProfileTootPaginator

  override suspend fun getToots(page: Int): Flow<List<Toot>> {
    if (!::tootPaginator.isInitialized) {
      tootPaginator = tootPaginatorProvider.provide(id)
    }
    return tootPaginator.paginateTo(page)
  }
}
