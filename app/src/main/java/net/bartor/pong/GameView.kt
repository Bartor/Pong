package net.bartor.pong

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import net.bartor.pong.bot.BasicBot
import net.bartor.pong.elements.Ball
import net.bartor.pong.elements.Paddle
import net.bartor.pong.models.MovementLimist
import net.bartor.pong.models.QuadraticMovement

class GameView(context: Context, private val mode: GameMode, private val diff: GameDiff) : SurfaceView(context),
    SurfaceHolder.Callback {
    private val thread: GameThread
    private lateinit var callback: PointCounter

    private lateinit var bot: BasicBot
    private lateinit var ball: Ball
    private lateinit var lPaddle: Paddle
    private lateinit var rPaddle: Paddle
    private lateinit var lPaddleMovement: QuadraticMovement
    private lateinit var rPaddleMovement: QuadraticMovement

    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

    fun update() {
        ball.update()
        if (collision(ball, lPaddle)) {
            ball.randomizedBounce(true, 0.2f)
            ball.speedUp(1.05f)
            callback.onBounce()
        }

        if (collision(ball, rPaddle)) {
            ball.randomizedBounce(true, 0.2f)
            ball.speedUp(1.05f)
            callback.onBounce()
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

        if (mode == GameMode.BOT) bot.update().let {
            lPaddleMovement.onInput(it.first, it.second)
        }
    }

    private fun collision(ball: Ball, paddle: Paddle): Boolean {
        val ballX = ball.x + ball.xSpeed + ball.getSize()
        val ballY = ball.y + ball.ySpeed + ball.getSize()
        return ballX > paddle.getX() && ballX < paddle.getX() + paddle.getWidth() && ballY > paddle.getY() && ballY < paddle.getY() + paddle.height
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas == null) return

        ball.draw(canvas)
        lPaddle.draw(canvas)
        rPaddle.draw(canvas)
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        lPaddle = Paddle(getDiffHeight(), width / 8f, height / 2f)
        rPaddle = Paddle(getDiffHeight(), 7 * width / 8f, height / 2f)

        lPaddle.setLimits(MovementLimist(0f, 0f, 0f, height.toFloat()))
        rPaddle.setLimits(MovementLimist(0f, 0f, 0f, height.toFloat()))

        lPaddleMovement = QuadraticMovement(lPaddle, 100f / height, 0.3f)
        rPaddleMovement = QuadraticMovement(rPaddle, 100f / height, 0.3f)
        //set last positions to middle
        lPaddleMovement.onInput(0f, height / 2f - lPaddle.height / 2f)
        rPaddleMovement.onInput(0f, height / 2f - rPaddle.height / 2f)

        nextRound()

        thread.running = true
        thread.start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        for (i in 0 until event!!.pointerCount) {
            if (event.getX(i) < width / 2f) {
                if (mode != GameMode.BOT) lPaddleMovement.onInput(event.getX(i), event.getY(i) - lPaddle.height / 2f)
            } else {
                rPaddleMovement.onInput(event.getX(i), event.getY(i) - rPaddle.height / 2f)
            }
        }
        return true
    }

    private fun nextRound() {
        ball = Ball(getDiffSpeed(), width / 2f, height / 2f)
        if (mode == GameMode.BOT) bot = BasicBot(ball, diff)
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
        fun onBounce()
    }

    private fun getDiffSpeed(): Float {
        return when (diff) {
            GameDiff.EASY -> 15f
            GameDiff.MEDIUM -> 20f
            GameDiff.HARD -> 25f
        }
    }

    private fun getDiffHeight(): Float {
        return when (diff) {
            GameDiff.EASY -> height / 2f
            GameDiff.MEDIUM -> height / 3f
            GameDiff.HARD -> height / 4f
        }
    }
}

enum class GameMode {
    SOLO,
    VS,
    BOT
}

enum class GameDiff {
    EASY,
    MEDIUM,
    HARD
}