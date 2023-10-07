package com.jeanbarrossilva.orca.std.injector

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.injector.module.Dependency
import com.jeanbarrossilva.orca.std.injector.module.Module
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import kotlin.test.Test
import org.junit.Rule

internal class InjectorTests {
    @get:Rule
    val injectorRule = InjectorTestRule()

    private abstract class SuperModuleWithAnnotatedDependency(
        @Dependency private val dependency: Module.() -> Int
    ) : Module()

    private abstract class SuperModuleWithNonAnnotatedDependency(
        @Suppress("unused") private val dependency: Module.() -> Int
    ) : Module()

    private class SubModuleWithAnnotatedDependency : SuperModuleWithAnnotatedDependency({ 0 })

    private class SubModuleWithNonAnnotatedDependency :
        SuperModuleWithNonAnnotatedDependency({ 0 })

    @Test(expected = Injector.SelfRegistrationException::class)
    fun throwsWhenRegisteringItself() {
        Injector.register(Injector)
    }

    @Test
    fun registersModule() {
        val module = object : Module() { }
        Injector.register<Module>(module)
        assertThat(Injector.from<Module>()).isEqualTo(module)
    }

    @Test(expected = Injector.SelfRetrievalException::class)
    fun throwsWhenGettingItself() {
        Injector.from<Injector>()
    }

    @Test(expected = Injector.ModuleNotRegisteredException::class)
    fun throwsWhenGettingUnregisteredModule() {
        Injector.from<Module>()
    }

    @Test(expected = Module.DependencyNotInjected::class)
    fun doesNotInjectNonAnnotatedModuleDependenciesWhenRegisteringIt() {
        Injector
            .register<SuperModuleWithNonAnnotatedDependency>(SubModuleWithNonAnnotatedDependency())
        Injector.from<SuperModuleWithNonAnnotatedDependency>().get<Int>()
    }

    @Test
    fun injectsAnnotatedModuleDependenciesWhenRegisteringIt() {
        Injector.register<SuperModuleWithAnnotatedDependency>(SubModuleWithAnnotatedDependency())
        assertThat(Injector.from<SuperModuleWithAnnotatedDependency>().get<Int>()).isEqualTo(0)
    }

    @Test
    fun getsDependencyFromModuleInjectedAfterItWasRegistered() {
        Injector.register<Module>(object : Module() { })
        Injector.from<Module>().inject { 0 }
        assertThat(Injector.from<Module>().get<Int>()).isEqualTo(0)
    }
}
