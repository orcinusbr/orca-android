package com.jeanbarrossilva.mastodonte.core.mastodon.profile

import com.dropbox.android.external.store4.get
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.flow.loadableFlow
import com.jeanbarrossilva.loadable.flow.unwrap
import com.jeanbarrossilva.mastodonte.core.mastodon.profile.cache.MastodonProfileStore
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.ProfileProvider
import kotlin.jvm.optionals.getOrNull
import kotlinx.coroutines.flow.Flow

class MastodonProfileProvider(private val store: MastodonProfileStore) : ProfileProvider() {
    private val loadableProfileFlow = loadableFlow<Profile>()

    /*
     * Since `contains` is called before `onProvide` each time, we reuse the obtained profile by
     * emitting it to `loadableProfileFlow` and later returning it at `onProvide`.
     */
    override suspend fun contains(id: String): Boolean {
        val optional = store.get(id)
        val provided = optional.getOrNull()
        val contains = provided != null
        if (contains) {
            loadableProfileFlow.emit(
                /*
                 * Null-asserting is safe because of `contains`, that is solely a check on whether
                 * `provided` is null; since that condition has to be met for us to reach this
                 * block, it is highly unlikely (I'd like to say impossible, but who knows 🫠)
                 * that a NullPointerException will get thrown.
                 */
                Loadable.Loaded(provided!!)
            )
        }
        return contains
    }

    override suspend fun onProvide(id: String): Flow<Profile> {
        return loadableProfileFlow.unwrap()
    }
}
