package com.jeanbarrossilva.mastodonte.core.sample.profile.edit

import com.jeanbarrossilva.mastodonte.core.profile.edit.EditableProfile
import com.jeanbarrossilva.mastodonte.core.profile.toot.Account
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfile
import java.net.URL

/**
 * [SampleProfile] that's also editable.
 *
 * @see EditableProfile
 **/
internal abstract class SampleEditableProfile private constructor() :
    SampleProfile, EditableProfile() {
    abstract override var avatarURL: URL
    abstract override var name: String
    abstract override var bio: String

    override val editor by lazy {
        SampleEditor(id)
    }

    override fun toString(): String {
        return "SampleEditableProfile(id=$id, account=$account, avatarURL=$avatarURL, " +
            "name=$name, bio=$bio, followerCount=$followerCount, followingCount=$followingCount, " +
            "url=$url)"
    }

    companion object {
        /**
         * Creates an instance of a [SampleEditableProfile].
         *
         * @param id Unique identifier.
         * @param account Unique identifier within an instance.
         * @param avatarURL [URL] that leads to the avatar image.
         * @param name Name to be displayed.
         * @param bio Describes who the owner is and/or provides information regarding this
         * [SampleEditableProfile].
         * @param followerCount Amount of followers.
         * @param followingCount Amount of following.
         * @param url [URL] that leads to the webpage of the instance through which this
         * [SampleEditableProfile] can be accessed.
         **/
        fun createInstance(
            id: String,
            account: Account,
            avatarURL: URL,
            name: String,
            bio: String,
            followerCount: Int,
            followingCount: Int,
            url: URL
        ): SampleEditableProfile {
            return object : SampleEditableProfile() {
                override val id = id
                override val account = account
                override var avatarURL = avatarURL
                override var name = name
                override var bio = bio
                override val followerCount = followerCount
                override val followingCount = followingCount
                override val url = url
            }
        }
    }
}
