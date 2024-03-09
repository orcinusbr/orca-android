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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination.type

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.mockkStatic
import io.mockk.verify
import kotlin.reflect.full.createType
import kotlin.reflect.typeOf
import kotlin.test.Test

internal class DefaultKTypeCreatorTests {
  @Test
  fun createsDefaultCreatorWithClassOfReifiedType() {
    assertThat(kTypeCreatorOf<Any>().kClass).isEqualTo(Any::class)
  }

  @Test
  fun delegatesCreationToCreateTypeExtensionMethodOnKClass() {
    mockkStatic("kotlin.reflect.full.KClassifiers") {
      kTypeCreatorOf<Any>().create()
      verify { Any::class.createType() }
    }
  }

  @Test(ComplexTypeException::class)
  fun throwsWhenCreatingComplexType() {
    kTypeCreatorOf<List<Any>>().create()
  }

  @Test
  fun createsSimpleType() {
    assertThat(kTypeCreatorOf<Any>().create()).isEqualTo(typeOf<Any>())
  }
}
