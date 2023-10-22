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
