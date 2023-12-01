/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.std.buildable.processor.grammar

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

internal class IndefiniteArticleTests {
  @Test
  fun aPrecedesEmptyString() {
    assertThat(IndefiniteArticle.of(" ")).isEqualTo(IndefiniteArticle.A)
  }

  @Test
  fun anPrecedesAmpersandLiteral() {
    assertThat(IndefiniteArticle.of("&")).isEqualTo(IndefiniteArticle.AN)
  }

  @Test
  fun aPrecedesSemiColonLiteral() {
    assertThat(IndefiniteArticle.of(";")).isEqualTo(IndefiniteArticle.A)
  }

  @Test
  fun anPrecedesWordsStartingWithNonUVowels() {
    assertThat(IndefiniteArticle.of("asterisk")).isEqualTo(IndefiniteArticle.AN)
    assertThat(IndefiniteArticle.of("elevator")).isEqualTo(IndefiniteArticle.AN)
    assertThat(IndefiniteArticle.of("indicate")).isEqualTo(IndefiniteArticle.AN)
    assertThat(IndefiniteArticle.of("outdated")).isEqualTo(IndefiniteArticle.AN)
  }

  @Test
  fun anPrecedesWordUncharted() {
    assertThat(IndefiniteArticle.of("uncharted")).isEqualTo(IndefiniteArticle.AN)
  }

  @Test
  fun aPrecedesWordUniversity() {
    assertThat(IndefiniteArticle.of("university")).isEqualTo(IndefiniteArticle.A)
  }

  @Test
  fun anPrecedesNflAcronym() {
    assertThat(IndefiniteArticle.of("NFL")).isEqualTo(IndefiniteArticle.AN)
  }

  @Test
  fun aPrecedesDigitOne() {
    assertThat(IndefiniteArticle.of("1")).isEqualTo(IndefiniteArticle.A)
  }

  @Test
  fun aPrecedesWordOne() {
    assertThat(IndefiniteArticle.of("one")).isEqualTo(IndefiniteArticle.A)
  }

  @Test
  fun anPrecedesDigitEight() {
    assertThat(IndefiniteArticle.of("8")).isEqualTo(IndefiniteArticle.AN)
  }

  @Test
  fun anPrecedesWordEight() {
    assertThat(IndefiniteArticle.of("eight")).isEqualTo(IndefiniteArticle.AN)
  }
}
