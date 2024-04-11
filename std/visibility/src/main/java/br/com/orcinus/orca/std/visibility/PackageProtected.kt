/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.std.visibility

/**
 * Denotes that a structure should only be referenced from a package equal to that in which it was
 * declared or its children.
 *
 * For example, a class marked as package-protected declared at `br.com.orcinus.orca.core` can be
 * referenced from `br.com.orcinus.orca.core` and `br.com.orcinus.orca.core.sample`, but shouldn't
 * be referenced from `br.com.orcinus.orca.app`.
 *
 * @param message Message with which the error will be reported on references from an outside
 *   package.
 */
@Retention(AnnotationRetention.BINARY)
@Target(
  AnnotationTarget.ANNOTATION_CLASS,
  AnnotationTarget.CLASS,
  AnnotationTarget.CONSTRUCTOR,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY,
  AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER,
  AnnotationTarget.TYPEALIAS
)
annotation class PackageProtected(val message: String = DEFAULT_MESSAGE) {
  companion object {
    /** [message] that is the default one of [PackageProtected]. */
    const val DEFAULT_MESSAGE =
      "This structure is package-protected and is not intended to be referenced by outside " +
        "packages."
  }
}
