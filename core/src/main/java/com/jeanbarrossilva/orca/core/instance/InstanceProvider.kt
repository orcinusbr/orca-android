package com.jeanbarrossilva.orca.core.instance

/** Provides an [Instance] through [provide]. **/
interface InstanceProvider {
    /** Provides an [Instance]. **/
    fun provide(): SomeInstance

    companion object
}
