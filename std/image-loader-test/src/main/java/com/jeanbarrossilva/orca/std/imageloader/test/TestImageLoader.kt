package com.jeanbarrossilva.orca.std.imageloader.test

import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.buildImage
import java.net.URL

/** [ImageLoader] that loads an empty [Image]. **/
object TestImageLoader : ImageLoader() {
    override suspend fun onLoad(width: Int, height: Int, url: URL): Image {
        return buildImage(width = 1, height = 1) {
            pixel(0)
        }
    }
}
