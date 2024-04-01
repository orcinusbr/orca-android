/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.Editor
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import com.jeanbarrossilva.orca.std.image.SomeImageLoader
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
