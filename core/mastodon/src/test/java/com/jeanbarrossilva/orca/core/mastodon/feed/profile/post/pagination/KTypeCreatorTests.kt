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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.reflect.KClass
import kotlin.reflect.typeOf
import kotlin.test.Test

internal class KTypeCreatorTests {
  @Test(KTypeCreator.Companion.ComplexTypeException::class)
  fun throwsWhenCreatingComplexTypeWithDefaultCreator() {
    @Suppress("UNCHECKED_CAST")
    KTypeCreator.defaultFor<List<Any>>().create(List::class as KClass<List<Any>>)
  }

  @Test
  fun defaultCreatorCreatesSimpleType() {
    assertThat(KTypeCreator.defaultFor<String>().create(String::class)).isEqualTo(typeOf<String>())
  }
}
