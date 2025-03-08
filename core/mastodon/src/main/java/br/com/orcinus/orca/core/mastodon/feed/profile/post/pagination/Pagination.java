/*
 * Copyright © 2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination;

import androidx.annotation.NonNull;
import br.com.orcinus.orca.core.feed.Pages;
import br.com.orcinus.orca.core.feed.profile.post.Post;
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.page.Page;
import io.ktor.client.statement.HttpResponse;
import kotlinx.coroutines.Deferred;

/**
 * Association of a deferred response received for a request for fetching {@link Post}s in a given
 * page and such page itself.
 *
 * @param page Page in which the request will be sent. May be invalid — the constructor of this
 *     class does not validate it; its validation is expected to have been performed previously by
 *     the caller.
 * @param responseDeferred Deferred {@link HttpResponse} with the DTOs of the {@link Post}s at the
 *     {@link #page}.
 * @see Pages#validate(int)
 */
record Pagination(@Page int page, @NonNull Deferred<HttpResponse> responseDeferred) {}
