/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.disposition

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize

/**
 * Version of this [CornerBasedShape] with its [bottomEnd][CornerBasedShape.bottomEnd] set to
 * [ZeroCornerSize].
 */
internal val CornerBasedShape.withoutBottomEnd
  get() = RoundedCornerShape(topStart, topEnd, ZeroCornerSize, bottomStart)

/**
 * Version of this [CornerBasedShape] with its [bottomStart][CornerBasedShape.bottomStart] set to
 * [ZeroCornerSize].
 */
internal val CornerBasedShape.withoutBottomStart
  get() = RoundedCornerShape(topStart, topEnd, bottomEnd, ZeroCornerSize)

/**
 * Version of this [CornerBasedShape] with its [topEnd][CornerBasedShape.topEnd] set to
 * [ZeroCornerSize].
 */
internal val CornerBasedShape.withoutTopEnd
  get() = RoundedCornerShape(topStart, ZeroCornerSize, bottomEnd, bottomStart)

/**
 * Version of this [CornerBasedShape] with its [topStart][CornerBasedShape.topStart] set to
 * [ZeroCornerSize].
 */
internal val CornerBasedShape.withoutTopStart
  get() = RoundedCornerShape(ZeroCornerSize, topEnd, bottomEnd, bottomStart)
