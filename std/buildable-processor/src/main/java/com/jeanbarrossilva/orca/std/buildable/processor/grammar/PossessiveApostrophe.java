/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
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
