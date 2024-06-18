/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.std.injector.module.binding

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.std.injector.module.Module
import kotlin.test.Test

internal class BindingExtensionsTests {
  class AliasModule : BaseModule()

  abstract class BaseModule : Module()

  @Test
  fun binds() {
    val module = AliasModule()
    assertThat(module.bound)
      .isEqualTo(Binding(base = AliasModule::class, alias = AliasModule::class, module))
  }

  @Test
  fun bindsToDistinctBaseAndAlias() {
    val module = AliasModule()
    assertThat(module.boundTo<BaseModule, AliasModule>())
      .isEqualTo(Binding(base = BaseModule::class, alias = AliasModule::class, module))
  }
}
