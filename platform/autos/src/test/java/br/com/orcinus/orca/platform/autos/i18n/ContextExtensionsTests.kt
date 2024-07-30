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

import android.os.LocaleList
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.platform.testing.context
import java.util.Locale
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ContextExtensionsTests {
  @Test
  fun createsLocalizedContext() {
    val arabicLocale = Locale.forLanguageTag("ar-SA")
    assertThat(context.at(arabicLocale).resources.configuration.locales)
      .isEqualTo(LocaleList(arabicLocale))
  }
}
