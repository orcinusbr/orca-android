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

/**
 * Evaluates the JavaScript script to be translated that resulted from the given [evaluation], that
 * is provided with a [Dom] from which a Kotlin API that mimics the DOM's can be used.
 *
 * @param evaluation Modifies the JavaScript script to be evaluated.
 */
internal fun buildDom(evaluation: Dom.() -> Unit): String {
  return Dom().apply(evaluation).build()
}
