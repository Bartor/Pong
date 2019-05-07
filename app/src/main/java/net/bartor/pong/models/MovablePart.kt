package net.bartor.pong.models

interface MovablePart {
    public fun updatePosition(x: Float, y: Float)
    public fun getX(): Float
    public fun getY(): Float
}