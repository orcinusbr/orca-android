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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.snack

import androidx.compose.material3.SnackbarData

/**
 * [visuals][SnackbarData.visuals] cast to [OrcaSnackbarVisuals].
 *
 * @throws ClassCastException If [visuals][SnackbarData.visuals] is not an [OrcaSnackbarVisuals].
 */
internal val SnackbarData.orcaVisuals
  get() = visuals as OrcaSnackbarVisuals
