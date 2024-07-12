/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.platform.autos.test.kit.input.text

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onNodeWithTag
import br.com.orcinus.orca.platform.autos.kit.input.text.FormTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.FormTextFieldErrorsTag
import br.com.orcinus.orca.platform.autos.kit.input.text.FormTextFieldTag
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextFieldTag

/**
 * [SemanticsNodeInteraction] of a [FormTextField].
 *
 * @see FormTextField
 */
fun SemanticsNodeInteractionsProvider.onFormTextField(): SemanticsNodeInteraction {
  return onNodeWithTag(FormTextFieldTag)
}

/** [SemanticsNodeInteraction] of a [SearchTextField]. */
fun SemanticsNodeInteractionsProvider.onSearchTextField(): SemanticsNodeInteraction {
  return onNodeWithTag(SearchTextFieldTag)
}

/**
 * [SemanticsNodeInteraction] of a text field's errors.
 *
 * @see FormTextField
 */
fun SemanticsNodeInteractionsProvider.onTextFieldErrors(): SemanticsNodeInteraction {
  return onNodeWithTag(FormTextFieldErrorsTag)
}
