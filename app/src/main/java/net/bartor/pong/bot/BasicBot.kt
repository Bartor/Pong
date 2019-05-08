package net.bartor.pong.bot

import net.bartor.pong.GameDiff
import net.bartor.pong.elements.Ball
import java.util.*

class BasicBot(private var ball: Ball, private val diff: GameDiff) {
    private var moves = LinkedList<Pair<Float, Float>>()

    init {
        for (i in 0 until getDiffIterationSize()) moves.add(Pair(ball.x, ball.y))
    }

    fun update() : Pair<Float, Float> {
        moves.add(Pair(ball.x + getInFrontItersSize()*ball.xSpeed, ball.y + getInFrontItersSize()*ball.ySpeed))
        return moves.removeFirst()
    }

    private fun getDiffIterationSize(): Int {
        return when (diff) {
            GameDiff.EASY -> 30
            GameDiff.MEDIUM -> 10
            GameDiff.HARD -> 1
        }
    }

    private fun getInFrontItersSize(): Int {
        return when (diff) {
            GameDiff.EASY -> 0
            GameDiff.MEDIUM -> 2
            GameDiff.HARD -> 5
        }
    }
}