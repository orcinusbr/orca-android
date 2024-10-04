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

package br.com.orcinus.orca.core.mastodon.notification

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.mastodon.notification.JsonElements.toNonLiteralString
import kotlin.test.Test
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject

internal class JsonElementsTests {
  @Test
  fun convertsIntoNonLiteralString() {
    assertThat(
        buildJsonObject {
          put("a", 0)
          put("b", "0")
          putJsonObject("c") { put("d", "0") }
          putJsonArray("e") {
            add(0)
            add("0")
          }
        }
      )
      .transform("toNonLiteralString") { it.toNonLiteralString() }
      .isEqualTo("{\"a\":0,\"b\":0,\"c\":{\"d\":0},\"e\":[0,0]}")
  }
}
