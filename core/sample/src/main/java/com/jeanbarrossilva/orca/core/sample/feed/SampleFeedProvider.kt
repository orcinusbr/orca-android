package com.jeanbarrossilva.orca.core.sample.feed

import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/** [FeedProvider] that provides a feed for a sample [Profile]. **/
internal object SampleFeedProvider : FeedProvider() {
    /** [Flow] with the toots to be provided in the feed. **/
    private val tootsFlow = SampleTootProvider.tootsFlow.asStateFlow()

    override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
        return tootsFlow.map {
            it.chunked(SampleProfile.TOOTS_PER_PAGE).getOrElse(page) {
                emptyList()
            }
        }
    }

    override suspend fun containsUser(userID: String): Boolean {
        return userID == Profile.sample.id
    }
}
