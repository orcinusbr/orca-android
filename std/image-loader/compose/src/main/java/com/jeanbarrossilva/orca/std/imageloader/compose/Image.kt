package com.jeanbarrossilva.orca.std.imageloader.compose

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.placeholder.Placeholder
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.compose.Image as _Image
import java.net.URL

/**
 * [Composable] that asynchronously loads the image to which the [url] leads with the specified
 * [loader].
 *
 * @param url [URL] of the image to be loaded.
 * @param contentDescription Description of what the image contains.
 * @param modifier [Modifier] to be applied to the underlying [Placeholder].
 * @param shape [Shape] by which this [Image][_Image] will be clipped.
 * @param contentScale Defines how the image will be scaled within this [Composable]'s bounds.
 **/
@Composable
fun Image(
    url: URL,
    contentDescription: String,
    modifier: Modifier = Modifier,
    loader: ImageLoader = CoilImageLoader(LocalContext.current),
    shape: Shape = RectangleShape,
    contentScale: ContentScale = ContentScale.Fit
) {
    var bitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    LaunchedEffect(loader) {
        bitmap = loader.load(url).toBitmap().asImageBitmap()
    }

    Placeholder(modifier, isLoading = bitmap == null, shape) {
        bitmap?.let {
            Image(it, contentDescription, contentScale = contentScale)
        }
    }
}

/** Preview of an [Image][_Image]. **/
@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ImagePreview() {
    _Image(
        URL("https://images.unsplash.com/photo-1692890846581-da1a95435f34"),
        contentDescription = "Preview image",
        Modifier.size(128.dp)
    )
}
