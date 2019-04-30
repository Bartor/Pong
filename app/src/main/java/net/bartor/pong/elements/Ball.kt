package net.bartor.pong.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

const val SIZE = 40f

class Ball(speed: Float, var x: Float, var y: Float) {
    private val color = Paint().also { it.setARGB(255, 0x0, 0x70, 0xff) }

    private var xSpeed = 0f
    private var ySpeed = 0f

    init {
        val angle = Random.nextFloat() % (2 * Math.PI.toFloat())
        xSpeed = speed * sin(angle)
        ySpeed = speed * cos(angle)
    }

    fun update() {
        x += xSpeed
        y += ySpeed
    }

    fun bounce(side: Boolean) {
        if (side) xSpeed = -xSpeed
        else ySpeed = -ySpeed
    }

    fun randomizedBounce(side: Boolean, random: Float) {
        val normalizedSpeed = sqrt(xSpeed*xSpeed + ySpeed*ySpeed)
        var angle = atan(xSpeed/ySpeed)
        angle += (Random.nextFloat() % random) * 2*Math.PI.toFloat()
        if (side) {
            xSpeed = - normalizedSpeed * sin(angle)
            ySpeed = normalizedSpeed * cos(angle)
        } else {
            xSpeed = normalizedSpeed * sin(angle)
            ySpeed = - normalizedSpeed * cos(angle)
        }
    }

    fun draw(canvas: Canvas) {
        canvas.drawOval(RectF(x, y, x + SIZE, y + SIZE), color)
    }

    fun getSize(): Float {
        return SIZE
    }

    fun speedUp(factor: Float) {
        xSpeed *= factor
        ySpeed *= factor
    }
}
