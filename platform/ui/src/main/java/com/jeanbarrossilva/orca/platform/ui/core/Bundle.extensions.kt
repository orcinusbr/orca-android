package com.jeanbarrossilva.orca.platform.ui.core

import android.os.Bundle
import androidx.core.os.bundleOf

/**
 * Creates a [Bundle] with the given key-value [pairs] put into it.
 *
 * @param pairs Elements to be put into the resulting [Bundle].
 * @return [Bundle], or `null` if [pairs] is empty.
 **/
@PublishedApi
internal fun bundleOf(vararg pairs: Pair<String, Any?>): Bundle? {
    val hasArgs = pairs.isNotEmpty()
    return if (hasArgs) bundleOf(*pairs) else null
}
