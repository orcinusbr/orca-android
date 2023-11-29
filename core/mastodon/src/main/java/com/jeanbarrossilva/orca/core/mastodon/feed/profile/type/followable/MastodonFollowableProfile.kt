package com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.followable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndPost
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import com.jeanbarrossilva.orca.core.mastodon.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.injector.Injector
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL

/**
 * [MastodonProfile] that can be followed.
 *
 * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
 *   [MastodonProfilePostPaginator] for paginating through the [MastodonProfile]'s [Post]s will be
 *   provided.
 */
internal data class MastodonFollowableProfile<T : Follow>(
  private val postPaginatorProvider: MastodonProfilePostPaginator.Provider,
  override val id: String,
  override val account: Account,
  override val avatarLoader: SomeImageLoader,
  override val name: String,
  override val bio: StyledString,
  override val follow: T,
  override val followerCount: Int,
  override val followingCount: Int,
  override val url: URL
) :
  Profile by MastodonProfile(
    postPaginatorProvider,
    id,
    account,
    avatarLoader,
    name,
    bio,
    followerCount,
    followingCount,
    url
  ),
  FollowableProfile<T>() {
  override suspend fun onChangeFollowTo(follow: T) {
    val toggledRoute = follow.getToggledRoute(this)
    Injector.get<SomeHttpInstance>().client.authenticateAndPost(toggledRoute)
  }
}
