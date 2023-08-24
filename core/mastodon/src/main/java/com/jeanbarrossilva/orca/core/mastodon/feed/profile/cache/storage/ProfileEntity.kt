package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage

import androidx.annotation.IntDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.editable.MastodonEditableProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.followable.MastodonFollowableProfile
import java.net.URL

@Entity(tableName = "profiles")
data class ProfileEntity internal constructor(
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

    internal fun toProfile(
        tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
    ): Profile {
        return when (type) {
            EDITABLE_TYPE -> toMastodonEditableProfile(tootPaginateSourceProvider)
            FOLLOWABLE_TYPE -> toMastodonFollowableProfile(tootPaginateSourceProvider)
            else -> throw IllegalStateException("Unknown profile entity type: $type.")
        }
    }

    private fun toMastodonEditableProfile(
        tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
    ): MastodonEditableProfile {
        val account = Account.of(account)
        val avatarURL = URL(avatarURL)
        val url = URL(url)
        return MastodonEditableProfile(
            tootPaginateSourceProvider,
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

    private fun toMastodonFollowableProfile(
        tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
    ): MastodonFollowableProfile<Follow> {
        val account = Account.of(account)
        val avatarURL = URL(avatarURL)
        val follow = Follow.of(checkNotNull(follow))
        val url = URL(url)
        return MastodonFollowableProfile(
            tootPaginateSourceProvider,
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
