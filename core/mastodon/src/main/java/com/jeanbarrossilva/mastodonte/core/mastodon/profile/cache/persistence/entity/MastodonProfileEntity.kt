package com.jeanbarrossilva.mastodonte.core.mastodon.profile.cache.persistence.entity

import androidx.annotation.IntDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanbarrossilva.mastodonte.core.account.Account
import com.jeanbarrossilva.mastodonte.core.mastodon.profile.type.editable.MastodonEditableProfile
import com.jeanbarrossilva.mastodonte.core.mastodon.profile.type.followable.MastodonFollowableProfile
import com.jeanbarrossilva.mastodonte.core.mastodon.toot.status.TootPaginateSource
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.type.followable.Follow
import java.net.URL

@Entity(tableName = "profiles")
data class MastodonProfileEntity internal constructor(
    @PrimaryKey internal val id: String,
    internal val account: String,
    @ColumnInfo(name = "avatar_url") internal val avatarURL: String,
    internal val name: String,
    internal val bio: String,
    @Type internal val type: Int,
    internal val follow: String?,
    @ColumnInfo(name = "follower_count") internal val followerCount: Int,
    @ColumnInfo(name = "following_count") internal val followingCount: Int,
    internal val url: String
) {
    @IntDef(EDITABLE_TYPE, FOLLOWABLE_TYPE)
    internal annotation class Type

    internal fun toProfile(tootPaginateSource: TootPaginateSource): Profile {
        return when (type) {
            EDITABLE_TYPE -> toMastodonEditableProfile(tootPaginateSource)
            FOLLOWABLE_TYPE -> toMastodonFollowableProfile(tootPaginateSource)
            else -> throw IllegalStateException("Unknown profile entity type: $type.")
        }
    }

    private fun toMastodonEditableProfile(tootPaginateSource: TootPaginateSource):
            MastodonEditableProfile {
        val account = Account.of(account)
        val avatarURL = URL(avatarURL)
        val url = URL(url)
        return MastodonEditableProfile(
            tootPaginateSource,
            id,
            account,
            avatarURL,
            name,
            bio,
            followerCount,
            followingCount,
            url
        )
    }

    private fun toMastodonFollowableProfile(tootPaginateSource: TootPaginateSource):
            MastodonFollowableProfile<Follow> {
        val account = Account.of(account)
        val avatarURL = URL(avatarURL)
        val follow = when (follow) {
            Follow.Public.unfollowed().toString() -> Follow.Public.unfollowed()
            Follow.Public.following().toString() -> Follow.Public.following()
            Follow.Private.unfollowed().toString() -> Follow.Private.unfollowed()
            Follow.Private.requested().toString() -> Follow.Private.requested()
            Follow.Private.following().toString() -> Follow.Private.following()
            else -> throw IllegalStateException("No Follow matches \"$follow\".")
        }
        val url = URL(url)
        return MastodonFollowableProfile(
            tootPaginateSource,
            id,
            account,
            avatarURL,
            name,
            bio,
            follow,
            followerCount,
            followingCount,
            url
        )
    }

    companion object {
        internal const val EDITABLE_TYPE = 0
        internal const val FOLLOWABLE_TYPE = 1
    }
}
