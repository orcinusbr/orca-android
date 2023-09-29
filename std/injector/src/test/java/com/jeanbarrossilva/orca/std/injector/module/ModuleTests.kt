package com.jeanbarrossilva.orca.std.injector.module

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.injector.Injector
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import kotlin.test.Test
import org.junit.Rule

internal class ModuleTests {
    @get:Rule
    val injectorRule = InjectorTestRule()

    @Test
    fun injects() {
        Module {
            inject {
                0
            }
        }
            .inject()
        assertThat(Injector.get<Int>()).isEqualTo(0)
    }
}
