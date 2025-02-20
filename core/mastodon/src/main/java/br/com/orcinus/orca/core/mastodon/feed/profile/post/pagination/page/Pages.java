/*
 * Copyright © 2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.page;

/** Utilities for pages. */
public class Pages {
  /**
   * Marker that denotes an unset page. It is not a valid page itself — passing it as a parameter to
   * {@link #validate(int)} throws an {@link InvalidException}; rather, it merely indicates that a
   * page has not yet been defined.
   */
  public static final int NONE = -1;

  /** {@link IllegalArgumentException} thrown when a page is invalid. */
  public static class InvalidException extends IllegalArgumentException {
    /**
     * Constructs an {@link InvalidException}.
     *
     * @param message The detail message.
     */
    InvalidException(final String message) {
      super(message);
    }
  }

  /** {@link InvalidException} thrown when a page is negative. */
  public static class NegativeException extends InvalidException {
    /**
     * Constructs a {@link NegativeException}.
     *
     * @param page The negative page.
     */
    NegativeException(final int page) {
      super("Page cannot be negative (" + name(page) + ").");
    }
  }

  /**
   * Creates an instance of this class.
   *
   * <p>This class serves as a collection of static utilities and, thus, shall not be constructed.
   */
  private Pages() {}

  /**
   * Ensures that the given integer is a valid page.
   *
   * @param page Page whose validity will be checked.
   * @throws NegativeException If the page is negative.
   */
  public static void validate(final int page) throws NegativeException {
    if (page < 0) {
      throw new NegativeException(page);
    }
  }

  /**
   * Provides a readable name for the given page in case it is a marker (i. e., {@link #NONE});
   * otherwise, returns its {@link String} representation.
   */
  private static String name(final int page) {
    String name = String.valueOf(page);
    if (page == NONE) {
      name = "NONE";
    }
    return name;
  }
}
