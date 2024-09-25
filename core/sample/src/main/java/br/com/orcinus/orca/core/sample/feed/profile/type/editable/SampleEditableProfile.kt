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

package br.com.orcinus.orca.core.sample.feed.profile.type.editable

import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.type.editable.EditableProfile
import br.com.orcinus.orca.core.feed.profile.type.editable.Editor
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfile
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composer
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composers
import br.com.orcinus.orca.std.markdown.Markdown

/**
 * [SampleProfile] that's also editable.
 *
 * @param provider [SampleProfileProvider] through which the [editor] can perform edits.
 * @see EditableProfile
 */
class SampleEditableProfile
internal constructor(
  private val provider: SampleProfileProvider,
  private val delegate: Author,
  override var bio: Markdown,
  override val followerCount: Int,
  override val followingCount: Int
) :
  EditableProfile(),
  Composer by object : SampleProfile(delegate) {
    override var name = super.name
    override val bio = bio
    override val followerCount = followerCount
    override val followingCount = followingCount
  } {
  override val editor: Editor by lazy { SampleEditor(provider, id) }

  override fun equals(other: Any?): Boolean {
    return other is SampleEditableProfile && Composers.equals(this, other) && editor == other.editor
  }

  override fun hashCode(): Int {
    return Composers.hash(this, editor)
  }
}
