package com.jeanbarrossilva.orca.core.http

import com.jeanbarrossilva.orca.core.http.instance.HttpInstance
import com.jeanbarrossilva.orca.core.http.instance.SomeHttpInstance
import java.io.Serializable

/**
 * Singleton through which non-[Serializable] HTTP-core-related structures can be defined and
 * retrieved.
 **/
object HttpBridge {
    /** [HttpInstance] through which HTTP-based core operations will be performed.. **/
    lateinit var instance: SomeHttpInstance
        private set

    /**
     * Crosses this [HttpBridge].
     *
     * @param instance [HttpInstance] to be set.
     **/
    fun cross(instance: SomeHttpInstance) {
        this.instance = instance
    }
}
