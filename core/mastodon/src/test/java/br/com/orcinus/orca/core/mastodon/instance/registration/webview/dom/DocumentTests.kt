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

package br.com.orcinus.orca.core.mastodon.instance.registration.webview.dom

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

internal class DocumentTests {
  @Test
  fun getsElementByID() {
    assertThat(interactWithDom { document.getElementById("id") })
      .isEqualTo("document.getElementById(\"id\")")
  }

  @Test
  fun getsElementsByClassName() {
    assertThat(interactWithDom { document.getElementsByClassName("Airplane pt.2") })
      .isEqualTo("document.getElementsByClassName(\"Airplane pt.2\")")
  }

  @Test
  fun getsElementsByTagName() {
    assertThat(interactWithDom { document.getElementsByTagName("input") })
      .isEqualTo("document.getElementsByTagName(\"input\")")
  }
}
