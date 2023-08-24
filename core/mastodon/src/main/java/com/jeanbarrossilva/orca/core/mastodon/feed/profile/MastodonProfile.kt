package com.jeanbarrossilva.orca.core.mastodon.feed.profile

import com.chrynan.paginate.core.loadAllPagesItems
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status.TootPaginateSource
import java.net.URL
import kotlinx.coroutines.flow.Flow

internal data class MastodonProfile(
    private val tootPaginateSourceProvider: ProfileTootPaginateSource.Provider,
    override val id: String,
    override val account: Account,
    override val avatarURL: URL,
    override val name: String,
    override val bio: String,
    override val followerCount: Int,
    override val followingCount: Int,
    override val url: URL
) : Profile {
    private val tootPaginateSource = tootPaginateSourceProvider.provide(id)
    private val tootsFlow = tootPaginateSource.loadAllPagesItems(TootPaginateSource.DEFAULT_COUNT)

    override suspend fun getToots(page: Int): Flow<List<Toot>> {
        tootPaginateSource.paginateTo(page)
        return tootsFlow
    }
}
