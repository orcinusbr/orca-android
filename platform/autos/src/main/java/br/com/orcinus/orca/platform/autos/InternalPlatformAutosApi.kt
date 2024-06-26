/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.platform.autos

import br.com.orcinus.orca.std.visibility.PackageProtected

/**
 * Denotes that an API should only be referenced by the internal structures of the platform's
 * implementation of αὐτός.
 */
@PackageProtected("This API is internal to the platform implementation of αὐτός.")
internal annotation class InternalPlatformAutosApi
