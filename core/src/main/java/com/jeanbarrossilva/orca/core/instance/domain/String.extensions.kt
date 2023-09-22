package com.jeanbarrossilva.orca.core.instance.domain

/** Whether this [String] contains no illegal characters. **/
internal val String.isLegal
    get() = Domain.illegalCharacters.none { it in this }
