package com.jeanbarrossilva.orca.core.feed.profile.type.editable

import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString

/** Edits an [EditableProfile]. */
interface Editor {
  /**
   * Sets [avatarLoader] as the [EditableProfile]'s [avatarLoader][EditableProfile.avatarLoader].
   *
   * @param avatarLoader Avatar [ImageLoader] to be set to the [EditableProfile].
   */
  suspend fun setAvatarLoader(avatarLoader: SomeImageLoader)

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
        override suspend fun setAvatarLoader(avatarLoader: SomeImageLoader) {}

        override suspend fun setName(name: String) {}

        override suspend fun setBio(bio: StyledString) {}
      }
  }
}
