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
