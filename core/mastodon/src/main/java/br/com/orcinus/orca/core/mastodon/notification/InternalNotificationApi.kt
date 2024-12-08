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

package br.com.orcinus.orca.core.mastodon.notification

import br.com.orcinus.orca.std.visibility.PackageProtected

/**
 * Denotes that a structure should not be referenced from APIs unrelated to Mastodon notifications.
 */
@PackageProtected(
  "This API is to be referenced only by structures related to the subscription to and receipt of " +
    "push updates forwarded from the Mastodon server to the device in the form of system " +
    "notifications. These reside exclusively in the " +
    "`br.com.orcinus.orca.core.mastodon.notification` package."
)
@Target(
  AnnotationTarget.CLASS,
  AnnotationTarget.CONSTRUCTOR,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY
)
internal annotation class InternalNotificationApi
