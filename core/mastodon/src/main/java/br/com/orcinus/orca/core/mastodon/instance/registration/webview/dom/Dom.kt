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

package br.com.orcinus.orca.core.mastodon.instance.registration.webview.dom

/**
 * Exposes the JavaScript Document Object Model (DOM) as a Kotlin API.
 *
 * @see build
 */
internal class Dom {
  /**
   * [Document] to which an access is a mere read operation on the field's value instead of a call
   * to a getter, meaning that doing so doesn't actually change the [script].
   *
   * @see document
   */
  private val unevaluatedDocument = Document()

  /** Source code to be returned when translating calls made to the Kotlin API into JavaScript. */
  private var script = ""

  /** [JavaScriptString] that is always returned by [Document.HTMLInputElement.type]. */
  private val javaScriptString by lazy(::JavaScriptString)

  /**
   * Direct translation of accessing [Document] in JavaScript. Its side effect is that, as well as
   * any call to the [Document] API, it appends the equivalent expression or statement to the
   * [String] to be later returned by [build].
   */
  val document
    get() = unevaluatedDocument.also { script += "document" }

  /**
   * The `Boolean` object represents a truth value: `true` or `false`.
   *
   * For more information, refer to the
   * [documentation at Mozilla Developer Network](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Boolean).
   */
  object JavaScriptBoolean

  /**
   * The `String` object is used to represent and manipulate a sequence of characters.
   *
   * For more information, refer to the
   * [documentation at Mozilla Developer Network](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String).
   */
  inner class JavaScriptString {
    /**
     * The abstract operation IsStrictlyEqual takes arguments `x` (an ECMAScript language value) and
     * `y` (an ECMAScript language value) and returns a `Boolean`. It provides the semantics for the
     * `===` operator.
     *
     * For more information, refer to the
     * [ECMAScript Language Specification](https://tc39.es/ecma262/multipage/abstract-operations.html#sec-isstrictlyequal).
     *
     * @param other [String] to which this [JavaScriptString] is being compared.
     */
    fun isStrictlyEqual(other: String): JavaScriptBoolean {
      script += " === \"$other\""
      return JavaScriptBoolean
    }

    /**
     * The `toLowerCase()` method of `String` values returns this string converted to lower case.
     *
     * For more information, refer to the
     * [documentation at Mozilla Developer Network](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/toLowerCase).
     */
    fun toLowerCase(): JavaScriptString {
      script += ".toLowerCase()"
      return this
    }
  }

  /**
   * The `Document` interface represents any web page loaded in the browser and serves as an entry
   * point into the web page's content, which is the DOM tree.
   *
   * The DOM tree includes elements such as `<body>` and `<table>`, among many others. It provides
   * functionality globally to the document, like how to obtain the page's URL and create new
   * elements in the document.
   *
   * The `Document` interface describes the common properties and methods for any kind of document.
   * Depending on the document's type (e.g. HTML, XML, SVG, …), a larger API is available: HTML
   * documents, served with the `"text/html"` content type, also implement the `HTMLDocument`
   * interface, whereas XML and SVG documents implement the `XMLDocument` interface.
   *
   * For more information, refer to the
   * [documentation at Mozilla Developer Network](https://developer.mozilla.org/en-US/docs/Web/API/Document).
   */
  inner class Document {
    /** [HTMLCollection] that is always returned by [getElementsByClassName]. */
    private val htmlCollection by lazy(::HTMLCollection)

    /**
     * [HTMLInputElement] that is always returned by both [getElementById] and [HTMLCollection.get].
     */
    private val htmlInputElement by lazy(::HTMLInputElement)

    /**
     * The `HTMLInputElement` interface provides special properties and methods for manipulating the
     * options, layout, and presentation of <input> elements.
     *
     * For more information, refer to the
     * [documentation at Mozilla Developer Network](https://developer.mozilla.org/en-US/docs/Web/API/HTMLInputElement).
     */
    inner class HTMLInputElement {
      /**
       * The type property of the [HTMLInputElement] interface indicates the kind of data allowed in
       * the `<input>` element, or example a number, a date, or an email. Browsers will select the
       * appropriate widget and behavior to help users to enter a valid value.
       *
       * It reflects the `type` attribute of the `<input>` element.
       */
      val type
        get() = javaScriptString.also { script += ".type" }

      /**
       * Changes the string that represents the current value of the control. If the user enters a
       * value different from the value expected, this may return an empty string.
       *
       * For more information, refer to the
       * [documentation at Mozilla Developer Network](https://developer.mozilla.org/en-US/docs/Web/API/HTMLInputElement).
       *
       * @param value Value to which this [HTMLInputElement]'s should be changed.
       */
      fun setValue(value: String) {
        state { ".value = $value" }
      }

      /**
       * The `HTMLElement.click()` method simulates a mouse click on an element. When called on an
       * element, the element's click event is fired (unless its disabled attribute is set).
       *
       * For more information, refer to the
       * [documentation at Mozilla Developer Network](https://developer.mozilla.org/en-US/docs/Web/API/HTMLElement/click).
       */
      fun click() {
        state { ".click()" }
      }
    }

    /**
     * The [HTMLCollection] interface represents a generic collection (array-like object similar to
     * arguments) of elements (in document order) and offers methods and properties for selecting
     * from the list.
     *
     * For more information, refer to the
     * [documentation at Mozilla Developer Network](https://developer.mozilla.org/en-US/docs/Web/API/HTMLCollection).
     */
    inner class HTMLCollection {
      /**
       * Performs the operation within the [block] on each [HTMLInputElement] contained by this
       * [HTMLCollection].
       *
       * @param block Action to be run on each [HTMLInputElement].
       */
      fun forEach(block: (HTMLInputElement) -> Unit) {
        val referenceName = script.split('\n').last()
        state { "for (let element of $referenceName) {" }
        block(htmlInputElement)
        state { "}" }
      }

      /**
       * Obtains an [HTMLInputElement] at the given [index].
       *
       * @param index Index at which the [HTMLInputElement] to be obtained is.
       */
      operator fun get(index: Int): HTMLInputElement {
        script += "[$index]"
        return htmlInputElement
      }
    }

    /**
     * The `getElementById()` method of the [Document] interface returns an `Element` object
     * representing the element whose id property matches the specified string. Since element IDs
     * are required to be unique if specified, they're a useful way to get access to a specific
     * element quickly.
     *
     * For more information, refer to the
     * [documentation at Mozilla Developer Network](https://developer.mozilla.org/en-US/docs/Web/API/Document/getElementById).
     *
     * @param id The ID of the element to locate. The ID is a case-sensitive string which is unique
     *   within the document; only one element should have any given ID.
     */
    fun getElementById(id: String): HTMLInputElement {
      script += ".getElementById(\"$id\")"
      return htmlInputElement
    }

    /**
     * The `getElementsByClassName` method of [Document] interface returns an array-like object of
     * all child elements which have all of the given class name(s).
     *
     * When called on the document object, the complete document is searched, including the root
     * node. You may also call `getElementsByClassName()` on any element; it will return only
     * elements which are descendants of the specified root element with the given class name(s).
     *
     * For more information, refer to the
     * [documentation at Mozilla Developer Network](https://developer.mozilla.org/en-US/docs/Web/API/Document/getElementsByClassName).
     *
     * @param names A string representing the class name(s) to match; multiple class names are
     *   separated by whitespace.
     */
    fun getElementsByClassName(names: String): HTMLCollection {
      script += ".getElementsByClassName($names)"
      return htmlCollection
    }

    /**
     * The `getElementsByTagName` method of Document interface returns an [HTMLCollection] of
     * elements with the given tag name.
     *
     * The complete document is searched, including the root node. The returned [HTMLCollection] is
     * live, meaning that it updates itself automatically to stay in sync with the DOM tree without
     * having to call `document.getElementsByTagName()` again.
     *
     * For more information, refer to the
     * [documentation at Mozilla Developer Network](https://developer.mozilla.org/en-US/docs/Web/API/Document/getElementsByTagName).
     *
     * @param name A string representing the name of the elements. The special string * represents
     *   all elements.
     */
    fun getElementsByTagName(name: String): HTMLCollection {
      script += ".getElementsByTagName(\"$name\")"
      return htmlCollection
    }
  }

  /**
   * Appends a JavaScript conditional.
   *
   * @param condition Provides the condition to be satisfied in order for the [statement] to be run.
   * @param statement Action performed when the [condition] is `true`.
   */
  fun `if`(condition: () -> JavaScriptBoolean, statement: () -> Unit) {
    script += "if ("
    condition()
    state { ") {" }
    statement()
    script += '}'
  }

  /**
   * Provides a [String] in which all calls to the [Dom] API are represented as JavaScript source.
   */
  fun build(): String {
    return script
  }

  /**
   * Appends the finished statement produced by the [statement] lambda to the [script].
   *
   * @param statement Creates a statement.
   */
  private fun state(statement: () -> String) {
    script += "${statement()}\n"
  }
}
