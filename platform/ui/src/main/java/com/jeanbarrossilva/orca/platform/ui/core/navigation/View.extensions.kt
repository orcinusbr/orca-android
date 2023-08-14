package com.jeanbarrossilva.orca.platform.ui.core.navigation

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.reflect.KClass

/**
 * Verifies if this [View]'s (if [isInclusive]) or one of its children's type is equal to [T] and
 * then returns it if it is.
 *
 * This method is run recursively on each child if it's a [ViewGroup] and continues to
 * do so if a child's child is a [ViewGroup], until either the [View] we're searching for is found
 * or none is found at all, in which case an [IllegalStateException] is thrown.
 *
 * @param T [View] to be found.
 * @param isInclusive Whether this [View] should be taken into account in the search and returned if
 * its type matches [T].
 * @throws IllegalStateException If no matching [View] is found.
 **/
internal inline fun <reified T : View> View.get(isInclusive: Boolean = true): T {
    return get(T::class, isInclusive)
}

/**
 * Verifies if this [View]'s (if [isInclusive]) or one of its children's [KClass] equal to the given
 * [viewClass].
 *
 * This method is run recursively on each child if it's a [ViewGroup] and continues to
 * do so if a child's child is a [ViewGroup], until either the [View] we're searching for is found
 * or none is found at all, in which case an [IllegalStateException] is thrown.
 *
 * @param T [View] to be found.
 * @param viewClass [KClass] of the [View] to be found.
 * @param isInclusive Whether this [View] should be taken into account in the search and returned if
 * its [KClass] matches the [viewClass].
 * @throws IllegalStateException If no matching [View] is found.
 **/
private fun <T : View> View.get(viewClass: KClass<T>, isInclusive: Boolean): T {
    @Suppress("UNCHECKED_CAST")
    return when {
        isInclusive && this::class == viewClass ->
            this as T
        this is ViewGroup ->
            children.firstNotNullOfOrNull { it.get(viewClass, isInclusive = true) }
        else ->
            null
    }
        ?: throw IllegalStateException("No ${viewClass.simpleName} found from $this.")
}
