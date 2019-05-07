package net.bartor.pong.models

interface MovablePart {
    public fun updatePosition(x: Float, y: Float)
    public fun setLimits(limits: MovementLimits)
    public fun getX(): Float
    public fun getY(): Float
}

data class MovementLimits(val xTop: Float, val yTop: Float, val xBot: Float, val yBot: Float)