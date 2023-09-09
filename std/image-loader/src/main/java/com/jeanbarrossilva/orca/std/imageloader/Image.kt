package com.jeanbarrossilva.orca.std.imageloader

import java.util.Objects

/**
 * An image.
 *
 * @param width How wide this [Image] is.
 * @param height How tall this [Image] is.
 * @param pixels [Pixel]s by which this [Image] is composed.
 **/
class Image private constructor(val width: Int, val height: Int, val pixels: List<Pixel>) {
    init {
        if (pixels.size < width * height) {
            throw IllegalArgumentException("Insufficient amount of pixels for $width x $height.")
        }
    }

    /**
     * Smallest addressable element in an [Image].
     *
     * @param x Location on the x-axis.
     * @param y Location on the y-axis.
     * @param color [Int] form of the color by which this [Pixel] is colored.
     **/
    class Pixel internal constructor(val x: Int, val y: Int, val color: Int) {
        override fun equals(other: Any?): Boolean {
            return other is Pixel && x == other.x && y == other.y && color == other.color
        }

        override fun hashCode(): Int {
            return Objects.hash(x, y, color)
        }

        override fun toString(): String {
            return "Pixel(x=$x, y=$y, color=$color)"
        }
    }

    /**
     * Configures an [Image] to be built.
     *
     * @param width How wide the [Image] is.
     * @param height How tall the [Image] is.
     **/
    class Builder internal constructor(private val width: Int, private val height: Int) {
        /** [Pixel]s by which the [Image] will be composed. **/
        private val pixels = mutableListOf<Pixel>()

        /**
         * Adds a [Pixel] to the next remaining space in the x-axis, jumping to the next column if
         * the row has been filled.
         *
         * @param color [Int] form of the color by which the [Pixel] will be colored.
         * @throws IllegalStateException If the [Image] cannot have more [Pixel]s added to it.
         **/
        fun pixel(color: Int) {
            val lastPixel = pixels.lastOrNull()
            val x = lastPixel?.x?.inc()?.mirror(0, width.dec()) ?: 0
            val y = if (lastPixel != null && x == 0) lastPixel.y.inc() else lastPixel?.y ?: 0
            if (y >= height) {
                throw IllegalStateException("Coordinate out of $width x $height bounds: ($x, $y).")
            }
            val pixel = Pixel(x, y, color)
            pixels.add(pixel)
        }

        /** Builds the [Image] with the provided configurations. **/
        internal fun build(): Image {
            val pixelsAsList = pixels.toList()
            return Image(width, height, pixelsAsList)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Image &&
            width == other.width &&
            height == other.height &&
            pixels.containsAll(other.pixels)
    }

    override fun hashCode(): Int {
        return Objects.hash(width, height, pixels)
    }

    override fun toString(): String {
        return "Image(width=$width, height=$height, pixels=$pixels)"
    }
}
