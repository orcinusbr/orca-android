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
 * Creates a script from an interaction with a Document Object Model (DOM), made possible by the
 * [DomInteractor] provided to the lambda.
 *
 * @param interaction Interacts with a DOM.
 */
internal fun interactWithDom(interaction: DomInteractor.() -> Unit): String {
  return DomInteractor().apply(interaction).script()
}
