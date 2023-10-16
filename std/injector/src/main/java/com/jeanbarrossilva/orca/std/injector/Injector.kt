package com.jeanbarrossilva.orca.std.injector

import com.jeanbarrossilva.orca.std.injector.binding.Binding
import com.jeanbarrossilva.orca.std.injector.binding.SomeBinding
import com.jeanbarrossilva.orca.std.injector.binding.bind
import com.jeanbarrossilva.orca.std.injector.module.Module
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/** [Module] that enables global [Module] and dependency injection. */
object Injector : Module() {
  /** [Binding]s that have been registered associated to their assigned types. */
  @PublishedApi internal val modularization = hashMapOf<SomeBinding, Module>()

  /** [IllegalArgumentException] thrown if the [Injector] registers itself. */
  class SelfRegistrationException @PublishedApi internal constructor() :
    IllegalArgumentException("Injector cannot register itself.")

  /** [IllegalArgumentException] thrown if the [Injector] gets itself. */
  class SelfRetrievalException @PublishedApi internal constructor() :
    IllegalArgumentException("Injector cannot get itself.")

  /**
   * [NoSuchElementException] thrown if a [Module] that hasn't been registered is requested to be
   * obtained or unregistered.
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
    register(module, bind())
  }

  /**
   * Registers the given [module], associating it to the [binding].
   *
   * @param B Base [Module] to which a [Module] is bound to.
   * @param A [Module] at the utmost bottom of the inheritance tree to which a [Module] is bound to.
   * @param module [Module] to be registered.
   * @param binding [Binding] to which the [module] will be associated.
   * @throws SelfRegistrationException If the [module] is this [Injector].
   */
  inline fun <reified B : Module, reified A : B> register(module: B, binding: Binding<B, A>) {
    if (module != this) {
      registerWithoutSelfRegistrationInsurance(module, binding)
    } else {
      throw SelfRegistrationException()
    }
  }

  /**
   * Gets the injected [Module] of type [T].
   *
   * @param T [Module] to be obtained.
   * @throws SelfRetrievalException If the [Module] is this [Injector].
   * @throws ModuleNotRegisteredException If no [Module] of type [T] has been registered.
   */
  @Throws(ModuleNotRegisteredException::class)
  inline fun <reified T : Module> from(): T {
    return if (T::class != Injector::class) {
      modularization.filterKeys { T::class in it }.values.singleOrNull() as T?
        ?: throw ModuleNotRegisteredException(T::class)
    } else {
      throw SelfRetrievalException()
    }
  }

  /**
   * Unregisters the given [Module].
   *
   * @param T [Module] to be unregistered.
   * @throws ModuleNotRegisteredException If no [Module] of type [T] has been injected.
   */
  @Throws(ModuleNotRegisteredException::class)
  inline fun <reified T : Module> unregister() {
    modularization.keys.find { T::class in it }?.let(modularization::remove)
      ?: throw ModuleNotRegisteredException(T::class)
  }

  override fun onClear() {
    modularization.values.forEach(Module::clear)
    modularization.clear()
  }

  /**
   * Registers the given [module] without ensuring that this [Module] isn't injecting itself,
   * associating it to the [binding].
   *
   * @param B [Module] to be associated to the given one.
   * @param module [Module] to be registered.
   * @param binding [Binding] to which the [module] will be associated.
   */
  @PublishedApi
  internal inline fun <reified B : Module, reified A : B> registerWithoutSelfRegistrationInsurance(
    module: B,
    binding: Binding<B, A>
  ) {
    modularization[binding] = module
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
