package com.jeanbarrossilva.orca.core.http.auth.authorization.selectable.list

import com.jeanbarrossilva.orca.core.http.auth.authorization.selectable.Selectable

/** Creates an empty [SelectableList]. **/
internal inline fun <reified T> emptySelectableList(): SelectableList<T> {
    return selectableListOf()
}

/**
 * Creates a [SelectableList] with the given [elements].
 *
 * @param elements [Selectable]s to be put in the [SelectableList].
 **/
internal fun <T> selectableListOf(vararg elements: Selectable<T>): SelectableList<T> {
    val list = listOf(*elements)

    @Suppress("DiscouragedApi")
    return SelectableList(list)
}
