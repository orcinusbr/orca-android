package com.jeanbarrossilva.orca.core.feed

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Filters each element of the [Collection]s emitted to this [Flow].
 *
 * @param predicate Whether the currently iterated element should be in the filtered [List].
 **/
internal fun <T> Flow<Collection<T>>.filterEach(predicate: (T) -> Boolean): Flow<List<T>> {
    return map {
        it.filter(predicate)
    }
}
