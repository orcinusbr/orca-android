package com.jeanbarrossilva.orca.std.injector.binding

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.injector.module.Module
import kotlin.test.Test

internal class BindingExtensionsTests {
  class SubModule : SuperModule()

  abstract class SuperModule : Module()

  @Test
  fun binds() {
    val module = SubModule()
    assertThat(module.boundTo<SuperModule, SubModule>())
      .isEqualTo(Binding(SuperModule::class, SubModule::class, module))
  }
}
