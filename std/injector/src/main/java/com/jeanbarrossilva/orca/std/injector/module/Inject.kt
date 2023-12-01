/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
