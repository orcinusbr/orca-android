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

package com.jeanbarrossilva.orca.std.buildable.processor.grammar;

import java.text.Normalizer;
import org.jetbrains.annotations.NotNull;

/**
 * Obtains the appropriate indefinite article for a {@link String} through {@link
 * IndefiniteArticle#of}.
 */
public class IndefiniteArticle {
  /** Indefinite article "a". */
  @NotNull static final String A = "a";

  /** Indefinite article "an". */
  @NotNull static final String AN = "an";

  private IndefiniteArticle() {}

  /** Obtains the appropriate indefinite article for the given {@link String}. */
  @NotNull
  public static String of(@NotNull String string) {
    if (string.isBlank()) {
      return A;
    }
    String word = string.trim().split(" ")[0];
    char initialChar = word.charAt(0);
    boolean isInitialCharLetter = Character.isAlphabetic(initialChar);
    if ((isInitialCharLetter && shouldWordBePrecededByAn(word, initialChar))
        || (!isInitialCharLetter && shouldNonLetterCharacterBePrecededByAn(initialChar))) {
      return AN;
    }
    return A;
  }

  /** Whether the given word should be preceded by {@link IndefiniteArticle#AN}. */
  private static boolean shouldWordBePrecededByAn(@NotNull String word, char initialLetter) {
    return shouldInitialLetterSoundBeChecked(word) && isLetterWithVowelSound(initialLetter)
        || startsWithVowelSoundingConsonant(word)
        || startsWithUnexceptionalVowel(removeDiacritics(word));
  }

  /**
   * Returns whether the given word should have its initial letter's sound checked in order to
   * determine if it has to be preceded by {@link IndefiniteArticle#AN}.
   */
  private static boolean shouldInitialLetterSoundBeChecked(@NotNull String word) {
    return word.length() == 1 || isAcronym(word);
  }

  /** Returns whether the given word is an acronym, such as "SOLID" or "SAAS". */
  private static boolean isAcronym(String word) {
    char[] charArray = word.toCharArray();
    for (char character : charArray) {
      boolean isNotUpperCase = !Character.isUpperCase(character);
      if (isNotUpperCase) {
        return false;
      }
    }
    return true;
  }

  /** Returns whether the sound of the given letter is that of a vowel. */
  private static boolean isLetterWithVowelSound(char letter) {
    letter = Character.toLowerCase(letter);
    return letter == 'a'
        || letter == 'e'
        || letter == 'f'
        || letter == 'h'
        || letter == 'i'
        || letter == 'l'
        || letter == 'm'
        || letter == 'n'
        || letter == 'o'
        || letter == 'r'
        || letter == 's'
        || letter == 'x';
  }

  /** Returns whether the given word starts with a consonant that sounds like a vowel. */
  private static boolean startsWithVowelSoundingConsonant(@NotNull String word) {
    word = word.toLowerCase();
    return word.startsWith("heir") || word.startsWith("hon") || word.startsWith("hour");
  }

  /**
   * Returns whether the given word starts with a vowel that isn't succeeded by "ne" if it's an "o"
   * or "ni" if it's a "u".
   */
  private static boolean startsWithUnexceptionalVowel(@NotNull String word) {
    word = word.toLowerCase();
    return word.startsWith("a")
        || word.startsWith("e")
        || word.startsWith("i")
        || word.startsWith("o") && !word.startsWith("one")
        || word.startsWith("u") && !word.startsWith("uni");
  }

  /** Returns the given word without any diacritics. */
  @NotNull
  private static String removeDiacritics(@NotNull String word) {
    Normalizer.Form form = Normalizer.Form.NFKD;
    boolean doesNotHaveDiacritics = Normalizer.isNormalized(word, form);
    if (doesNotHaveDiacritics) {
      return word;
    }
    return Normalizer.normalize(word, form).replaceAll("\\p{M}", "");
  }

  /**
   * Returns whether the given non-letter character should be preceded by {@link
   * IndefiniteArticle#AN}.
   */
  private static boolean shouldNonLetterCharacterBePrecededByAn(char nonLetterCharacter) {
    String normalizedName = getNormalizedName(nonLetterCharacter);
    char initialNormalizedNameLetter = normalizedName.charAt(0);
    return shouldWordBePrecededByAn(normalizedName, initialNormalizedNameLetter);
  }

  /**
   * Returns a normalized version of the given character's name, lower-casing it and removing
   * prefixes like "digit" with which it is provided by the standard {@link Character#getName}
   * method.
   */
  @NotNull
  private static String getNormalizedName(char character) {
    String normalizedName = Character.getName(character).toLowerCase();
    boolean isCharacterDigit = Character.isDigit(character);
    if (isCharacterDigit) {
      normalizedName = normalizedName.replaceFirst("digit ", "");
    }
    return normalizedName;
  }
}
