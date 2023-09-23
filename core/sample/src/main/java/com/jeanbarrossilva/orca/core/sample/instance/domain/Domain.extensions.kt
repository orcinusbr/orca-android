package com.jeanbarrossilva.orca.core.sample.instance.domain

import com.jeanbarrossilva.orca.core.instance.domain.Domain

/** [Domain] returned by [sample]. **/
private val sampleDomain = Domain("instance.sample")

/** Sample [Domain]. **/
val Domain.Companion.sample
    get() = sampleDomain
