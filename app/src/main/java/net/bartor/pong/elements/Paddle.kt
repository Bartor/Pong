package net.bartor.pong.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import net.bartor.pong.models.MovablePart

const val WIDTH = 50f

class Paddle(var height: Float, private var x: Float, private var y: Float) : MovablePart {
    private val color = Paint().also { it.setARGB(255, 40, 53, 143) }

    override fun updatePosition(x: Float, y: Float) {
        this.y = y
    }

    override fun getX(): Float {
        return x
    }

    override fun getY(): Float {
        return y
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(RectF(x, y, x + WIDTH, y + height), color)
    }

    fun getWidth(): Float {
        return WIDTH
    }
}