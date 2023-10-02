package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import kotlinx.coroutines.flow.update

/** Performs [Toot]-related writing operations. **/
object SampleTootWriter {
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
}
