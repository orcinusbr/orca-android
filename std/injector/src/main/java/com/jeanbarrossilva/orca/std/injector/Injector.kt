package com.jeanbarrossilva.orca.std.injector

import kotlin.reflect.KClass

/** [Module] that enables global [Module] and dependency injection. **/
object Injector : Module() {
    /** [Module]s that have been registered associated to their assigned types. **/
    @PublishedApi
    internal val modularization = hashMapOf<KClass<out Module>, Module>()

    /** [IllegalArgumentException] thrown if the [Injector] registers itself. **/
    class SelfRegistrationException
    @PublishedApi
    internal constructor() : IllegalArgumentException("Injector cannot register itself.")

    /** [IllegalArgumentException] thrown if the [Injector] gets itself. **/
    class SelfRetrievalException
    @PublishedApi
    internal constructor() : IllegalArgumentException("Injector cannot get itself.")

    /**
     * [NoSuchElementException] thrown if a [Module] that hasn't been registered is requested to be
     * obtained.
     *
     * @param moduleClass [KClass] of the requested [Module].
     **/
    class ModuleNotRegisteredException
    @PublishedApi
    internal constructor(moduleClass: KClass<out Module>) :
        NoSuchElementException("No module of type ${moduleClass.qualifiedName} has been injected.")

    /**
     * Registers the given [module].
     *
     * @param T [Module] to be registered.
     * @param module [Module] to be registered.
     * @throws SelfRegistrationException If the [module] is this [Injector].
     **/
    inline fun <reified T : Module> register(module: T) {
        if (module != this) {
            modularization[T::class] = module
        } else {
            throw SelfRegistrationException()
        }
    }

    /**
     * Gets the injected [Module] of type [T].
     *
     * @param T [Module] to be obtained.
     * @throws SelfRetrievalException If the [Module] is this [Injector].
     * @throws ModuleNotRegisteredException If no [Module] of type [T] has been injected.
     **/
    @Throws(ModuleNotRegisteredException::class)
    inline fun <reified T : Module> from(): T {
        return if (T::class != Injector::class) {
            modularization[T::class] as T? ?: throw ModuleNotRegisteredException(T::class)
        } else {
            throw SelfRetrievalException()
        }
    }

    override fun onClear() {
        modularization.clear()
    }
}
