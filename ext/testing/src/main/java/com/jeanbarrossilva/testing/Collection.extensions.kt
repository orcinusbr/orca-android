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

package com.jeanbarrossilva.testing

import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

/**
 * Associates each of these properties with the declared ones of [S] that have the same name as
 * them.
 *
 * @param F Value whose properties will be associated to those of [S] with the same name.
 * @param S Value whose properties will be associated to those of [F] with the same name.
 */
@PublishedApi
internal inline fun <F, reified S : Any> Collection<KProperty1<out F, *>>
  .associateWithEquallyNamedDeclaredProperties(): Map<KProperty1<F, *>, KProperty1<S, *>> {
  return associateWith { firstsProperty ->
      S::class.declaredMemberProperties.firstOrNull { secondsProperty ->
        secondsProperty.name == firstsProperty.name
      }
    }
    .filterValues { it != null }
    .let {
      @Suppress("UNCHECKED_CAST")
      it as Map<KProperty1<F, *>, KProperty1<S, *>>
    }
}
