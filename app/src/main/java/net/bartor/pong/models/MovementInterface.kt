package net.bartor.pong.models

interface MovementInterface {
    public fun onInput(x: Float, y: Float)
    public fun update()
}