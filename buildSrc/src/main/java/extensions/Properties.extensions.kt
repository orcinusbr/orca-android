import java.io.File
import java.util.Properties

/**
 * Creates [Properties] according to the `local.properties` file.
 *
 * @param root Directory in which the `local.properties` file is.
 **/
fun localProperties(root: File): Properties {
    val file = File(root, "local.properties")
    return Properties().apply { tryToLoad(file) }
}

/**
 * Loads the given [file] into these [Properties].
 *
 * @param file [File] to be loaded.
 **/
private fun Properties.load(file: File) {
    file.inputStream().reader().use {
        load(it)
    }
}

/**
 * Loads the given [file] into these [Properties] if it's a normal file.
 *
 * @param file [File] to be loaded.
 **/
private fun Properties.tryToLoad(file: File) {
    if (file.isFile) {
        load(file)
    }
}
