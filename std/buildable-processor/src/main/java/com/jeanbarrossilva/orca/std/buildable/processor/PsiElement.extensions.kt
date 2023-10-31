package com.jeanbarrossilva.orca.std.buildable.processor

import org.jetbrains.kotlin.com.intellij.psi.PsiElement

/** Flattens all of the [PsiElement]s' children and does the same for each of them recursively. */
internal fun PsiElement.flattenChildren(): Sequence<PsiElement> {
  return sequence {
    yieldAll(children.iterator())
    yieldAll(children.flatMap(PsiElement::flattenChildren))
  }
}
