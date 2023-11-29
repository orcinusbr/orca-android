package com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable

import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL

/**
 * [SampleProfile] that's also followable.
 *
 * @param writer [SampleProfileWriter] through which the [Follow] status can be updated.
 * @see FollowableProfile
 */
internal data class SampleFollowableProfile<T : Follow>(
  override val id: String,
  override val account: Account,
  override val avatarLoader: SomeImageLoader,
  override val name: String,
  override val bio: StyledString,
  override val follow: T,
  override val followerCount: Int,
  override val followingCount: Int,
  override val url: URL,
  val writer: SampleProfileWriter,
  override val postProvider: SamplePostProvider
) : SampleProfile, FollowableProfile<T>() {
  override suspend fun onChangeFollowTo(follow: T) {
    writer.updateFollow(id, follow)
  }
}
