package net.bartor.pong.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import kotlin.math.*
import kotlin.random.Random

const val SIZE = 40f

class Ball(private var speed: Float, var x: Float, var y: Float) {
    private val color = Paint().also { it.setARGB(255, 40, 53, 143) }

    var xSpeed = 0f
    var ySpeed = 0f

    init {
        val angle = 1/2f*Math.PI.toFloat() + (if (Random.nextBoolean()) -1 else +1)*(Random.nextFloat() % 1/3*Math.PI.toFloat()) + (if (Random.nextBoolean()) Math.PI.toFloat() else 0f)
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
        val angle = 1/2f*Math.PI.toFloat() + (if (Random.nextBoolean()) -1 else +1)*(Random.nextFloat() % random*Math.PI.toFloat()) + (if (xSpeed < 0) Math.PI.toFloat() else 0f)

        if (side) {
            xSpeed = -speed*sin(angle)
            ySpeed = speed*cos(angle)
        } else {
            xSpeed *= speed*sin(angle)
            ySpeed *= -speed*cos(angle)
        }
    }

    fun draw(canvas: Canvas) {
        canvas.drawOval(RectF(x, y, x + SIZE, y + SIZE), color)
    }

    fun getSize(): Float {
        return SIZE
    }

    fun speedUp(factor: Float) {
        speed *= factor
        xSpeed *= factor
        ySpeed *= factor
    }
}
