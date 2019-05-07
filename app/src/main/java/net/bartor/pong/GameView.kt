package net.bartor.pong

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import net.bartor.pong.elements.Ball
import net.bartor.pong.elements.Paddle
import net.bartor.pong.models.MovementLimist
import net.bartor.pong.models.QuadraticMovement

class GameView(context: Context) : SurfaceView(context),
    SurfaceHolder.Callback {
    private val thread: GameThread
    private lateinit var callback : PointCounter

    private lateinit var ball : Ball
    private lateinit var lPaddle : Paddle
    private lateinit var rPaddle : Paddle
    private lateinit var lPaddleMovement : QuadraticMovement
    private lateinit var rPaddleMovement : QuadraticMovement

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
        lPaddleMovement.update()
        rPaddleMovement.update()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas == null) return

        ball.draw(canvas)
        lPaddle.draw(canvas)
        rPaddle.draw(canvas)
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        lPaddle = Paddle(height / 3f, width / 8f, height/2f)
        rPaddle = Paddle(height / 3f, 7 * width / 8f, height/2f)

        lPaddle.setLimits(MovementLimist(0f, 0f, 0f, height.toFloat()))
        rPaddle.setLimits(MovementLimist(0f, 0f, 0f, height.toFloat()))

        lPaddleMovement = QuadraticMovement(lPaddle, 100f/height, 0.3f)
        rPaddleMovement = QuadraticMovement(rPaddle, 100f/height, 0.3f)
        //set last positions to middle
        lPaddleMovement.onInput(0f, height/2f - lPaddle.height/2f)
        rPaddleMovement.onInput(0f, height/2f - rPaddle.height/2f)

        nextRound()

        thread.running = true
        thread.start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        for (i in 0 until event!!.pointerCount) {
            if (event.getX(i) < width / 2f) {
                lPaddleMovement.onInput(event.getX(i), event.getY(i) - lPaddle.height/2f)
            } else {
                rPaddleMovement.onInput(event.getX(i), event.getY(i) - rPaddle.height/2f)
            }
        }
        return true
    }

    private fun nextRound() {
        ball = Ball(20f, width/2f, height/2f)
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