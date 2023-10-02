package com.jeanbarrossilva.orca.core.feed.profile.toot.stat.test

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEmpty

/** Terminal operator that returns whether this [Flow] is empty. **/
internal suspend fun Flow<*>.isEmpty(): Boolean {
    var isEmpty = false
    onEmpty { isEmpty = true }.collect()
    return isEmpty
}
