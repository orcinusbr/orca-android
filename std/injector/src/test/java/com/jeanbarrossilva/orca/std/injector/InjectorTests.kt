package com.jeanbarrossilva.orca.std.injector

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import kotlin.test.Test
import org.junit.Rule

internal class InjectorTests {
    @get:Rule
    val injectorRule = InjectorTestRule()

    @Test
    fun injectsDependency() {
        Injector.inject { 0 }
        assertThat(Injector.get<Int>()).isEqualTo(0)
    }

    @Test(expected = NoSuchElementException::class)
    fun throwsWhenGettingDependencyThatHasNotBeenInjected() {
        Injector.get<Int>()
    }

    @Test
    fun getsDependencyThatGetsPreviouslyInjectedOne() {
        Injector.inject { 0 }
        Injector.inject { get<Int>() }
        assertThat(Injector.get<Int>()).isEqualTo(0)
    }

    @Test(NoSuchElementException::class)
    fun clears() {
        Injector.inject { 0 }
        Injector.clear()
        Injector.get<Int>()
    }
}
