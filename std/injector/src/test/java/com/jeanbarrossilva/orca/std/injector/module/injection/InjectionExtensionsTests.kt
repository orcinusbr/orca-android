/*
 * Copyright © 2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.std.injector.module.injection

import assertk.assertThat
import assertk.assertions.isSameAs
import com.jeanbarrossilva.orca.std.injector.Injector
import com.jeanbarrossilva.orca.std.injector.module.Module
import kotlin.test.Test

internal class InjectionExtensionsTests {
  @Test
  fun createsInjection() {
    val dependency = Object()
    with(Injector) {
      assertThat(with(injectionOf { dependency }) { provide() })
        .isSameAs(
          with(
            object : Injection<Any>() {
              override val dependencyClass = Any::class

              override fun Module.create(): Any {
                return dependency
              }
            }
          ) {
            provide()
          }
        )
    }
  }
}
