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

package br.com.orcinus.orca.ext.uri

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.ext.uri.url.SchemedURLBuilder
import java.net.URI
import kotlin.test.Test

class URIBuilderTests {
  @Test
  fun createsSchemedURIBuilder() {
    assertThat(URIBuilder.url().scheme("https")).isEqualTo(SchemedURLBuilder("https"))
  }

  @Test
  fun buildsMailtoURI() {
    assertThat(URIBuilder.mailto("jean@orcinus.com.br"))
      .isEqualTo(URI("${URIBuilder.MAILTO}:jean@orcinus.com.br"))
  }
}
