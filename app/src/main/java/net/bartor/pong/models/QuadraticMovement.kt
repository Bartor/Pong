package net.bartor.pong.models

import kotlin.math.abs
import kotlin.math.sqrt

class QuadraticMovement(private val part: MovablePart, private val coefficient: Float) : MovementInterface {
    private var xAcc = 0f
    private var yAcc = 0f
    override fun onInput(x: Float, y: Float) {
        xAcc = (x - part.getX()) * coefficient
        yAcc = (y - part.getY()) * coefficient

        println("$xAcc $yAcc")
    }

    override fun update() {
        part.let {
            it.updatePosition(it.getX() + xAcc, it.getY() + yAcc)
        }
    }
}