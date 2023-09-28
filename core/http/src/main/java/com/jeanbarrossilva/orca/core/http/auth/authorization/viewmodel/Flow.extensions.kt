package com.jeanbarrossilva.orca.core.http.auth.authorization.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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
