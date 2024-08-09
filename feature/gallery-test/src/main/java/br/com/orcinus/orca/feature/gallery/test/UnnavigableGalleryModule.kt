/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.feature.gallery.test

import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.feature.gallery.GalleryBoundary
import br.com.orcinus.orca.feature.gallery.GalleryModule
import br.com.orcinus.orca.std.injector.module.injection.immediateInjectionOf
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf

/**
 * [GalleryModule] to which a no-op [GalleryBoundary] is injected.
 *
 * @param postProvider [SamplePostProvider] to be injected.
 * @see NoOpGalleryBoundary
 */
class UnnavigableGalleryModule(postProvider: SamplePostProvider) :
  GalleryModule(immediateInjectionOf(postProvider), lazyInjectionOf { NoOpGalleryBoundary })
