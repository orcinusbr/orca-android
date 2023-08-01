package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.persistence.entity

import androidx.annotation.IntDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status.TootPaginateSource
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.editable.MastodonEditableProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.followable.MastodonFollowableProfile
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
        val follow = Follow.of(checkNotNull(follow))
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
