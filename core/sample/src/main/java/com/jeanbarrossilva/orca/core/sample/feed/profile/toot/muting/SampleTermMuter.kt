package com.jeanbarrossilva.orca.core.sample.feed.profile.toot.muting

import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import kotlinx.coroutines.flow.MutableStateFlow

/** An in-memory [TermMuter]. **/
@Suppress("FunctionName")
fun SampleTermMuter(): TermMuter {
    val termsFlow = MutableStateFlow(emptyList<String>())
    return TermMuter {
        getTerms { termsFlow }
        mute { termsFlow.value += it }
        unmute { termsFlow.value -= it }
    }
}
