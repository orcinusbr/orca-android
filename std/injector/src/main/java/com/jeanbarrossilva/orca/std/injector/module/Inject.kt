/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.std.injector.module

import com.jeanbarrossilva.orca.std.injector.Injector

/**
 * Denotes that the dependency returned by the value of the annotated property should be
 * automatically injected into the [Module] in which it was declared and that an extension function
 * for getting its result should be created for that same [Module].
 *
 * For example, declaring `class MyModule(@Inject val dependency: Module.() -> Int)` and registering
 * it in the [Injector] injects `dependency` into `MyModule` and makes its provided `Int` accessible
 * via a `MyModule.dependency()` extension function.
 *
 * Note that the annotated property should return a `Module.() -> Any`, which is how an injection is
 * recognized; otherwise, an error will be thrown at build time.
 *
 * @see Injector.register
 */
@Target(AnnotationTarget.PROPERTY) annotation class Inject
