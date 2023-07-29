package com.jeanbarrossilva.mastodonte.core.mastodon.profile

import com.jeanbarrossilva.mastodonte.core.mastodon.profile.cache.MastodonProfileStore
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.ProfileProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get
import kotlin.jvm.optionals.getOrNull

class MastodonProfileProvider(private val store: MastodonProfileStore) : ProfileProvider() {
    private val profileFlow = MutableSharedFlow<Profile>()

    override suspend fun contains(id: String): Boolean {
        val optional = store.get(id)
        val provided = optional.getOrNull()
        if (provided != null && provided != profileFlow.first()) {
            profileFlow.emit(optional.get())
        }
        return optional.isPresent
    }

    override suspend fun onProvide(id: String): Flow<Profile> {
        return profileFlow
    }
}
