package com.jeanbarrossilva.orca.core.http.feed.profile

import com.chrynan.paginate.core.loadAllPagesItems
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.pagination.HttpTootPaginateSource
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL
import kotlinx.coroutines.flow.Flow

/**
 * [Profile] whose [HttpToot]s are obtained through pagination performed by the
 * [tootPaginateSource].
 *
 * @param tootPaginateSourceProvider [ProfileTootPaginateSource] for paginating through the
 *   [HttpToot]s.
 */
internal data class HttpProfile(
  private val tootPaginateSourceProvider: ProfileTootPaginateSource.Provider,
  override val id: String,
  override val account: Account,
  override val avatarURL: URL,
  override val name: String,
  override val bio: StyledString,
  override val followerCount: Int,
  override val followingCount: Int,
  override val url: URL
) : Profile {
  private val tootPaginateSource = tootPaginateSourceProvider.provide(id)
  private val tootsFlow = tootPaginateSource.loadAllPagesItems(HttpTootPaginateSource.DEFAULT_COUNT)

  override suspend fun getToots(page: Int): Flow<List<Toot>> {
    tootPaginateSource.paginateTo(page)
    return tootsFlow
  }
}
