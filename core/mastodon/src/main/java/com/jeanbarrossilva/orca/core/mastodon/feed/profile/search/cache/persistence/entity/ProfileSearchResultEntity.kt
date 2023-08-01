package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import java.net.URL

@Entity(tableName = "profile_search_results")
data class ProfileSearchResultEntity(
    @PrimaryKey internal val query: String,
    internal val id: String,
    internal val account: String,
    @ColumnInfo(name = "avatar_url") internal val avatarURL: String,
    internal val name: String,
    internal val url: String
) {
    internal fun toProfileSearchResult(): ProfileSearchResult {
        val account = Account.of(account, "mastodon.social")
        val avatarURL = URL(avatarURL)
        val url = URL(url)
        return ProfileSearchResult(id, account, avatarURL, name, url)
    }
}
