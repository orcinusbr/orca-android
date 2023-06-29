package com.jeanbarrossilva.mastodonte.core.sample.profile

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.core.sample.profile.toot.SampleTootDao
import com.jeanbarrossilva.mastodonte.core.sample.profile.toot.sample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

/** [Profile] whose operations are performed in memory and serves as a sample. **/
internal interface SampleProfile : Profile {
    override suspend fun getToots(page: Int): Flow<List<Toot>> {
        return SampleTootDao.getByAuthorID(id).filterNotNull().map {
            it.windowed(TOOTS_PER_PAGE, partialWindows = true)[page]
        }
    }

    companion object {
        /** Maximum amount of [Toot]s emitted to [getToots]. **/
        const val TOOTS_PER_PAGE = 50
    }
}
