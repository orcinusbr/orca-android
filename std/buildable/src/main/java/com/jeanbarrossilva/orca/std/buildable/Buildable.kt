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

package com.jeanbarrossilva.orca.std.buildable

/**
 * Denotes that the annotated structure should be able to be built through a builder, from which
 * each one of its constructor properties can have their values set and open methods can have their
 * behavior defined.
 *
 * It allows for a more functional approach towards interfaces or abstract classes and primarily
 * aims on improving overall readability.
 *
 * For example, a given class named `MyClass`, declared as
 *
 * ```kotlin
 * @Buildable
 * abstract class MyClass(a: Int) {
 *   open fun f(b: Int) {
 *     return a * b
 *   }
 * }
 * ```
 *
 * can be instantiated by its generated factory method:
 * ```kotlin
 * MyClass(a = 64) {
 *   f { b ->
 *     a / b
 *   }
 * }
 * ```
 */
@Target(AnnotationTarget.CLASS) annotation class Buildable
