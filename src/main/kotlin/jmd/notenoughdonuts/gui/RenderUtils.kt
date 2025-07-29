package jmd.notenoughdonuts.gui

import net.minecraft.client.gui.DrawContext
import java.awt.Color

object RenderUtils {
    fun drawRoundedRect(context: DrawContext, x: Int, y: Int, width: Int, height: Int, radius: Int, color: Color) {
        // Draw main rectangle
        context.fill(x + radius, y, x + width - radius, y + height, color.rgb)
        context.fill(x, y + radius, x + width, y + height - radius, color.rgb)

        // Draw corners (approximated with smaller rectangles)
        for (i in 0..radius) {
            val currentRadius = (radius - i)
            context.fill(
                x + i,
                y + currentRadius,
                x + i + 1,
                y + height - currentRadius,
                color.rgb
            )
            context.fill(
                x + width - i - 1,
                y + currentRadius,
                x + width - i,
                y + height - currentRadius,
                color.rgb
            )
        }
    }

    fun drawGradientRect(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        startColor: Color,
        endColor: Color,
        horizontal: Boolean = false
    ) {
        for (i in 0..if (horizontal) width else height) {
            val progress = i.toFloat() / if (horizontal) width.toFloat() else height.toFloat()
            val currentColor = interpolateColors(startColor, endColor, progress)

            if (horizontal) {
                context.fill(x + i, y, x + i + 1, y + height, currentColor.rgb)
            } else {
                context.fill(x, y + i, x + width, y + i + 1, currentColor.rgb)
            }
        }
    }

    private fun interpolateColors(color1: Color, color2: Color, progress: Float): Color {
        val r = (color1.red + (color2.red - color1.red) * progress).toInt()
        val g = (color1.green + (color2.green - color1.green) * progress).toInt()
        val b = (color1.blue + (color2.blue - color1.blue) * progress).toInt()
        val a = (color1.alpha + (color2.alpha - color1.alpha) * progress).toInt()
        return Color(r, g, b, a)
    }
}
