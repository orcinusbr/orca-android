package com.jeanbarrossilva.mastodonte.core.profile.edit

/** Edits an [EditableProfile]. **/
interface Editor {
    /**
     * Sets the avatar to the base-64-encoded [avatar].
     *
     * @param avatar Base 64 [String] version of the avatar.
     **/
    suspend fun setAvatar(avatar: String)

    /**
     * Sets [name] as the [EditableProfile]'s [name][EditableProfile.name].
     *
     * @param name Name to be set to the [EditableProfile].
     **/
    suspend fun setName(name: String)

    /**
     * Sets [bio] as the [EditableProfile]'s [bio][EditableProfile.bio].
     *
     * @param bio Bio to be set to the [EditableProfile].
     **/
    suspend fun setBio(bio: String)

    /**
     * Makes the [EditableProfile] public or private according to [isPrivate].
     *
     * @param isPrivate Whether the [EditableProfile] is private.
     **/
    suspend fun setPrivate(isPrivate: Boolean)

    companion object {
        /** No-op, empty [Editor]. **/
        val empty = object : Editor {
            override suspend fun setAvatar(avatar: String) {
            }

            override suspend fun setName(name: String) {
            }

            override suspend fun setBio(bio: String) {
            }

            override suspend fun setPrivate(isPrivate: Boolean) {
            }
        }
    }
}
