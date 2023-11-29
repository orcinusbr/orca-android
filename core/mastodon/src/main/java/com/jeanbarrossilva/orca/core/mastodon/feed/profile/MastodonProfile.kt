package com.jeanbarrossilva.orca.core.mastodon.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL
import kotlinx.coroutines.flow.Flow

/**
 * [Profile] whose [Post]s are obtained through pagination performed by the [postPaginator].
 *
 * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
 *   [MastodonProfilePostPaginator] for paginating through the [Post]s will be provided.
 */
internal data class MastodonProfile(
  private val postPaginatorProvider: MastodonProfilePostPaginator.Provider,
  override val id: String,
  override val account: Account,
  override val avatarLoader: SomeImageLoader,
  override val name: String,
  override val bio: StyledString,
  override val followerCount: Int,
  override val followingCount: Int,
  override val url: URL
) : Profile {
  private lateinit var postPaginator: MastodonProfilePostPaginator

  override suspend fun getPosts(page: Int): Flow<List<Post>> {
    if (!::postPaginator.isInitialized) {
      postPaginator = postPaginatorProvider.provide(id)
    }
    return postPaginator.paginateTo(page)
  }
}
