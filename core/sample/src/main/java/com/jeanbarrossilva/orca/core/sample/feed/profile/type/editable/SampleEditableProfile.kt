package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.Editor
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfile
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL

/**
 * [SampleProfile] that's also editable.
 *
 * @see EditableProfile
 */
abstract class SampleEditableProfile private constructor() : SampleProfile, EditableProfile() {
  abstract override var avatarLoader: SomeImageLoader
  abstract override var name: String
  abstract override var bio: StyledString

  override val editor: Editor by lazy { SampleEditor(id) }

  override fun toString(): String {
    return "SampleEditableProfile(id=$id, account=$account, avatarLoader=$avatarLoader, " +
      "name=$name, bio=$bio, followerCount=$followerCount, followingCount=$followingCount, " +
      "url=$url)"
  }

  companion object {
    /**
     * Creates an instance of a [SampleEditableProfile].
     *
     * @param id Unique identifier.
     * @param account Unique identifier within an instance.
     * @param avatarLoader [ImageLoader] that loads the avatar.
     * @param name Name to be displayed.
     * @param bio Describes who the owner is and/or provides information regarding this
     *   [SampleEditableProfile].
     * @param followerCount Amount of followers.
     * @param followingCount Amount of following.
     * @param url [URL] that leads to the webpage of the instance through which this
     *   [SampleEditableProfile] can be accessed.
     */
    fun createInstance(
      id: String,
      account: Account,
      avatarLoader: SomeImageLoader,
      name: String,
      bio: StyledString,
      followerCount: Int,
      followingCount: Int,
      url: URL
    ): SampleEditableProfile {
      return object : SampleEditableProfile() {
        override val id = id
        override val account = account
        override var avatarLoader = avatarLoader
        override var name = name
        override var bio = bio
        override val followerCount = followerCount
        override val followingCount = followingCount
        override val url = url
      }
    }
  }
}
