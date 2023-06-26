package com.jeanbarrossilva.mastodonte.core.inmemory.profile

import com.jeanbarrossilva.mastodonte.core.inmemory.profile.toot.InMemoryTootDao
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

/** [Profile] whose operations are performed in memory. **/
interface InMemoryProfile : Profile {
    override suspend fun getToots(page: Int): Flow<List<Toot>> {
        return InMemoryTootDao.getByAuthorID(id).filterNotNull().map {
            it.windowed(50)[page]
        }
    }
}
