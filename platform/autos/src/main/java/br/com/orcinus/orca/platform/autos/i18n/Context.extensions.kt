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

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Creates a new [Context] whose resources and layout direction are determined by the given
 * [Locale].
 *
 * @param locale [Locale] for which the [Context] to be created is.
 * @throws IllegalStateException If a localized [Context] cannot be created. Specifically, when
 *   [Context.createConfigurationContext] returns `null`.
 */
@Throws(IllegalStateException::class)
internal fun Context.at(locale: Locale): Context {
  val localizedConfig = resources?.configuration?.let(::Configuration) ?: Configuration()
  localizedConfig.setLocale(locale)
  return createConfigurationContext(localizedConfig)
    ?: error("Could not create a context for the $locale locale.")
}
