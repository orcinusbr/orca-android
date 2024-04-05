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

package br.com.orcinus.orca.core.instance.registration

import kotlin.test.Test

internal class CredentialsTests {
  @Test(expected = Credentials.InvalidEmailException::class)
  fun throwsWhenEmailIsInvalid() {
    Credentials("alo@@ola.com", "password123")
  }

  @Test(expected = Credentials.BlankPasswordException::class)
  fun throwsWhenPasswordIsEmpty() {
    Credentials("jean@orcinus.com.br", " ")
  }

  @Test
  fun doesNotThrowsWhenEmailIsValidAndPasswordIsNotBlank() {
    Credentials("jean@orcinus.com.br", "password123")
  }
}
