package com.jeanbarrossilva.orca.std.injector

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import kotlin.test.Test
import org.junit.Rule

internal class InjectorTests {
    @get:Rule
    val injectorRule = InjectorTestRule()

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

    @Test
    fun getsInjectedDependencyFromRegisteredModule() {
        Injector.register<Module>(object : Module() { })
        Injector.from<Module>().inject { 0 }
        assertThat(Injector.from<Module>().get<Int>()).isEqualTo(0)
    }
}
