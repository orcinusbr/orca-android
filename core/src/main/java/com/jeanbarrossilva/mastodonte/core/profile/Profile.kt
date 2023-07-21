package com.jeanbarrossilva.mastodonte.core.profile

import com.jeanbarrossilva.mastodonte.core.account.Account
import com.jeanbarrossilva.mastodonte.core.toot.Toot
import java.io.Serializable
import java.net.URL
import kotlinx.coroutines.flow.Flow

/** A user's profile. **/
interface Profile : Serializable {
    /** Unique identifier. **/
    val id: String

    /** Unique identifier within an instance. **/
    val account: Account

    /** [URL] that leads to the avatar image. **/
    val avatarURL: URL

    /** Name to be displayed. **/
    val name: String

    /** Describes who the owner is and/or provides information regarding this [Profile]. **/
    val bio: String

    /** Amount of followers. **/
    val followerCount: Int

    /** Amount of following. **/
    val followingCount: Int

    /**
     * [URL] that leads to the webpage of the instance through which this [Profile] can be accessed.
     **/
    val url: URL

    suspend fun getToots(page: Int): Flow<List<Toot>>

    companion object
}
