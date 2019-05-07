package net.bartor.pong.models

class QuadraticMovement(private val part: MovablePart) : MovementInterface {
    private var xAcc = 0f
    private var yAcc = 0f
    override fun onInput(x: Float, y: Float) {
        xAcc = (x - part.getX())*(x - part.getX()) * if (x > part.getX()) 1 else -1
        yAcc = (y - part.getY())*(y - part.getY()) * if (y > part.getY()) 1 else -1
    }

    override fun update() {
        part.let {
            it.updatePosition(it.getX() + xAcc, it.getY() + yAcc)
        }
    }
}