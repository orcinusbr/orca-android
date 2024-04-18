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

package br.com.orcinus.orca.std.injector

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSameAs
import br.com.orcinus.orca.std.injector.module.Inject
import br.com.orcinus.orca.std.injector.module.Module
import br.com.orcinus.orca.std.injector.module.binding.boundTo
import br.com.orcinus.orca.std.injector.module.injection.Injection
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf
import br.com.orcinus.orca.std.injector.test.InjectorTestRule
import kotlin.test.Test
import org.junit.Rule

internal class InjectorTests {
  @get:Rule val injectorRule = InjectorTestRule()

  private class SubModuleWithNonAnnotatedInjection :
    SuperModuleWithNonAnnotatedInjection(lazyInjectionOf { 0 })

  private abstract class SuperModuleWithNonAnnotatedInjection(
    @Suppress("unused") private val injection: Injection<Int>
  ) : Module()

  private class SubModuleWithAnnotatedInjection :
    SuperModuleWithAnnotatedInjection(lazyInjectionOf { 0 })

  internal abstract class SuperModuleWithAnnotatedInjection(@Inject val injection: Injection<Int>) :
    Module()

  @Test(expected = Injector.SelfRegistrationException::class)
  fun throwsWhenRegisteringItself() {
    Injector.register(Injector)
  }

  @Test
  fun registersModule() {
    val module = object : Module() {}
    Injector.register<Module>(module)
    assertThat(Injector.from<Module>()).isEqualTo(module)
  }

  @Test
  fun replacesWhenRegisteringModuleBoundToSameBaseAsPreviouslyRegisteredOne() {
    val replacement = SubModuleWithAnnotatedInjection()
    Injector.register(
      SubModuleWithAnnotatedInjection().boundTo<SuperModuleWithAnnotatedInjection, _>()
    )
    Injector.register(replacement.boundTo<SuperModuleWithAnnotatedInjection, _>())
    assertThat(Injector.from<SuperModuleWithAnnotatedInjection>()).isSameAs(replacement)
  }

  @Test(expected = Injector.SelfRetrievalException::class)
  fun throwsWhenGettingItself() {
    Injector.from<Injector>()
  }

  @Test(expected = Injector.ModuleNotRegisteredException::class)
  fun throwsWhenGettingUnregisteredModule() {
    Injector.from<Module>()
  }

  @Test(expected = Module.DependencyNotInjectedException::class)
  fun doesNotInjectNonAnnotatedModuleDependenciesWhenRegisteringIt() {
    Injector.register<SuperModuleWithNonAnnotatedInjection>(SubModuleWithNonAnnotatedInjection())
    Injector.from<SuperModuleWithNonAnnotatedInjection>().get<Int>()
  }

  @Test
  fun registersAnnotatedModuleDependenciesWhenRegisteringIt() {
    Injector.register<SuperModuleWithAnnotatedInjection>(SubModuleWithAnnotatedInjection())
    assertThat(Injector.from<SuperModuleWithAnnotatedInjection>().injection()).isEqualTo(0)
  }

  @Test
  fun registersModuleBoundToBothItsActualAndBaseTypes() {
    val module = SubModuleWithAnnotatedInjection()
    Injector.register(
      module.boundTo<SuperModuleWithAnnotatedInjection, SubModuleWithAnnotatedInjection>()
    )
    assertThat(Injector.from<SuperModuleWithAnnotatedInjection>()).isEqualTo(module)
    assertThat(Injector.from<SubModuleWithAnnotatedInjection>()).isEqualTo(module)
  }

  @Test
  fun getsDependencyFromModuleInjectedAfterItWasRegistered() {
    Injector.register<Module>(object : Module() {})
    Injector.from<Module>().injectLazily { 0 }
    assertThat(Injector.from<Module>().get<Int>()).isEqualTo(0)
  }

  @Test(expected = Injector.ModuleNotRegisteredException::class)
  fun throwsWhenUnregisteringUnregisteredModule() {
    Injector.unregister<Module>()
  }

  @Test(expected = Injector.ModuleNotRegisteredException::class)
  fun unregistersModule() {
    Injector.register<Module>(object : Module() {})
    Injector.unregister<Module>()
    Injector.from<Module>()
  }
}
