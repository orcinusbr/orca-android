package com.jeanbarrossilva.mastodon.feature.profile.viewmodel

import com.jeanbarrossilva.loadable.Loadable
import java.io.Serializable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Converts this [Flow] into a [MutableStateFlow] that mirrors its emissions, with an initial
 * [loading][Loadable.Loading] [value][MutableStateFlow.value].
 *
 * @param scope [CoroutineScope] from which this [Flow] will be collected and its emissions will be
 * sent to the created [MutableStateFlow].
 **/
internal fun <T : Serializable?> Flow<Loadable<T>>.mutableStateIn(scope: CoroutineScope):
    MutableStateFlow<Loadable<T>> {
    return mutableStateIn(scope, Loadable.Loading())
}

/**
 * Converts this [Flow] into a [MutableStateFlow] that mirrors its emissions.
 *
 * @param scope [CoroutineScope] from which this [Flow] will be collected and its emissions will be
 * sent to the created [MutableStateFlow].
 * @param initialValue [Value][MutableStateFlow.value] that's initially held by the
 * [MutableStateFlow].
 **/
@Suppress("KDocUnresolvedReference")
internal fun <T> Flow<T>.mutableStateIn(scope: CoroutineScope, initialValue: T):
    MutableStateFlow<T> {
    return MutableStateFlow(initialValue).apply {
        scope.launch {
            this@mutableStateIn.collect(this@apply)
        }
    }
}
