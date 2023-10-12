package com.jeanbarrossilva.orca.core.feed.profile.type.editable

import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL

/** Edits an [EditableProfile]. */
interface Editor {
  /**
   * Sets [avatarURL] as the [EditableProfile]'s [avatarURL][EditableProfile.avatarURL].
   *
   * @param avatarURL Avatar [URL] to be set to the [EditableProfile]..
   */
  suspend fun setAvatarURL(avatarURL: URL)

  /**
   * Sets [name] as the [EditableProfile]'s [name][EditableProfile.name].
   *
   * @param name Name to be set to the [EditableProfile].
   */
  suspend fun setName(name: String)

  /**
   * Sets [bio] as the [EditableProfile]'s [bio][EditableProfile.bio].
   *
   * @param bio Bio to be set to the [EditableProfile].
   */
  suspend fun setBio(bio: StyledString)

  companion object {
    /** No-op, empty [Editor]. */
    val empty =
      object : Editor {
        override suspend fun setAvatarURL(avatarURL: URL) {}

        override suspend fun setName(name: String) {}

        override suspend fun setBio(bio: StyledString) {}
      }
  }
}
