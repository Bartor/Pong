package net.bartor.pong.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

const val WIDTH = 20f

class Paddle(var height: Float, var x: Float, var y: Float) {
    private val color = Paint().also { it.setARGB(255, 0x0, 0x70, 0xff) }

    fun draw(canvas: Canvas) {
        canvas.drawRect(RectF(x, y, x + WIDTH, y + height), color)
    }

    fun getWidth() : Float {
        return WIDTH
    }
}