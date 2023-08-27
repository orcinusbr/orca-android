package com.jeanbarrossilva.orca.platform.ui.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Maps each element of the emitted [Collection]s to the result of [transform].
 *
 * @param transform Transformation to be made to the currently iterated element.
 **/
fun <I, O> Flow<Collection<I>>.mapEach(transform: suspend (I) -> O): Flow<List<O>> {
    return map { elements ->
        elements.map { element ->
            transform(element)
        }
    }
}
