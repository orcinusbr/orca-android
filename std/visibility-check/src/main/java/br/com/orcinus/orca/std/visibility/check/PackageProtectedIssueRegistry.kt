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

package br.com.orcinus.orca.std.visibility.check

import br.com.orcinus.orca.std.visibility.check.detection.PackageProtectedDetector
import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API

/**
 * [IssueRegistry] containing the [PackageProtectedDetector.issue], which characterizes problems of
 * references to structures that have been marked as package-protected from outside of the module in
 * which they have been declared or any of its children.
 */
@Suppress("unused")
internal class PackageProtectedIssueRegistry : IssueRegistry() {
  override val vendor = Vendor(vendorName = "Orcinus", contact = "jean@orcinus.com.br")
  override val api = CURRENT_API
  override val issues = listOf(PackageProtectedDetector.issue)
}
