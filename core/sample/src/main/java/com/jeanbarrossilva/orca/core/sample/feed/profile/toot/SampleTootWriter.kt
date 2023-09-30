package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable.replacingOnceBy
import kotlinx.coroutines.flow.update

/** Performs [Toot]-related writing operations. **/
object SampleTootWriter {
    /**
     * Defines whether the [Toot] identified as [id] is marked as favorite.
     *
     * @param id ID of the [Toot] whose [Toot.isFavorite] will be updated to [isFavorite].
     * @param isFavorite Whether the [Toot] is marked as favorite.
     **/
    fun updateFavorite(id: String, isFavorite: Boolean) {
        update(id) {
            copy(isFavorite = isFavorite)
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
            copy(isReblogged = isReblogged)
        }
    }

    /** Clears all added [Toot]s, including the default ones. **/
    fun clear() {
        SampleTootProvider.tootsFlow.update {
            emptyList()
        }
    }

    /** Resets this [SampleTootWriter] to its default state. **/
    fun reset() {
        SampleTootProvider.tootsFlow.value = SampleTootProvider.defaultToots
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
