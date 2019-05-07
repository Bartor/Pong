package net.bartor.pong

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import net.bartor.pong.elements.Ball
import net.bartor.pong.elements.Paddle
import net.bartor.pong.models.QuadraticMovement

class GameView(context: Context) : SurfaceView(context),
    SurfaceHolder.Callback {
    private val thread: GameThread
    private lateinit var callback : PointCounter

    private var ball = Ball(10f, 0f, 0f)
    private val lPaddle = Paddle(0f, 0f, 0f)
    private val rPaddle = Paddle(0f, 0f, 0f)
    private val lPaddleMovement = QuadraticMovement(lPaddle)
    private val rPaddleMovement = QuadraticMovement(rPaddle)

    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

    fun update() {
        ball.update()
        if (ball.x >= lPaddle.getX() && ball.x <= lPaddle.getX() + lPaddle.getWidth() && ball.y <= lPaddle.getY() + lPaddle.height && ball.y >= lPaddle.getY()) {
            ball.randomizedBounce(true, 0.2f)
            ball.speedUp(1.005f)
        }

        if (ball.x + ball.getSize() >= rPaddle.getX() && ball.x <= rPaddle.getX() + rPaddle.getWidth() && ball.y <= rPaddle.getY() + rPaddle.height && ball.y >= rPaddle.getY()) {
            ball.randomizedBounce(true, 0.2f)
            ball.speedUp(1.05f)
        }

        if (ball.x <= 0) {
            callback.onPointCount(true)
            nextRound()
        }
        if (ball.x + ball.getSize() >= width) {
            callback.onPointCount(false)
            nextRound()
        }
        if (ball.y <= 0 || ball.y + ball.getSize() >= height) ball.bounce(false)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas == null) return

        ball.draw(canvas)
        lPaddle.draw(canvas)
        rPaddle.draw(canvas)
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        lPaddle.height = height / 3f
        rPaddle.height = height / 3f

        lPaddle.updatePosition(width / 8f, lPaddle.getY())
        rPaddle.updatePosition(width - width / 8f - rPaddle.getWidth(), rPaddle.getY())

        nextRound()

        thread.running = true
        thread.start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        for (i in 0 until event!!.pointerCount) {
            if (event.getX(i) < width / 2f) {
                lPaddleMovement.onInput(event.x, event.y)
            } else {
                rPaddleMovement.onInput(event.x, event.y)
            }
        }
        return true
    }

    private fun nextRound() {
        ball.y = height / 2f
        ball.x = width / 2f

        rPaddle.updatePosition(rPaddle.getX(), height / 2f - rPaddle.height / 2)
        lPaddle.updatePosition(lPaddle.getX(), height / 2f - rPaddle.height / 2)
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        thread.running = false
        thread.join()
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {}

    fun setOnPointCounter(pointCounter: PointCounter) {
        callback = pointCounter
    }

    interface PointCounter {
        fun onPointCount(left: Boolean)
    }
}