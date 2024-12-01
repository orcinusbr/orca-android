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

package br.com.orcinus.orca.core.mastodon.notification.security.encoding

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.prop
import kotlin.test.Test
import org.junit.BeforeClass

internal class Z85Tests {
  @Test fun encodes() = assertThat(byteArrayOf()).prop(ByteArray::encodeToZ85).isEmpty()

  @Test fun decodes() = assertThat("").prop(String::decodeFromZ85).isEmpty()

  companion object {
    @BeforeClass @JvmStatic fun setUp() = System.loadLibrary("core-mastodon")
  }
}
