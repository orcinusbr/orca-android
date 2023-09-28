package com.jeanbarrossilva.orca.core.http.auth.authorization.selectable.list

import com.jeanbarrossilva.orca.core.http.auth.authorization.selectable.Selectable

/**
 * Converts this [Collection] into a [SelectableList] and selects the given [element].
 *
 * @param element Element to be selected.
 **/
internal fun <T> Collection<T>.select(element: T): SelectableList<T> {
    return selectIf { _, current ->
        element == current
    }
}

/**
 * Converts this [Collection] into a [SelectableList] and selects its first element.
 **/
internal fun <T> Collection<T>.selectFirst(): SelectableList<T> {
    return selectIf { index, _ ->
        index == 0
    }
}

/**
 * Converts this [Collection] into a [SelectableList].
 *
 * @param selection Predicate that determines whether the element that's been given to it is
 * selected.
 **/
private fun <T> Collection<T>.selectIf(selection: (index: Int, T) -> Boolean):
    SelectableList<T> {
    val elements = mapIndexed { index, element ->
        @Suppress("DiscouragedApi")
        (
            Selectable(
                element,
                selection(index, element)
            )
            )
    }

    @Suppress("DiscouragedApi")
    return SelectableList(elements)
}
