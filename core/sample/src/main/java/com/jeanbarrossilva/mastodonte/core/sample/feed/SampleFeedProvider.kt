package com.jeanbarrossilva.mastodonte.core.sample.feed

import com.jeanbarrossilva.mastodonte.core.feed.FeedProvider
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.sample
import com.jeanbarrossilva.mastodonte.core.sample.toot.SampleTootProvider
import com.jeanbarrossilva.mastodonte.core.toot.Toot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/** [FeedProvider] that provides a feed for a sample [Profile]. **/
object SampleFeedProvider : FeedProvider() {
    /** [Flow] with the toots to be provided in the feed. **/
    private val tootsFlow = SampleTootProvider.tootsFlow.asStateFlow()

    override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
        return tootsFlow.map {
            it.chunked(SampleProfile.TOOTS_PER_PAGE)[page]
        }
    }

    override suspend fun containsUser(userID: String): Boolean {
        return userID == Profile.sample.id
    }
}
