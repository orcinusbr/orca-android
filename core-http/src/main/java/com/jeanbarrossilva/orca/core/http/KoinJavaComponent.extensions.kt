package com.jeanbarrossilva.orca.core.http

import org.koin.java.KoinJavaComponent

/**
 * Retrieves the [T] dependency.
 *
 * @param T Dependency to be retrieved.
 **/
inline fun <reified T> get(): T {
    return KoinJavaComponent.get(T::class.java)
}
