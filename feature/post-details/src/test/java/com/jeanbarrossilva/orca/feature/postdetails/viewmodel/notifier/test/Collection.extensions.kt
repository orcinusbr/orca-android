package com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier.test

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

/**
 * Returns the [KFunction] whose parameters' [KClass]es match the given ones.
 *
 * @param T Value returned by each of the [KFunction]s.
 */
internal operator fun <T> Collection<KFunction<T>>.get(
  vararg parameterClasses: KClass<*>
): KFunction<T> {
  return first { function ->
    function.parameters.map { parameter -> parameter.type.classifier } == parameterClasses.toList()
  }
}
