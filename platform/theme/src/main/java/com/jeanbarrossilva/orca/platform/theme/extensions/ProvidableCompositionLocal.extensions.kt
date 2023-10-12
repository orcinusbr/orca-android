package com.jeanbarrossilva.orca.platform.theme.extensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/** Material 3's [ProvidableCompositionLocal] for [Typography]. */
internal val LocalTypography
  @Composable get() = material3CompositionLocalOf(MaterialTheme.typography)

/**
 * Gets Material 3's [ProvidableCompositionLocal] for the given type [T] through reflection (since
 * it's private API). If it fails to do so, creates one that provides the [fallback].
 *
 * @param fallback Instance of [T] to fallback to and provide to the created
 *   [ProvidableCompositionLocal] if none is found at the default site.
 * @throws IllegalStateException When the name of [T] cannot be obtained.
 * @throws ClassCastException When the field at the default site exists but is not a
 *   [ProvidableCompositionLocal]<[T]>.
 */
private inline fun <reified T : Any> material3CompositionLocalOf(
  fallback: T
): ProvidableCompositionLocal<T> {
  val typeClass = T::class
  val typeName =
    typeClass.simpleName ?: throw IllegalStateException("Could not get $typeClass's name.")
  val siteClassName = "androidx.compose.material3.${typeName}Kt"
  val fieldName = "Local$typeName"

  @Suppress("UNCHECKED_CAST")
  return Class.forName(siteClassName)
    ?.getDeclaredField(fieldName)
    ?.apply { isAccessible = true }
    ?.get(null)
    ?.let { it as ProvidableCompositionLocal<T> }
    ?: staticCompositionLocalOf { fallback }
}
