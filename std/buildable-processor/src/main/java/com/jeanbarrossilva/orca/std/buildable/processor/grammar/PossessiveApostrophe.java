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

package com.jeanbarrossilva.orca.std.buildable.processor.grammar;

/**
 * Obtains the appropriate possessive apostrophe for a given {@link String} through {@link
 * PossessiveApostrophe#of}.
 */
public class PossessiveApostrophe {
  /** Possessive apostrophe without an "s" at the end. */
  static String WITHOUT_TRAILING_S = "'";

  /** Possessive apostrophe with an "s" at the end. */
  static String WITH_TRAILING_S = WITHOUT_TRAILING_S + 's';

  /** Obtains the appropriate possessive apostrophe for the given {@link String}. */
  public static String of(String string) {
    string = string.trim();
    if (string.endsWith("s")) {
      return WITHOUT_TRAILING_S;
    }
    return WITH_TRAILING_S;
  }
}
