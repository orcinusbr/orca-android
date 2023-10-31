package com.jeanbarrossilva.orca.std.buildable.processor

import com.squareup.kotlinpoet.KModifier

/** Whether this [KModifier] denotes a visibility level. */
internal val KModifier.isOfVisibility
  get() =
    this == KModifier.INTERNAL ||
      this == KModifier.PRIVATE ||
      this == KModifier.PROTECTED ||
      this == KModifier.PUBLIC
