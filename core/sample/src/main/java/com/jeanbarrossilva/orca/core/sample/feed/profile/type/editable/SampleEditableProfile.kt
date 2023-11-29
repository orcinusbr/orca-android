package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.Editor
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL

/**
 * [SampleProfile] that's also editable.
 *
 * @param writer [SampleProfileWriter] through which the [editor] can edit this [EditableProfile].
 * @see EditableProfile
 */
internal data class SampleEditableProfile(
  override val id: String,
  override val account: Account,
  override var avatarLoader: SomeImageLoader,
  override var name: String,
  override var bio: StyledString,
  override val followerCount: Int,
  override val followingCount: Int,
  override val url: URL,
  override val postProvider: SamplePostProvider,
  private val writer: SampleProfileWriter
) : SampleProfile, EditableProfile() {
  override val editor: Editor by lazy { SampleEditor(writer, id) }
}
