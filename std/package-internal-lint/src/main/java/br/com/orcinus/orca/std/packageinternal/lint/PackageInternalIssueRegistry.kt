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

package br.com.orcinus.orca.std.packageinternal.lint

import br.com.orcinus.orca.std.packageinternal.lint.detection.PackageInternalDetector
import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor

/**
 * [IssueRegistry] containing the [PackageInternalDetector.issue], which characterizes problems of
 * accesses to structures that have been marked as package-internal from outside of the module in
 * which they have been declared or any of its derivatives.
 */
internal class PackageInternalIssueRegistry : IssueRegistry() {
  override val vendor = Vendor(vendorName = "Orcinus", contact = "jean@orcinus.com.br")
  override val issues = listOf(PackageInternalDetector.issue)
}
