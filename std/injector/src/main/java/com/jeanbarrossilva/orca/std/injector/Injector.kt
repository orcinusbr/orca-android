package com.jeanbarrossilva.orca.std.injector

import com.jeanbarrossilva.orca.std.injector.Injector.get
import com.jeanbarrossilva.orca.std.injector.Injector.inject
import kotlin.reflect.KClass

/** Enables dependency injection through [inject] and [get]. **/
object Injector {
    /** Dependencies that have been injected associated to their assigned types. **/
    @PublishedApi
    internal val injections = hashMapOf<KClass<*>, () -> Any>()

    /**
     * Injects the given [dependency].
     *
     * @param T Dependency to be injected.
     * @param dependency Returns the dependency to be injected.
     **/
    inline fun <reified T : Any> inject(noinline dependency: Injector.() -> T) {
        if (T::class !in injections) {
            injections[T::class] = {
                @Suppress("UNUSED_EXPRESSION")
                dependency()
            }
        }
    }

    /**
     * Obtains the injected dependency whose type is [T].
     *
     * @param T Dependency to be obtained.
     * @throws NoSuchElementException If no dependency of type [T] has been injected.
     **/
    @Throws(NoSuchElementException::class)
    inline fun <reified T : Any> get(): T {
        return injections[T::class]?.invoke() as T? ?: throw dependencyNotInjected<T>()
    }

    /** Removes all injected dependencies. **/
    fun clear() {
        injections.clear()
    }

    /**
     * Returns the [NoSuchElementException] to be thrown when a dependency of type [T] is requested
     * to be obtained but hasn't been injected.
     *
     * @param T Dependency that's been requested to be obtained.
     **/
    @PublishedApi
    internal inline fun <reified T : Any> dependencyNotInjected(): NoSuchElementException {
        return NoSuchElementException(
            "No dependency of type ${T::class.qualifiedName} has been injected."
        )
    }
}
