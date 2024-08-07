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

package br.com.orcinus.orca.platform.autos.i18n

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.platform.autos.test.R
import br.com.orcinus.orca.platform.testing.context
import java.util.Locale
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ReadableThrowableTests {
  @Test
  fun messageIsDefaultStringInDefaultLocale() {
    assertThat(ReadableThrowable(context, R.string.default_string).message).isEqualTo("0")
  }

  @Test
  fun messageIsDefaultStringInNonDefaultLocale() {
    val nonDefaultLocale = Locale.getAvailableLocales().first { it != Locale.getDefault() }
    assertThat(ReadableThrowable(context.at(nonDefaultLocale), R.string.default_string).message)
      .isEqualTo("0")
  }

  @Test
  fun localizedMessageIsLocalizedInDefaultLocale() {
    assertThat(ReadableThrowable(context, R.string.default_string).localizedMessage).isEqualTo("0")
  }

  @Test
  fun localizedMessageIsLocalizedInNonDefaultLocale() {
    assertThat(
        ReadableThrowable(context.at(Locale.forLanguageTag("pt-BR")), R.string.default_string)
          .localizedMessage
      )
      .isEqualTo("1")
  }
}
