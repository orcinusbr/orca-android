package com.jeanbarrossilva.orca.platform.ui.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.runningFold

/**
 * Maps each element of the emitted [Collection]s to the resulting [Flow] of [transform], merging
 * and folding them into an up-to-date [List] that gets emitted each time any of these [Flow]s
 * receive an emission.
 *
 * @param transform Transformation to be made to the currently iterated element.
 **/
@OptIn(ExperimentalCoroutinesApi::class)
fun <I, O> Flow<Collection<I>>.flatMapEach(transform: suspend (I) -> Flow<O>): Flow<List<O>> {
    return mapEach(transform).flatMapLatest { flows ->
        flows.merge().runningFold(emptyList()) { accumulator, element ->
            accumulator + element
        }
    }
}

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
