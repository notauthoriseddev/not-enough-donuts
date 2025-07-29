package jmd.notenoughdonuts.gui.utils

import net.minecraft.client.gui.DrawContext
import java.awt.Color

object RenderUtils {
    fun drawRoundedRect(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Int,
        color: Color
    ) {
        // Draw main rectangle
        context.fill(
            x + radius,
            y,
            x + width - radius,
            y + height,
            color.rgb
        )
        context.fill(
            x,
            y + radius,
            x + width,
            y + height - radius,
            color.rgb
        )

        // Draw corners
        drawCircleQuarter(context, x + radius, y + radius, radius, 180f, 270f, color)
        drawCircleQuarter(context, x + width - radius, y + radius, radius, 270f, 360f, color)
        drawCircleQuarter(context, x + width - radius, y + height - radius, radius, 0f, 90f, color)
        drawCircleQuarter(context, x + radius, y + height - radius, radius, 90f, 180f, color)
    }

    private fun drawCircleQuarter(
        context: DrawContext,
        centerX: Int,
        centerY: Int,
        radius: Int,
        startAngle: Float,
        endAngle: Float,
        color: Color
    ) {
        val segments = 8
        val angleStep = (endAngle - startAngle) / segments
        
        for (i in 0 until segments) {
            val angle1 = Math.toRadians((startAngle + i * angleStep).toDouble())
            val angle2 = Math.toRadians((startAngle + (i + 1) * angleStep).toDouble())
            
            val x1 = centerX + (radius * Math.cos(angle1)).toInt()
            val y1 = centerY + (radius * Math.sin(angle1)).toInt()
            val x2 = centerX + (radius * Math.cos(angle2)).toInt()
            val y2 = centerY + (radius * Math.sin(angle2)).toInt()
            
            context.fill(
                Math.min(x1, x2),
                Math.min(y1, y2),
                Math.max(x1, x2) + 1,
                Math.max(y1, y2) + 1,
                color.rgb
            )
        }
    }
}
