package com.jeanbarrossilva.mastodonte.core.sample.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.core.sample.profile.edit.replacingOnceBy
import kotlinx.coroutines.flow.update

/** Performs [Toot]-related writing operations. **/
internal object SampleTootWriter {
    /**
     * Defines whether the [Toot] identified as [id] is marked as favorite.
     *
     * @param id ID of the [Toot] whose [Toot.isFavorite] will be updated to [isFavorite].
     * @param isFavorite Whether the [Toot] is marked as favorite.
     **/
    fun updateFavorite(id: String, isFavorite: Boolean) {
        SampleTootProvider.tootsFlow.update { toots ->
            toots.replacingOnceBy({ (this as SampleToot).copy(isFavorite = isFavorite) }) { toot ->
                toot.id == id
            }
        }
    }
}
