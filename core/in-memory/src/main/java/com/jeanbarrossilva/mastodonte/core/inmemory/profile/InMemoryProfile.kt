package com.jeanbarrossilva.mastodonte.core.inmemory.profile

import com.jeanbarrossilva.mastodonte.core.inmemory.profile.toot.InMemoryTootDao
import com.jeanbarrossilva.mastodonte.core.profile.Follow
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.toot.Account
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import java.net.URL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** [InMemoryProfile] whose [Follow] status type is not taken into consideration. **/
typealias AnyInMemoryProfile = InMemoryProfile<*>

/** [Profile] whose operations are performed in memory. **/
class InMemoryProfile<T : Follow>(
    override val id: String,
    override val account: Account,
    override val avatarURL: URL,
    override val name: String,
    override val bio: String,
    follow: T,
    override val followerCount: Int,
    override val followingCount: Int,
    override val url: URL
) : Profile<T>() {
    override var follow = follow
        internal set

    override suspend fun onChangeFollowTo(follow: T) {
        InMemoryProfileDao.updateFollow(id, follow)
    }

    override fun getToots(page: Int): Flow<List<Toot>> {
        return flowOf(InMemoryTootDao.selectAll().windowed(50)[page])
    }
}
