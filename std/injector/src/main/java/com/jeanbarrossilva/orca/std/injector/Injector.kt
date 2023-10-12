package com.jeanbarrossilva.orca.std.injector

import com.jeanbarrossilva.orca.std.injector.module.Module
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/** [Module] that enables global [Module] and dependency injection. */
object Injector : Module() {
  /** [Module]s that have been registered associated to their assigned types. */
  @PublishedApi internal val modularization = hashMapOf<KClass<out Module>, Module>()

  /** [IllegalArgumentException] thrown if the [Injector] registers itself. */
  class SelfRegistrationException @PublishedApi internal constructor() :
    IllegalArgumentException("Injector cannot register itself.")

  /** [IllegalArgumentException] thrown if the [Injector] gets itself. */
  class SelfRetrievalException @PublishedApi internal constructor() :
    IllegalArgumentException("Injector cannot get itself.")

  /**
   * [NoSuchElementException] thrown if a [Module] that hasn't been registered is requested to be
   * obtained.
   *
   * @param moduleClass [KClass] of the requested [Module].
   */
  class ModuleNotRegisteredException
  @PublishedApi
  internal constructor(moduleClass: KClass<out Module>) :
    NoSuchElementException("No module of type ${moduleClass.qualifiedName} has been injected.")

  /**
   * Registers the given [module].
   *
   * @param T [Module] to be associated to the given one.
   * @param module [Module] to be registered.
   * @throws SelfRegistrationException If the [module] is this [Injector].
   */
  inline fun <reified T : Module> register(module: T) {
    if (module != this) {
      registerWithoutSelfRegistrationInsurance(module)
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
   */
  @Throws(ModuleNotRegisteredException::class)
  inline fun <reified T : Module> from(): T {
    return if (T::class != Injector::class) {
      modularization[T::class] as T? ?: throw ModuleNotRegisteredException(T::class)
    } else {
      throw SelfRetrievalException()
    }
  }

  override fun onClear() {
    modularization.values.forEach(Module::clear)
    modularization.clear()
  }

  /**
   * Registers the given [module] without ensuring that this [Module] isn't injecting itself.
   *
   * @param T [Module] to be associated to the given one.
   * @param module [Module] to be registered.
   */
  @PublishedApi
  internal inline fun <reified T : Module> registerWithoutSelfRegistrationInsurance(module: T) {
    modularization[T::class] = module
    injectDeclaredDependenciesOf(module)
  }

  /**
   * Injects the dependencies declared within the given [module].
   *
   * @param T [Module] into which its dependencies will be injected.
   * @param module [Module] whose dependencies will be injected into it.
   */
  @PublishedApi
  internal inline fun <reified T : Module> injectDeclaredDependenciesOf(module: T) {
    T::class
      .memberProperties
      .filterIsInjection()
      .associateBy { it.returnType.arguments.last().type?.classifier }
      .mapKeys { (dependencyClassifier, _) -> dependencyClassifier as KClass<*> }
      .onEach { (_, property) -> property.isAccessible = true }
      .mapValues { (_, property) -> property.get(module) }
      .forEach { (dependencyClass, injection) ->
        module.inject(dependencyClass, injection.castTo())
      }
  }
}
