package com.jeanbarrossilva.mastodonte.core.sample.profile.edit

import com.jeanbarrossilva.mastodonte.core.profile.edit.Editor
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileDao
import java.net.URL

/** [Editor] that edits [SampleEditableProfile]s. **/
internal class SampleEditor(private val id: String) : Editor {
    override suspend fun setAvatarURL(avatarURL: URL) {
        edit {
            this.avatarURL = avatarURL
        }
    }

    override suspend fun setName(name: String) {
        edit {
            this.name = name
        }
    }

    override suspend fun setBio(bio: String) {
        edit {
            this.bio = bio
        }
    }

    /**
     * Applies the [edit] to the [SampleEditableProfile] whose [ID][SampleEditableProfile.id]
     * matches [id].
     *
     * @param edit Editing to be made to the matching [SampleEditableProfile].
     **/
    private inline fun edit(crossinline edit: SampleEditableProfile.() -> Unit) {
        SampleProfileDao.profilesFlow.value = SampleProfileDao
            .profilesFlow
            .value
            .filterIsInstance<SampleEditableProfile>()
            .replacingOnceBy({ apply(edit) }) { it.id == id }
    }
}
