package com.jeanbarrossilva.orca.platform.ui.core.replacement

import com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable.replaceOnceBy

/**
 * [MutableList] that replaces a given element when its [selector] matches that of the other one
 * being added, maintaining its index.
 *
 * @param elements [MutableList] to which this [ReplacementList]'s functionality will be delegated,
 * except for that of [add].
 * @param selector Returns the value by which elements should be compared when replacing them.
 **/
internal class ReplacementList<I, O>(
    private val elements: MutableList<I>,
    private val selector: (I) -> O
) : MutableList<I> by elements {
    override fun equals(other: Any?): Boolean {
        return other is MutableList<*> && size == other.size && areElementsPositionallyEqual(other)
    }

    override fun hashCode(): Int {
        return elements.hashCode()
    }

    override fun toString(): String {
        return elements.toString()
    }

    override fun add(element: I): Boolean {
        val hasBeenReplaced =
            elements.replaceOnceBy({ element }) { selector(element) == selector(it) }
        if (!hasBeenReplaced) {
            elements.add(element)
        }
        return true
    }

    /**
     * Checks whether this [ReplacementList] and [other] have elements that don't vary neither in
     * identity nor in index.
     *
     * @param other [MutableList] whose elements will be compared to those of this
     * [ReplacementList].
     **/
    private fun areElementsPositionallyEqual(other: MutableList<*>): Boolean {
        var areElementsPositionallyEqual: Boolean? = null
        other.forEachIndexed { index, element ->
            areElementsPositionallyEqual = get(index) == element
            if (areElementsPositionallyEqual == false) {
                return@forEachIndexed
            }
        }
        return areElementsPositionallyEqual == true
    }
}
