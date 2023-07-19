package com.jeanbarrossilva.mastodonte.core.sample.toot

import com.jeanbarrossilva.mastodonte.core.sample.profile.edit.replacingOnceBy
import com.jeanbarrossilva.mastodonte.core.toot.Toot
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
        update(id) {
            (this as SampleToot).copy(isFavorite = isFavorite)
        }
    }

    /**
     * Defines whether the [Toot] identified as [id] is reblogged.
     *
     * @param id ID of the [Toot] whose [Toot.isReblogged] will be updated to [isReblogged].
     * @param isReblogged Whether the [Toot] is reblogged.
     **/
    fun updateReblogged(id: String, isReblogged: Boolean) {
        update(id) {
            (this as SampleToot).copy(isReblogged = isReblogged)
        }
    }

    /**
     * Replaces the currently existing [Toot] identified as [id] by its updated version.
     *
     * @param id ID of the [Toot] to be updated.
     * @param update Changes to be made to the existing [Toot].
     * @throws SampleTootProvider.NonexistentTootException If no [Toot] with such [ID][Toot.id]
     * exists.
     **/
    private fun update(id: String, update: Toot.() -> Toot) {
        SampleTootProvider.tootsFlow.update { toots ->
            toots.replacingOnceBy(update) { toot ->
                toot.id == id
            }
        }
    }
}
