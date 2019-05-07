package net.bartor.pong.models

class QuadraticMovement(private val part: MovablePart, private val coefficient: Float, private val drag: Float) :
    MovementInterface {
    private var lastX = 0f
    private var lastY = 0f

    private var xAcc = 0f
    private var yAcc = 0f
    override fun onInput(x: Float, y: Float) {
        lastX = x
        lastY = y
    }

    override fun update() {
        xAcc = (lastX - part.getX()) * coefficient
        yAcc = (lastY - part.getY()) * coefficient

        if (xAcc > 0) xAcc -= if (xAcc > drag) drag else xAcc
        else xAcc += if (-xAcc > drag) drag else xAcc
        if (yAcc > 0) yAcc -= if (yAcc > drag) drag else yAcc
        else yAcc += if (-yAcc > drag) drag else yAcc

        part.let {
            it.updatePosition(it.getX() + xAcc, it.getY() + yAcc)
        }
    }
}