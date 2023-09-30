package com.jeanbarrossilva.orca.core.http.feed.profile.toot.test

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.status.HttpAttachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.sample

/** [HttpAttachment]s returned by [samples]. **/
private val sampleHttpAttachments =
    Content.sample.attachments.map { HttpAttachment(previewUrl = "${it.url}", it.description) }

/** Sample [HttpAttachment]s. **/
internal val HttpAttachment.Companion.samples
    get() = sampleHttpAttachments
