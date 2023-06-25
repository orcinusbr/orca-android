package com.jeanbarrossilva.mastodonte.core.inmemory.profile.follow

import com.jeanbarrossilva.mastodonte.core.inmemory.profile.InMemoryProfile
import com.jeanbarrossilva.mastodonte.core.inmemory.profile.InMemoryProfileDao
import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile

abstract class InMemoryFollowableProfile<T : Follow> : InMemoryProfile, FollowableProfile<T>() {
    abstract override var follow: T
        internal set

    override suspend fun onChangeFollowTo(follow: T) {
        InMemoryProfileDao.updateFollow(id, follow)
    }
}
