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

package br.com.orcinus.orca.core.mastodon.instance.registration.webview.dom.interactor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;
import kotlin.PublishedApi;
import kotlin.text.StringsKt;

/**
 * Exposes the JavaScript Document Object Model (DOM) as a Java API and allows for it to be
 * interacted with.
 *
 * @see DomInteractor#script
 */
public class DomInteractor {
  /** {@link Character} that denotes the end of a statement in the {@link DomInteractor#script}. */
  private static final Character STATEMENT_END_DELIMITER = ';';

  /** {@link Document} that is always returned by {@link DomInteractor#getDocument()}. */
  @Nullable private Document document;

  /** Source code to be returned when translating calls made to the Java API into JavaScript. */
  @NonNull private String script = "";

  /**
   * {@link JavaScriptString} that is always returned by {@link
   * Document.HTMLInputElement#getType()}.
   */
  @NonNull private final JavaScriptString javaScriptString = new JavaScriptString();

  /**
   * The {@code Boolean} object represents a truth value: {@code true} or {@code false}.
   *
   * <p>For more information, refer to the <a
   * href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Boolean">
   * documentation at Mozilla Developer Network </a>.
   */
  public static class JavaScriptBoolean {
    /**
     * Single instance of a {@link JavaScriptBoolean}.
     *
     * @noinspection InstantiationOfUtilityClass, RedundantSuppression
     */
    @NonNull static final JavaScriptBoolean instance = new JavaScriptBoolean();

    /**
     * The {@code Boolean} object represents a truth value: {@code true} or {@code false}.
     *
     * <p>For more information, refer to the <a
     * href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Boolean">
     * documentation at Mozilla Developer Network </a>.
     */
    private JavaScriptBoolean() {}
  }

  /**
   * The {@code String} object is used to represent and manipulate a sequence of characters.
   *
   * <p>For more information, refer to the <a
   * href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String">
   * documentation at Mozilla Developer Network </a>.
   */
  public class JavaScriptString {
    /**
     * The {@code String} object is used to represent and manipulate a sequence of characters.
     *
     * <p>For more information, refer to the <a
     * href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String">
     * documentation at Mozilla Developer Network </a>.
     */
    JavaScriptString() {}

    /**
     * The abstract operation IsStrictlyEqual takes arguments {@code x} (an ECMAScript language
     * value) and {@code y} (an ECMAScript language value) and returns a {@code Boolean}. It
     * provides the semantics for the {@code ===} operator.
     *
     * <p>For more information, refer to the <a
     * href="https://tc39.es/ecma262/multipage/abstract-operations.html#sec-isstrictlyequal">
     * ECMAScript Language Specification </a>.
     *
     * @param other {@link String} to which this {@link JavaScriptString} is being compared.
     */
    @NonNull
    public JavaScriptBoolean isStrictlyEqual(@NonNull String other) {
      script += " === \"" + other + '"';
      return JavaScriptBoolean.instance;
    }

    /**
     * The {@code toLowerCase()} method of {@code String} values returns this string converted to
     * lower case.
     *
     * <p>For more information, refer to the <a
     * href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/toLowerCase">
     * documentation at Mozilla Developer Network </a>.
     */
    @NonNull
    public JavaScriptString toLowerCase() {
      script += ".toLowerCase()";
      return this;
    }
  }

  /**
   * The {@code Document} interface represents any web page loaded in the browser and serves as an
   * entry point into the web page's content, which is the DOM tree.
   *
   * <p>The DOM tree includes elements such as {@code <body>} and {@code <table>}, among many
   * others. It provides functionality globally to the document, like how to obtain the page's URL
   * and create new elements in the document.
   *
   * <p>The {@code Document} interface describes the common properties and methods for any kind of
   * document. Depending on the document's type (e.g. HTML, XML, SVG, …), a larger API is available:
   * HTML documents, served with the {@code "text/html"} content type, also implement the
   * `HTMLDocument` interface, whereas XML and SVG documents implement the `XMLDocument` interface.
   *
   * <p>For more information, refer to the <a
   * href="https://developer.mozilla.org/en-US/docs/Web/API/Document">documentation at Mozilla
   * Developer Network </a>.
   */
  public class Document {
    /**
     * {@link HTMLCollection} that is always returned by {@link
     * Document#getElementsByClassName(String)}.
     */
    @NonNull private final HTMLCollection htmlCollection = new HTMLCollection();

    /**
     * {@link HTMLInputElement} that is always returned by both {@link
     * Document#getElementById(String)} and {@link HTMLCollection#get(int)}.
     */
    @NonNull private final HTMLInputElement htmlInputElement = new HTMLInputElement();

    /**
     * The {@code HTMLInputElement} interface provides special properties and methods for
     * manipulating the options, layout, and presentation of {@code <input>} elements.
     *
     * <p>For more information, refer to the <a
     * href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLInputElement">documentation at
     * Mozilla Developer Network </a>.
     */
    public class HTMLInputElement {
      /**
       * The {@code HTMLInputElement} interface provides special properties and methods for
       * manipulating the options, layout, and presentation of {@code <input>} elements.
       *
       * <p>For more information, refer to the <a
       * href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLInputElement">documentation at
       * Mozilla Developer Network </a>.
       */
      HTMLInputElement() {}

      /**
       * The type property of the {@link HTMLInputElement} interface indicates the kind of data
       * allowed in the {@code <input>} element, or example a number, a date, or an email. Browsers
       * will select the appropriate widget and behavior to help users to enter a valid value.
       *
       * <p>It reflects the {@code type} attribute of the {@code <input>} element.
       */
      @NonNull
      public JavaScriptString getType() {
        script += ".type";
        return javaScriptString;
      }

      /**
       * Changes the string that represents the current value of the control. If the user enters a
       * value different from the value expected, this may return an empty string.
       *
       * <p>For more information, refer to the <a
       * href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLInputElement">documentation at
       * Mozilla Developer Network </a>.
       *
       * @param value Value to which this {@link HTMLInputElement}'s should be changed.
       */
      public void setValue(@NonNull String value) {
        state(".value = \"" + value + '"');
      }

      /**
       * The {@code HTMLElement.click()} method simulates a mouse click on an element. When called
       * on an element, the element's click event is fired (unless its disabled attribute is set).
       *
       * <p>For more information, refer to the <a
       * href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLElement/click">documentation at
       * Mozilla Developer Network </a>.
       */
      public void click() {
        state(".click()");
      }
    }

    /**
     * The {@link HTMLCollection} interface represents a generic collection (array-like object
     * similar to arguments) of elements (in document order) and offers methods and properties for
     * selecting from the list.
     *
     * <p>For more information, refer to the <a
     * href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLCollection">documentation at
     * Mozilla Developer Network </a>.
     */
    public class HTMLCollection {
      /**
       * The {@link HTMLCollection} interface represents a generic collection (array-like object
       * similar to arguments) of elements (in document order) and offers methods and properties for
       * selecting from the list.
       *
       * <p>For more information, refer to the <a
       * href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLCollection">documentation at
       * Mozilla Developer Network </a>.
       */
      HTMLCollection() {}

      /**
       * Performs the operation within the block on each {@link HTMLInputElement} contained by this
       * {@link HTMLCollection}.
       *
       * @param block Action to be run on each {@link HTMLInputElement}.
       */
      public void forEach(@NonNull Consumer<Supplier<HTMLInputElement>> block) {
        String statementDelimiterEndAsString = STATEMENT_END_DELIMITER.toString();
        String[] statements = script.split(statementDelimiterEndAsString);
        String referenceName = statements[statements.length - 1];
        script =
            StringsKt.replaceAfterLast(script, STATEMENT_END_DELIMITER, "", "")
                + "for (let element of "
                + referenceName
                + ") {";
        block.accept(
            () -> {
              script += "element";
              return htmlInputElement;
            });
        script += '}';
      }

      /**
       * Obtains an {@link HTMLInputElement} at the given index.
       *
       * @param index Index at which the {@link HTMLInputElement} to be obtained is.
       */
      @NonNull
      public HTMLInputElement get(int index) {
        script += '[' + String.valueOf(index) + ']';
        return htmlInputElement;
      }
    }

    /**
     * The {@code Document} interface represents any web page loaded in the browser and serves as an
     * entry point into the web page's content, which is the DOM tree.
     *
     * <p>The DOM tree includes elements such as {@code <body>} and {@code <table>}, among many
     * others. It provides functionality globally to the document, like how to obtain the page's URL
     * and create new elements in the document.
     *
     * <p>The {@code Document} interface describes the common properties and methods for any kind of
     * document. Depending on the document's type (e.g. HTML, XML, SVG, …), a larger API is
     * available: HTML documents, served with the {@code "text/html"} content type, also implement
     * the `HTMLDocument` interface, whereas XML and SVG documents implement the `XMLDocument`
     * interface.
     *
     * <p>For more information, refer to the <a
     * href="https://developer.mozilla.org/en-US/docs/Web/API/Document">documentation at Mozilla
     * Developer Network </a>.
     */
    Document() {}

    /**
     * The {@code getElementById()} method of the {@link Document} interface returns an {@code
     * Element} object representing the element whose id property matches the specified string.
     * Since element IDs are required to be unique if specified, they're a useful way to get access
     * to a specific element quickly.
     *
     * <p>For more information, refer to the <a
     * href="https://developer.mozilla.org/en-US/docs/Web/API/Document/getElementById">documentation
     * at Mozilla Developer Network </a>.
     *
     * @param id The ID of the element to locate. The ID is a case-sensitive string which is unique
     *     within the document; only one element should have any given ID.
     */
    @NonNull
    public HTMLInputElement getElementById(@NonNull String id) {
      script += ".getElementById(\"" + id + "\")";
      return htmlInputElement;
    }

    /**
     * The {@code getElementsByClassName} method of {@link Document} interface returns an array-like
     * object of all child elements which have all of the given class name(s).
     *
     * <p>When called on the document object, the complete document is searched, including the root
     * node. You may also call {@code getElementsByClassName()} on any element; it will return only
     * elements which are descendants of the specified root element with the given class name(s).
     *
     * <p>For more information, refer to the <a
     * href="https://developer.mozilla.org/en-US/docs/Web/API/Document/getElementsByClassName">
     * documentation at Mozilla Developer Network </a>.
     *
     * @param names A string representing the class name(s) to match; multiple class names are
     *     separated by whitespace.
     */
    @NonNull
    public HTMLCollection getElementsByClassName(@NonNull String names) {
      script += ".getElementsByClassName(\"" + names + "\")";
      return htmlCollection;
    }

    /**
     * The {@code getElementsByTagName} method of {@link Document} interface returns an {@link
     * HTMLCollection} of elements with the given tag name.
     *
     * <p>The complete document is searched, including the root node. The returned {@link
     * HTMLCollection} is live, meaning that it updates itself automatically to stay in sync with
     * the DOM tree without having to call `document.getElementsByTagName()` again.
     *
     * <p>For more information, refer to the <a
     * href="https://developer.mozilla.org/en-US/docs/Web/API/Document/getElementsByTagName">
     * documentation at Mozilla Developer Network </a>.
     *
     * @param name A string representing the name of the elements. The special string * represents
     *     all elements.
     */
    @NonNull
    public HTMLCollection getElementsByTagName(@NonNull String name) {
      script += ".getElementsByTagName(\"" + name + "\")";
      return htmlCollection;
    }
  }

  /**
   * Exposes the JavaScript Document Object Model (DOM) as a Java API and allows for it to be
   * interacted with.
   *
   * @see DomInteractor#script
   */
  @PublishedApi
  public DomInteractor() {}

  /**
   * Direct translation of accessing {@link Document} in JavaScript. Its side effect is that, as
   * well as any call to the {@link Document} API, it appends the equivalent expression or statement
   * to the {@link String} to be later returned by {@link DomInteractor#script()}.
   */
  @NonNull
  public Document getDocument() {
    script += "document";
    if (document == null) {
      document = new Document();
    }
    return document;
  }

  /**
   * Provides a {@link String} in which all calls to the {@link DomInteractor} API are represented
   * as JavaScript source.
   */
  @NonNull
  @PublishedApi
  public String script() {
    return StringsKt.trimEnd(script).toString();
  }

  /**
   * Appends a JavaScript conditional.
   *
   * @param condition Provides the condition to be satisfied in order for the statement to be run.
   * @param statement Action performed when the condition is {@code true}.
   */
  void doIf(@NonNull Supplier<JavaScriptBoolean> condition, @NonNull Runnable statement) {
    script += "if (";
    condition.get();
    script += ") {";
    statement.run();
    script += '}';
  }

  /**
   * Appends the statement as finished to the {@link DomInteractor#script}.
   *
   * @param statement Statement to be finished and appended.
   */
  private void state(String statement) {
    script += statement + STATEMENT_END_DELIMITER;
  }
}
